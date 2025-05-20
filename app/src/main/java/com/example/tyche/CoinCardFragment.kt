package com.example.tyche

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import org.json.JSONObject

class CoinCardFragment : Fragment() {

    private val client: OkHttpClient by lazy { OkHttpClient() }
    private lateinit var webSocket: WebSocket

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_coin_card, container, false)
        connectWebSocket()
        return view
    }

    private fun connectWebSocket() {
        val request = Request.Builder().url("ws://192.168.0.135:3001").build()
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

    private fun handleWebSocketMessage(text: String) {
        try {
            val json = JSONObject(text)

            if (!json.has("symbol") || !json.has("price") || !json.has("percent") || !json.has("volume")) {
                return
            }

            val symbol = json.getString("symbol")
            val price = json.getDouble("price")
            val percent = json.getDouble("percent")
            val volume = json.getDouble("volume")

            activity?.runOnUiThread {
                val formattedPrice = "€%.2f".format(price)
                val formattedPercent = "%+.2f%%".format(percent)
                val formattedVolume = "%.2f".format(volume)
                val pnlColor = if (percent >= 0) Color.GREEN else Color.RED

                when (symbol) {
                    "BTC/EUR" -> view?.let { updateCard(it, R.id.coin_symbol_1, R.id.coin_price_1, R.id.coin_pnl_1, R.id.coin_volume_1, symbol, formattedPrice, formattedPercent, formattedVolume, pnlColor) }
                    "ETH/EUR" -> view?.let { updateCard(it, R.id.coin_symbol_2, R.id.coin_price_2, R.id.coin_pnl_2, R.id.coin_volume_2, symbol, formattedPrice, formattedPercent, formattedVolume, pnlColor) }
                    "XRP/EUR" -> view?.let { updateCard(it, R.id.coin_symbol_3, R.id.coin_price_3, R.id.coin_pnl_3, R.id.coin_volume_3, symbol, formattedPrice, formattedPercent, formattedVolume, pnlColor) }
                    "SOL/EUR" -> view?.let { updateCard(it, R.id.coin_symbol_4, R.id.coin_price_4, R.id.coin_pnl_4, R.id.coin_volume_4, symbol, formattedPrice, formattedPercent, formattedVolume, pnlColor) }
                    "BNB/EUR" -> view?.let { updateCard(it, R.id.coin_symbol_5, R.id.coin_price_5, R.id.coin_pnl_5, R.id.coin_volume_5, symbol, formattedPrice, formattedPercent, formattedVolume, pnlColor) }
                    else -> {}
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun reconnectWebSocket() {
        connectWebSocket()
    }

    private fun updateCard(
        view: View,
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
        view.findViewById<TextView>(symbolId).text = symbol
        view.findViewById<TextView>(priceId).text = price
        view.findViewById<TextView>(pnlId).text = percent
        val pnlTextView = view.findViewById<TextView>(pnlId)
        pnlTextView.setTextColor(pnlColor)

        view.findViewById<TextView>(volumeId).text = "Vol: " + volume
    }

    override fun onDestroyView() {
        super.onDestroyView()
        webSocket.cancel()
    }
}
