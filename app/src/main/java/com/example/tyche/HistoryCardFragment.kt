package com.example.tyche

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tyche.api.ServiceBuilder
import com.example.tyche.api.TradeHistoryResponse
import kotlinx.coroutines.launch

class HistoryCardFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_history_card, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.tradeRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val token = requireContext()
            .getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
            .getString("TOKEN_KEY", null)

        if (token != null) {
            lifecycleScope.launch {
                try {
                    val response: TradeHistoryResponse = ServiceBuilder.apiService.getHistoryTrade("Bearer $token")
                    recyclerView.adapter = TradeAdapter(response.trades)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}
