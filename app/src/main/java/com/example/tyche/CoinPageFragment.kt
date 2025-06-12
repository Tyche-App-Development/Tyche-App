package com.example.tyche

import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.tyche.api.ServiceBuilder
import com.example.tyche.network.WebSocketManager
import com.github.mikephil.charting.charts.CandleStickChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.CandleData
import com.github.mikephil.charting.data.CandleDataSet
import com.github.mikephil.charting.data.CandleEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import org.json.JSONObject
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class CoinPageFragment : Fragment() {

    private var symbol: String? = null
    private var price: String? = null
    private var percent: String? = null
    private var imageResId: Int? = null

    private var listener: ((String) -> Unit)? = null
    private lateinit var chart: CandleStickChart
    private lateinit var rootView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            symbol = it.getString("symbol")
            price = it.getString("price")
            percent = it.getString("percent")
            imageResId = it.getInt("imageResId")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        rootView = inflater.inflate(R.layout.fragment_coin_page, container, false)

        rootView.findViewById<Button>(R.id.createStratBtn)?.setOnClickListener { openStrat2Page() }
        rootView.findViewById<ImageView>(R.id.backBtn)?.setOnClickListener { goBack() }

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val exoTypeface = ResourcesCompat.getFont(requireContext(), R.font.exo_2)

        val symbolNow = symbol ?: return
        val subtitleView = view.findViewById<TextView>(R.id.subtitle)
        val pnlView = view.findViewById<TextView>(R.id.coin_pnl)
        chart = view.findViewById(R.id.candleStickChart)

        view.findViewById<TextView>(R.id.coinName).text = symbolNow
        subtitleView.text = price ?: ""
        pnlView.text = percent ?: ""
        view.findViewById<ImageView>(R.id.coinLogoImage).setImageResource(imageResId ?: R.drawable.logo)

        chart.apply {
            setBackgroundColor(Color.TRANSPARENT)
            description.isEnabled = false
            legend.isEnabled = false
            xAxis.apply {
                valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String = ""
                }
                textColor = Color.WHITE
                textSize = 12f
                position = XAxis.XAxisPosition.BOTTOM
                setDrawGridLines(true)
                granularity = 1f
                labelRotationAngle = -30f
                typeface = exoTypeface
                setLabelCount(7, true)
            }
            axisRight.apply {
                textColor = Color.WHITE
                textSize = 14f
                setDrawGridLines(true)
                typeface = exoTypeface
                setLabelCount(12, true)
            }
            axisLeft.isEnabled = false
        }

        WebSocketManager.connect()
        WebSocketManager.requestKlines(symbolNow)

        val coinId = mapSymbolToCoinId(symbolNow)
        fetchCoinDetails(coinId, view)

        symbol?.let { fetchCoinDetails(it.lowercase(Locale.ROOT), view) }

        listener = { text ->
            try {
                val json = JSONObject(text)
                val type = json.optString("type")
                val symbolJson = json.optString("symbol")

                if (type == "ticker" && symbolJson == symbolNow) {
                    val livePrice = json.getDouble("price")
                    val livePercent = json.getDouble("percent")

                    val formattedPrice = "$%.2f".format(livePrice)
                    val formattedPercent = "%+.2f%%".format(livePercent)
                    val pnlColor = if (livePercent >= 0) Color.GREEN else Color.RED

                    activity?.runOnUiThread {
                        subtitleView.text = formattedPrice
                        pnlView.text = formattedPercent
                        pnlView.setTextColor(pnlColor)

                        val candleData = chart.data
                        val dataSet = candleData?.getDataSetByIndex(0) as? CandleDataSet
                        val lastEntry = dataSet?.getEntryForIndex(dataSet.entryCount - 1)

                        if (lastEntry != null) {
                            lastEntry.close = livePrice.toFloat()
                            if (livePrice > lastEntry.high) lastEntry.high = livePrice.toFloat()
                            if (livePrice < lastEntry.low) lastEntry.low = livePrice.toFloat()

                            candleData.notifyDataChanged()
                            chart.notifyDataSetChanged()
                            chart.invalidate()
                        }
                    }
                }

                if (type == "klines" && symbolJson == symbolNow) {
                    val dataArray = json.getJSONArray("data")
                    val entries = mutableListOf<CandleEntry>()
                    val timestamps = mutableListOf<Long>()

                    for (i in 0 until dataArray.length()) {
                        val candle = dataArray.getJSONArray(i)
                        val timestamp = candle.getLong(0)
                        val open = candle.getDouble(1).toFloat()
                        val high = candle.getDouble(2).toFloat()
                        val low = candle.getDouble(3).toFloat()
                        val close = candle.getDouble(4).toFloat()

                        entries.add(CandleEntry(i.toFloat(), high, low, open, close))
                        timestamps.add(timestamp)
                    }

                    val dateFormatter = object : ValueFormatter() {
                        private val sdf = SimpleDateFormat("dd/MM/yy", Locale.getDefault())
                        override fun getFormattedValue(value: Float): String {
                            val index = value.toInt()
                            return if (index in timestamps.indices) {
                                sdf.format(Date(timestamps[index]))
                            } else ""
                        }
                    }

                    val dataSet = CandleDataSet(entries, "Candlestick Data").apply {
                        shadowColor = Color.DKGRAY
                        shadowWidth = 1f
                        decreasingColor = Color.RED
                        decreasingPaintStyle = Paint.Style.FILL
                        increasingColor = Color.GREEN
                        increasingPaintStyle = Paint.Style.FILL
                        neutralColor = Color.BLUE
                        barSpace = 0.1f
                    }

                    val candleData = CandleData(dataSet)

                    activity?.runOnUiThread {
                        chart.data = candleData
                        chart.xAxis.valueFormatter = dateFormatter
                        chart.notifyDataSetChanged()
                        chart.invalidate()
                    }
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        WebSocketManager.registerListener(listener!!)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        listener?.let { WebSocketManager.unregisterListener(it) }
    }

    companion object {
        fun newInstance(symbol: String, imageResId: Int, price: String = "", percent: String = "") =
            CoinPageFragment().apply {
                arguments = Bundle().apply {
                    putString("symbol", symbol)
                    putString("price", price)
                    putString("percent", percent)
                    putInt("imageResId", imageResId)
                }
            }
    }

    private fun mapSymbolToCoinId(symbol: String): String {
        return when (symbol.uppercase(Locale.ROOT)) {
            "BTC/USDT" -> "bitcoin"
            "ETH/USDT" -> "ethereum"
            "XRP/USDT" -> "ripple"
            "SOL/USDT" -> "solana"
            "BNB/USDT" -> "binancecoin"
            else -> symbol.lowercase(Locale.ROOT)
        }
    }


    private fun fetchCoinDetails(coinId: String, view: View) {
        val url = ServiceBuilder.BASE_URL + "coin/$coinId"

        val client = okhttp3.OkHttpClient()
        val request = okhttp3.Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                Log.e("CoinDetails", "Erro: ${e.message}")
            }

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                val responseBody = response.body?.string()
                if (!response.isSuccessful || responseBody == null) return

                try {
                    val json = JSONObject(responseBody).getJSONObject("coin")
                    val rank = json.getInt("rank")
                    val supply = json.getDouble("circulating_supply")
                    val marketCap = json.getDouble("market_cap_usd")
                    val high = json.getDouble("all_time_high")
                    val low = json.getDouble("all_time_low")
                    val issueDate = json.getString("issue_date")

                    activity?.runOnUiThread {
                        view?.findViewById<TextView>(R.id.coinRank)?.text = "#$rank"
                        view?.findViewById<TextView>(R.id.coinSupply)?.text = supply.toString()
                        view?.findViewById<TextView>(R.id.coinMarketCap)?.text = "$%.2f".format(marketCap)
                        view?.findViewById<TextView>(R.id.coinAllTimeHigh)?.text = "$%.2f".format(high)
                        view?.findViewById<TextView>(R.id.coinAllTimeLow)?.text = "$%.2f".format(low)
                        view?.findViewById<TextView>(R.id.coinIssueDate)?.text = issueDate
                    }
                } catch (e: Exception) {
                    Log.e("CoinDetails", "Parse error: ${e.message}")
                }
            }

        })
    }

    private fun openStrat2Page() {
        val fragment = CreateStrategyPage2Fragment.newInstance(symbol)

        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.nav_frame, fragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .addToBackStack(null)
            .commit()
    }

    private fun goBack() {
        requireActivity().onBackPressedDispatcher.onBackPressed()
    }
}
