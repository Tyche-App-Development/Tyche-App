package com.example.tyche

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.tyche.api.BalanceResponse
import com.example.tyche.api.ServiceBuilder
import com.example.tyche.api.UserResponse
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomePageFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomePageFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null

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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPreferences = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("TOKEN_KEY", null)

        val coinCardFragment = CoinCardFragment()
        val nameTextView = view.findViewById<TextView>(R.id.name)

        val totalBalanceTextView = view.findViewById<TextView>(R.id.total_balance)

        lifecycleScope.launch {
            try {
                val response = ServiceBuilder.apiService.getBalance("Bearer $token")
                val formatted = "$%.2f".format(response.balanceUSD)
                totalBalanceTextView.text = formatted
            } catch (e: Exception) {
                totalBalanceTextView.text = "Erro saldo: ${e.message}"
            }
        }

        val lucroTextView = view.findViewById<TextView>(R.id.profit_text)
        val pnlTextView = view.findViewById<TextView>(R.id.pnl_text)

        lifecycleScope.launch {
            try {
                val profitResponse = ServiceBuilder.apiService.getProfitPNL("Bearer $token")
                val pnlList = profitResponse.pnl

                var totalProfit = 0.0

                for (item in pnlList) {
                    totalProfit += item.profit
                }

                lucroTextView.text = "Lucro: $%.2f".format(totalProfit)


                if (pnlList.isNotEmpty()) {
                    val avgPNL = pnlList.map { it.pnlPercent }.average()
                    pnlTextView.text = "PNL: %.2f%%".format(avgPNL)
                } else {
                    pnlTextView.text = "PNL: 0.00%"
                }

            } catch (e: Exception) {
                lucroTextView.text = "Erro lucro"
                pnlTextView.text = "Erro PNL"
            }
        }



        ServiceBuilder.apiService.getUser("Bearer $token").enqueue(object :
            Callback<UserResponse> {
                override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                    if (response.isSuccessful) {
                        val userProfile = response.body()
                        nameTextView.text = userProfile?.user?.name ?: "Nome não encontrado"
                    } else {
                        nameTextView.text = "Erro: ${response.code()}"
                    }
                }

                override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                    nameTextView.text = "Erro na conexão: ${t.message}"
                }
            })

        childFragmentManager.beginTransaction()
            .replace(R.id.coin_card, coinCardFragment)
            .commit()
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomePageFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }

            }
    }
}