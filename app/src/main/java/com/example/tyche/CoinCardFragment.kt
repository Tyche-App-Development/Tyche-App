package com.example.tyche

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.tyche.network.WebSocketManager
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import org.json.JSONObject

class CoinCardFragment : Fragment() {

    private val client: OkHttpClient by lazy { OkHttpClient() }
    private lateinit var webSocket: WebSocket
    private lateinit var rootView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        rootView = inflater.inflate(R.layout.fragment_coin_card, container, false)

        WebSocketManager.connect()

        WebSocketManager.registerListener { message ->
            activity?.runOnUiThread {
                handleWebSocketMessage(message)
            }
        }

        setupCardClickListeners()
        return rootView
    }




    private fun connectWebSocket() {
        val request = Request.Builder().url("ws://10.0.2.2:3001").build()
        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {}

            override fun onMessage(webSocket: WebSocket, text: String) {
                handleWebSocketMessage(text)
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                reconnectWebSocket()
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {}
        })
    }

    private fun reconnectWebSocket() {
        connectWebSocket()
    }

    private fun handleWebSocketMessage(text: String) {
        try {
            val json = JSONObject(text)

            if (!json.has("symbol") || !json.has("price") || !json.has("percent") || !json.has("volume")) return

            val symbol = json.getString("symbol")
            val price = json.getDouble("price")
            val percent = json.getDouble("percent")
            val volume = json.getDouble("volume")

            activity?.runOnUiThread {
                val formattedPrice = "$%.2f".format(price)
                val formattedPercent = "%+.2f%%".format(percent)
                val formattedVolume = "%.2f".format(volume)
                val pnlColor = if (percent >= 0) Color.GREEN else Color.RED

                when (symbol) {
                    "BTC/USDT" -> updateCard(R.id.coin_symbol_1, R.id.coin_price_1, R.id.coin_pnl_1, R.id.coin_volume_1, symbol, formattedPrice, formattedPercent, formattedVolume, pnlColor)
                    "ETH/USDT" -> updateCard(R.id.coin_symbol_2, R.id.coin_price_2, R.id.coin_pnl_2, R.id.coin_volume_2, symbol, formattedPrice, formattedPercent, formattedVolume, pnlColor)
                    "XRP/USDT" -> updateCard(R.id.coin_symbol_3, R.id.coin_price_3, R.id.coin_pnl_3, R.id.coin_volume_3, symbol, formattedPrice, formattedPercent, formattedVolume, pnlColor)
                    "SOL/USDT" -> updateCard(R.id.coin_symbol_4, R.id.coin_price_4, R.id.coin_pnl_4, R.id.coin_volume_4, symbol, formattedPrice, formattedPercent, formattedVolume, pnlColor)
                    "BNB/USDT" -> updateCard(R.id.coin_symbol_5, R.id.coin_price_5, R.id.coin_pnl_5, R.id.coin_volume_5, symbol, formattedPrice, formattedPercent, formattedVolume, pnlColor)
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun updateCard(
        symbolId: Int,
        priceId: Int,
        pnlId: Int,
        volumeId: Int,
        symbol: String,
        price: String,
        percent: String,
        volume: String,
        pnlColor: Int
    ) {
        rootView.findViewById<TextView>(symbolId).text = symbol
        rootView.findViewById<TextView>(priceId).text = price
        rootView.findViewById<TextView>(pnlId).apply {
            text = percent
            setTextColor(pnlColor)
        }
        rootView.findViewById<TextView>(volumeId).text = "Vol: $volume"
    }

    private fun setupCardClickListeners() {
        rootView.findViewById<View>(R.id.coin_card_1)?.setOnClickListener { openCoinPage("BTC/USDT") }
        rootView.findViewById<View>(R.id.coin_card_2)?.setOnClickListener { openCoinPage("ETH/USDT") }
        rootView.findViewById<View>(R.id.coin_card_3)?.setOnClickListener { openCoinPage("XRP/USDT") }
        rootView.findViewById<View>(R.id.coin_card_4)?.setOnClickListener { openCoinPage("SOL/USDT") }
        rootView.findViewById<View>(R.id.coin_card_5)?.setOnClickListener { openCoinPage("BNB/USDT") }
    }


    private fun openCoinPage(symbol: String) {
        val imageResId = when (symbol) {
            "BTC/USDT" -> R.drawable.bitcoin_btc_logo
            "ETH/USDT" -> R.drawable.eth_logo
            "XRP/USDT" -> R.drawable.xrp_logo
            "SOL/USDT" -> R.drawable.solana_sol_logo
            "BNB/USDT" -> R.drawable.bnb_bnb_logo
            else -> return
        }

        val fragment = CoinPageFragment.newInstance(symbol, imageResId)

        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.nav_frame, fragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .addToBackStack(null)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        WebSocketManager.unregisterListener(::handleWebSocketMessage)
    }

}
