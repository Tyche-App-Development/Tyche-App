package com.example.tyche.network

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import org.json.JSONObject
import org.slf4j.MDC.put

object WebSocketManager {

    private var webSocket: WebSocket? = null
    private val client = OkHttpClient()
    private val listeners = mutableListOf<(String) -> Unit>()

    private const val SOCKET_URL = "ws://10.0.2.2:3001"

    fun connect() {
        if (webSocket == null) {
            val request = Request.Builder().url(SOCKET_URL).build()
            webSocket = client.newWebSocket(request, object : WebSocketListener() {
                override fun onOpen(ws: WebSocket, response: Response) {

                }

                override fun onMessage(ws: WebSocket, text: String) {
                    listeners.forEach { it.invoke(text) }
                }

                override fun onFailure(ws: WebSocket, t: Throwable, response: Response?) {
                    reconnect()
                }

                override fun onClosing(ws: WebSocket, code: Int, reason: String) {
                    ws.close(code, reason)
                }
            })
        }
    }

    private fun reconnect() {
        disconnect()
        connect()
    }

    fun disconnect() {
        webSocket?.close(1000, null)
        webSocket = null
    }

    fun requestKlines(symbol: String) {
        webSocket?.send(JSONObject().apply {
            put("type", "get_klines")
            put("symbol", symbol)
        }.toString())
    }

    fun registerListener(listener: (String) -> Unit) {
        listeners.add(listener)
    }

    fun unregisterListener(listener: (String) -> Unit) {
        listeners.remove(listener)
    }
}
