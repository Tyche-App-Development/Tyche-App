package com.example.tyche

import StrategyInfo
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import com.example.tyche.api.ServiceBuilder
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class StrategyCardFragment : Fragment() {

    private lateinit var strategiesAdapter: StrategiesAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var emptyState: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_strategies, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.strategies_recycler_view)
        progressBar = view.findViewById(R.id.progress_bar)
        emptyState = view.findViewById(R.id.empty_state)

        if (recyclerView == null) {
            Log.e("StrategyCardFragment", "RecyclerView não encontrado!")
            return
        }

        setupRecyclerView()
        loadStrategies()
    }

    private fun setupRecyclerView() {
        strategiesAdapter = StrategiesAdapter { strategy ->
            onStrategyClick(strategy)
        }

        recyclerView.apply {
            adapter = strategiesAdapter
            layoutManager = LinearLayoutManager(requireContext())
            itemAnimator = DefaultItemAnimator()
            setHasFixedSize(true)
        }
    }

    private fun loadStrategies() {
        showLoading(true)

        val sharedPreferences = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("TOKEN_KEY", null)

        if (token.isNullOrEmpty()) {
            showError("Token não encontrado. Faça login novamente.")
            return
        }

        lifecycleScope.launch {
            try {
                val response = ServiceBuilder.apiService.getUserStrategies("Bearer $token")

                if (response.strategies.isNotEmpty()) {
                    strategiesAdapter.submitList(response.strategies)
                    showEmptyState(false)
                } else {
                    showEmptyState(true)
                }

            } catch (e: HttpException) {
                Log.e("StrategyCardFragment", "Erro HTTP: ${e.code()} - ${e.message()}")
                when (e.code()) {
                    401 -> showError("Sessão expirada. Faça login novamente.")
                    404 -> showError("Nenhuma estratégia encontrada.")
                    500 -> showError("Erro interno do servidor.")
                    else -> showError("Erro: ${e.message()}")
                }
            } catch (e: IOException) {
                Log.e("StrategyCardFragment", "Erro de conexão: ${e.message}")
                showError("Erro de conexão. Verifique sua internet.")
            } catch (e: Exception) {
                Log.e("StrategyCardFragment", "Erro inesperado: ${e.message}")
                showError("Erro inesperado: ${e.message}")
            } finally {
                showLoading(false)
            }
        }
    }

    private fun onStrategyClick(strategy: StrategyInfo) {
        Toast.makeText(
            requireContext(),
            """
    Strategy: ${strategy.botName}
    Saldo: $${"%.2f".format(strategy.balance.current)}
""",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun showLoading(show: Boolean) {
        progressBar.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun showEmptyState(show: Boolean) {
        emptyState.visibility = if (show) View.VISIBLE else View.GONE
        recyclerView.visibility = if (show) View.GONE else View.VISIBLE
    }

    private fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
        showEmptyState(true)
    }
}
