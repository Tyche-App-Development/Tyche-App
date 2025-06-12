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
            Log.e("StrategyCardFragment", "RecyclerView not found!")
            return
        }

        setupRecyclerView()
        loadStrategies()
    }

    private fun setupRecyclerView() {
        strategiesAdapter = StrategiesAdapter { strategy ->

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
            showError("Token not found. Please log in again.")
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
                when (e.code()) {
                    401 -> showError("Session expired. Please log in again.")
                    404 -> showError("No strategies found.")
                    500 -> showError("Internal server error.")
                    else -> showError("Error: ${e.message()}")
                }
            } catch (e: IOException) {
                showError("Network error. Please check your connection.")
            } catch (e: Exception) {
                showError("Unexpected error: ${e.localizedMessage}")
            } finally {
                showLoading(false)
            }
        }
    }

    private fun showLoading(show: Boolean) {
        progressBar.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun showEmptyState(show: Boolean) {
        emptyState.visibility = if (show) View.VISIBLE else View.GONE
        recyclerView.visibility = if (show) View.GONE else View.VISIBLE
    }

    private fun showError(message: String) {
        showEmptyState(true)
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }
}
