package com.example.tyche

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.lifecycleScope
import com.example.tyche.api.ServiceBuilder
import kotlinx.coroutines.launch

class WalletPageFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null
    private lateinit var rootView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_wallet_page, container, false)
        rootView.findViewById<ImageView>(R.id.add_btn)?.setOnClickListener { openCreateStrategyPage() }
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadWalletData()

        if (savedInstanceState == null) {
            val strategyCardFragment = StrategyCardFragment()
            childFragmentManager.beginTransaction()
                .replace(R.id.strategy_card, strategyCardFragment, "STRATEGY_FRAGMENT")
                .commit()
        }
    }

    private fun loadWalletData() {
        val sharedPreferences = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("TOKEN_KEY", null)

        val totalBalanceTextView = rootView.findViewById<TextView>(R.id.total_balance)
        val profitTextView = rootView.findViewById<TextView>(R.id.profit_text)
        val pnlTextView = rootView.findViewById<TextView>(R.id.pnl_text)

        if (token != null) {
            lifecycleScope.launch {
                try {
                    val balanceResponse = ServiceBuilder.apiService.getBalance("Bearer $token")
                    totalBalanceTextView?.text = "$%.2f".format(balanceResponse.balanceUSD)

                    if (profitTextView != null && pnlTextView != null) {
                        val profitResponse = ServiceBuilder.apiService.getProfitPNL("Bearer $token")
                        profitTextView.text = buildString {
                            append(getString(R.string.profit))
                            append(": $%.2f".format(profitResponse.profit))
                        }
                        pnlTextView.text = "PNL: %.2f%%".format(profitResponse.pnlPercent)
                    }

                } catch (e: Exception) {
                    totalBalanceTextView?.text = "Error loading balance"
                }
            }
        }
    }

    private fun openCreateStrategyPage() {
        val fragment = CreateStrategyPageFragment()
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.nav_frame, fragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .addToBackStack(null)
            .commit()
    }

    companion object {
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            WalletPageFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
