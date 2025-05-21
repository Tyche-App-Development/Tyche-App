package com.example.tyche

import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.example.tyche.network.WebSocketManager
import com.github.mikephil.charting.charts.CandleStickChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.CandleData
import com.github.mikephil.charting.data.CandleDataSet
import com.github.mikephil.charting.data.CandleEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class CoinPageFragment : Fragment() {

    private var symbol: String? = null
    private var price: String? = null
    private var percent: String? = null
    private var imageResId: Int? = null

    private var listener: ((String) -> Unit)? = null

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
    ): View? {
        return inflater.inflate(R.layout.fragment_coin_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val exoTypeface = ResourcesCompat.getFont(requireContext(), R.font.exo_2)

        val symbolNow = symbol ?: ""
        val subtitleView = view.findViewById<TextView>(R.id.subtitle)
        val pnlView = view.findViewById<TextView>(R.id.coin_pnl)

        view.findViewById<TextView>(R.id.coinName).text = symbolNow
        subtitleView.text = price
        pnlView.text = percent
        view.findViewById<ImageView>(R.id.coinLogoImage).setImageResource(imageResId ?: R.drawable.logo)

        WebSocketManager.connect()
        listener = { text ->
            try {
                val json = JSONObject(text)
                if (json.getString("symbol") == symbolNow) {
                    val livePrice = json.getDouble("price")
                    val livePercent = json.getDouble("percent")

                    val formattedPrice = "€%.2f".format(livePrice)
                    val formattedPercent = "%+.2f%%".format(livePercent)
                    val pnlColor = if (livePercent >= 0) Color.GREEN else Color.RED

                    activity?.runOnUiThread {
                        subtitleView.text = formattedPrice
                        pnlView.text = formattedPercent
                        pnlView.setTextColor(pnlColor)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        WebSocketManager.registerListener(listener!!)

        val klineList = listOf(
            listOf(1704585600000, 0.5678, 0.5717, 0.5483, 0.5512, 255912),
            listOf(1704672000000, 0.5526, 0.5819, 0.5411, 0.577, 615559),
            listOf(1704758400000, 0.5778, 0.5826, 0.5316, 0.5654, 847284),
            listOf(1704844800000, 0.5668, 0.6134, 0.534, 0.6013, 813324),
            listOf(1704931200000, 0.6015, 0.6251, 0.5846, 0.6029, 664096),
            listOf(1705017600000, 0.6013, 0.6033, 0.55, 0.5702, 518873)
        )

        val entries = mutableListOf<CandleEntry>()
        val timestamps = mutableListOf<Long>()

        for ((index, kline) in klineList.withIndex()) {
            val timestamp = kline[0].toLong()
            val open = kline[1].toFloat()
            val high = kline[2].toFloat()
            val low = kline[3].toFloat()
            val close = kline[4].toFloat()
            entries.add(CandleEntry(index.toFloat(), high, low, open, close))
            timestamps.add(timestamp)
        }

        val dateFormatter = object : ValueFormatter() {
            private val sdf = SimpleDateFormat("dd/MM/yy", Locale.getDefault())
            override fun getFormattedValue(value: Float): String {
                val index = value.toInt()
                return if (index in timestamps.indices) {
                    sdf.format(Date(timestamps[index]))
                } else {
                    ""
                }
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
        val chart = view.findViewById<CandleStickChart>(R.id.candleStickChart)

        chart.apply {
            data = candleData
            setBackgroundColor(Color.TRANSPARENT)
            description.isEnabled = false
            legend.isEnabled = false

            xAxis.apply {
                valueFormatter = dateFormatter
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
            invalidate()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        listener?.let { WebSocketManager.unregisterListener(it) }
    }

    companion object {
        fun newInstance(symbol: String, imageResId: Int) =
            CoinPageFragment().apply {
                arguments = Bundle().apply {
                    putString("symbol", symbol)
                    putInt("imageResId", imageResId)
                }
            }
    }
}