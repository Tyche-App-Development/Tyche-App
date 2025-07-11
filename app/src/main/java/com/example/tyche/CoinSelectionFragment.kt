package com.example.tyche

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentTransaction
import com.example.tyche.network.WebSocketManager
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import org.json.JSONObject

// TODO: Rename parameter arguments, choose names that match
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CoinSelectionFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CoinSelectionFragment : Fragment() {
    private val client: OkHttpClient by lazy { OkHttpClient() }
    private lateinit var webSocket: WebSocket
    private lateinit var rootView: View
    private var symbol: String? = null

    private var symbolSelectedListener: OnSymbolSelectedListener? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        rootView = inflater.inflate(R.layout.fragment_coin_selection, container, false)
        WebSocketManager.connect()

        WebSocketManager.registerListener { message ->
            activity?.runOnUiThread {
                handleWebSocketMessage(message)
            }
        }

        setupCardClickListeners()
        return rootView
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val parent = parentFragment
        if (parent is OnSymbolSelectedListener) {
            symbolSelectedListener = parent
        } else {
            throw RuntimeException("$parent must implement OnSymbolSelectedListener")
        }
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
                val formattedPrice = "€%.2f".format(price)
                val formattedPercent = "%+.2f%%".format(percent)
                val formattedVolume = "%.2f".format(volume)
                val pnlColor = if (percent >= 0) Color.GREEN else Color.RED

                when (symbol) {
                    "BTC/USDT" -> updateCard(R.id.coin_symbol_1, symbol)
                    "ETH/USDT" -> updateCard(R.id.coin_symbol_2, symbol)
                    "XRP/USDT" -> updateCard(R.id.coin_symbol_3, symbol)
                    "SOL/USDT" -> updateCard(R.id.coin_symbol_4, symbol)
                    "BNB/USDT" -> updateCard(R.id.coin_symbol_5, symbol)
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun updateCard(
        symbolId: Int,
        symbol: String
    ) {
        rootView.findViewById<TextView>(symbolId).text = symbol
    }

    private fun setupCardClickListeners() {
        val card1 = rootView.findViewById<CardView>(R.id.coin_card_1)
        val card2 = rootView.findViewById<CardView>(R.id.coin_card_2)
        val card3 = rootView.findViewById<CardView>(R.id.coin_card_3)
        val card4 = rootView.findViewById<CardView>(R.id.coin_card_4)
        val card5 = rootView.findViewById<CardView>(R.id.coin_card_5)

        card1.setOnClickListener {
            card1.setCardBackgroundColor(Color.parseColor("#AE8022"))
            card2.setCardBackgroundColor(Color.parseColor("#3A3939"))
            card3.setCardBackgroundColor(Color.parseColor("#3A3939"))
            card4.setCardBackgroundColor(Color.parseColor("#3A3939"))
            card5.setCardBackgroundColor(Color.parseColor("#3A3939"))
            symbol = "BTC/USDT"
            symbolSelectedListener?.onSymbolSelected(symbol!!, true)
        }
        card2.setOnClickListener {
            card1.setCardBackgroundColor(Color.parseColor("#3A3939"))
            card2.setCardBackgroundColor(Color.parseColor("#AE8022"))
            card3.setCardBackgroundColor(Color.parseColor("#3A3939"))
            card4.setCardBackgroundColor(Color.parseColor("#3A3939"))
            card5.setCardBackgroundColor(Color.parseColor("#3A3939"))
            symbol = "ETH/USDT"
            symbolSelectedListener?.onSymbolSelected(symbol!!, true)
        }
        card3.setOnClickListener {
            card1.setCardBackgroundColor(Color.parseColor("#3A3939"))
            card2.setCardBackgroundColor(Color.parseColor("#3A3939"))
            card3.setCardBackgroundColor(Color.parseColor("#AE8022"))
            card4.setCardBackgroundColor(Color.parseColor("#3A3939"))
            card5.setCardBackgroundColor(Color.parseColor("#3A3939"))
            symbol = "XRP/USDT"
            symbolSelectedListener?.onSymbolSelected(symbol!!, true)
        }
        card4.setOnClickListener {
            card1.setCardBackgroundColor(Color.parseColor("#3A3939"))
            card2.setCardBackgroundColor(Color.parseColor("#3A3939"))
            card3.setCardBackgroundColor(Color.parseColor("#3A3939"))
            card4.setCardBackgroundColor(Color.parseColor("#AE8022"))
            card5.setCardBackgroundColor(Color.parseColor("#3A3939"))
            symbol = "SOL/USDT"
            symbolSelectedListener?.onSymbolSelected(symbol!!, true)
        }
        card5.setOnClickListener {
            card1.setCardBackgroundColor(Color.parseColor("#3A3939"))
            card2.setCardBackgroundColor(Color.parseColor("#3A3939"))
            card3.setCardBackgroundColor(Color.parseColor("#3A3939"))
            card4.setCardBackgroundColor(Color.parseColor("#3A3939"))
            card5.setCardBackgroundColor(Color.parseColor("#AE8022"))
            symbol = "BNB/USDT"
            symbolSelectedListener?.onSymbolSelected(symbol!!, true)
        }
    }

    interface OnSymbolSelectedListener {
        fun onSymbolSelected(symbol: String, selected: Boolean)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        WebSocketManager.unregisterListener(::handleWebSocketMessage)
    }
}