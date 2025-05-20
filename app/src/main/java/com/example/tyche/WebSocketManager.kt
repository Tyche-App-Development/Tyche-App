package com.example.tyche

import android.graphics.Color
import android.view.View
import android.widget.TextView
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import org.json.JSONObject

class WebSocketManager(private val view: View) {

    private val client: OkHttpClient by lazy { OkHttpClient() }
    private lateinit var webSocket: WebSocket

    fun connect() {
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

    fun disconnect() {
        webSocket.cancel()
    }

    private fun handleWebSocketMessage(text: String) {
        try {
            val json = JSONObject(text)

            if (!json.has("symbol") || !json.has("price") || !json.has("percent")) {
                return
            }

            val symbol = json.getString("symbol")
            val price = json.getDouble("price")
            val percent = json.getDouble("percent")

            val formattedPrice = "€%.2f".format(price)
            val formattedPercent = "%+.2f%%".format(percent)
            val pnlColor = if (percent >= 0) Color.GREEN else Color.RED

            when (symbol) {
                "BTC/EUR" -> updateCard(R.id.coin_symbol_1, R.id.coin_price_1, R.id.coin_pnl_1, symbol, formattedPrice, formattedPercent, pnlColor)
                "ETH/EUR" -> updateCard(R.id.coin_symbol_2, R.id.coin_price_2, R.id.coin_pnl_2, symbol, formattedPrice, formattedPercent, pnlColor)
                "XRP/EUR" -> updateCard(R.id.coin_symbol_3, R.id.coin_price_3, R.id.coin_pnl_3, symbol, formattedPrice, formattedPercent, pnlColor)
                "SOL/EUR" -> updateCard(R.id.coin_symbol_4, R.id.coin_price_4, R.id.coin_pnl_4, symbol, formattedPrice, formattedPercent, pnlColor)
                "BNB/EUR" -> updateCard(R.id.coin_symbol_5, R.id.coin_price_5, R.id.coin_pnl_5, symbol, formattedPrice, formattedPercent, pnlColor)
                else -> {}
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun updateCard(
        symbolId: Int,
        priceId: Int,
        pnlId: Int,
        symbol: String,
        price: String,
        percent: String,
        pnlColor: Int
    ) {
        view.findViewById<TextView>(symbolId).text = symbol
        view.findViewById<TextView>(priceId).text = price
        val pnlTextView = view.findViewById<TextView>(pnlId)
        pnlTextView.text = percent
        pnlTextView.setTextColor(pnlColor)
    }

    private fun reconnectWebSocket() {
        connect()
    }
}
