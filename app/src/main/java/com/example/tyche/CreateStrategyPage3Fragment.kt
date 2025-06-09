package com.example.tyche

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.lifecycleScope
import com.example.tyche.api.ServiceBuilder
import com.example.tyche.api.ServiceBuilder.apiService
import com.example.tyche.api.Strategy
import com.example.tyche.api.UserResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
class CreateStrategyPage3Fragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private var symbol: String? = null
    private var strategy: String? = null
    private var amount: String? = null

    private lateinit var rootView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            symbol = it.getString("symbol")
            strategy = it.getString("strategy")
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        rootView = inflater.inflate(R.layout.fragment_create_strategy_page3, container, false)

        rootView.findViewById<ImageView>(R.id.backBtn)?.setOnClickListener { goBack() }
        rootView.findViewById<Button>(R.id.cancelButton)?.setOnClickListener { openHomePage() }

        return rootView
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPreferences = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("TOKEN_KEY", null)

        val confirmBtn = rootView.findViewById<Button>(R.id.nextButton)
        val inputAmount = rootView.findViewById<EditText>(R.id.editAmount)
        val balanceText = rootView.findViewById<TextView>(R.id.balanceText)

        lifecycleScope.launch {
            try {
                val response = ServiceBuilder.apiService.getBalanceUSDT("Bearer $token")
                val formatted = "$%.2f".format(response.usdt.amount.toDouble())
                balanceText.setText(formatted)
            } catch (e: Exception) {
                balanceText.setText("Erro saldo: ${e.message}")
            }
        }


        inputAmount?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (!s.isNullOrBlank()) {
                    confirmBtn.isEnabled = true
                    confirmBtn.setBackgroundColor(Color.parseColor("#E8AF3B"))
                    amount = inputAmount.text.toString()
                } else {
                    confirmBtn.isEnabled = false
                    confirmBtn.setBackgroundColor(Color.parseColor("#3A3939"))
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })



        confirmBtn.setOnClickListener {
            if (confirmBtn.isEnabled) {
                val risk = strategy

                if (symbol != null && risk != null && amount != null) {
                    val amountDouble = amount!!.toDouble()
                    val finalStrategy = Strategy(symbol!!, risk, amountDouble)

                    val sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
                    val token = sharedPreferences.getString("TOKEN_KEY", null)

                    if (token != null) {
                        CoroutineScope(Dispatchers.IO).launch {
                            try {
                                val response = ServiceBuilder.apiService.sendStrategy("Bearer $token", finalStrategy)
                                if (response.isSuccessful) {
                                    Log.d("DONE", "POST OK: $finalStrategy")
                                    withContext(Dispatchers.Main) {
                                        openWalletPage()
                                    }
                                } else {
                                    Log.e("ERROR", "Falha ao enviar: ${response.code()}")
                                }
                            } catch (e: Exception) {
                                Log.e("ERROR", "Erro de rede: ${e.message}")
                            }
                        }
                    } else {
                        Log.e("ERROR", "Token não encontrado")
                    }
                } else {
                    Log.e("ERROR", "Algum valor está nulo")
                }
            }
        }


    }

    companion object {
        fun newInstance(symbol: String?, strategy: String?) =
            CreateStrategyPage3Fragment().apply {
                arguments = Bundle().apply {
                    putString("symbol", symbol)
                    putString("strategy", strategy)
                }
                println(symbol + strategy)
            }
    }

    private fun openWalletPage() {
        val fragment = WalletPageFragment()

        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.nav_frame, fragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .addToBackStack(null)
            .commit()
    }

    private fun goBack() {
        requireActivity().onBackPressedDispatcher.onBackPressed()
    }

    private fun openHomePage() {
        val fragment = HomePageFragment()

        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.nav_frame, fragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .addToBackStack(null)
            .commit()
    }
}