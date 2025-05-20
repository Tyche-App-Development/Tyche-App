package com.example.tyche

import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import com.github.mikephil.charting.charts.CandleStickChart
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.CandleData
import com.github.mikephil.charting.data.CandleDataSet
import com.github.mikephil.charting.data.CandleEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CoinPageFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CoinPageFragment : Fragment() {
    // TODO: Rename and change types of parameters
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
        return inflater.inflate(R.layout.fragment_coin_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val exoTypeface = ResourcesCompat.getFont(requireContext(), R.font.exo_2)

        val klineList = listOf(
                listOf(1704585600000, 0.5678, 0.5717, 0.5483, 0.5512, 255912),
                listOf(1704672000000, 0.5526, 0.5819, 0.5411, 0.577, 615559),
                listOf(1704758400000, 0.5778, 0.5826, 0.5316, 0.5654, 847284),
                listOf(1704844800000, 0.5668, 0.6134, 0.534, 0.6013, 813324),
                listOf(1704931200000, 0.6015, 0.6251, 0.5846, 0.6029, 664096),
                listOf(1705017600000, 0.6013, 0.6033, 0.55, 0.5702, 518873),
                listOf(1705104000000, 0.5706, 0.577, 0.5627, 0.5743, 151337),
                listOf(1705190400000, 0.5748, 0.5916, 0.5714, 0.5779, 153529),
                listOf(1705276800000, 0.5801, 0.589, 0.5678, 0.5747, 197890),
                listOf(1705363200000, 0.5757, 0.5796, 0.5662, 0.5757, 657768),
                listOf(1705449600000, 0.5756, 0.5762, 0.5615, 0.5679, 1186110),
                listOf(1705536000000, 0.5677, 0.5683, 0.5417, 0.5515, 2102782),
                listOf(1705622400000, 0.5513, 0.553, 0.5216, 0.5439, 3018220),
                listOf(1705708800000, 0.5431, 0.5562, 0.5386, 0.5531, 1500383),
                listOf(1705795200000, 0.553, 0.5548, 0.5451, 0.546, 495112),
                listOf(1705881600000, 0.5476, 0.5485, 0.5171, 0.5267, 1436522),
                listOf(1705968000000, 0.5267, 0.5312, 0.4965, 0.5173, 1385150),
                listOf(1706054400000, 0.5173, 0.5189, 0.5107, 0.5178, 733519),
                listOf(1706140800000, 0.5179, 0.5179, 0.5042, 0.5135, 868786),
                listOf(1706227200000, 0.5133, 0.536, 0.508, 0.5323, 913453),
                listOf(1706313600000, 0.5318, 0.5346, 0.5271, 0.53, 327794),
                listOf(1706400000000, 0.5299, 0.5346, 0.5215, 0.5241, 606767),
                listOf(1706486400000, 0.5242, 0.5399, 0.5195, 0.5351, 1593859),
                listOf(1706572800000, 0.5342, 0.5384, 0.5073, 0.5108, 3672849),
                listOf(1706659200000, 0.5098, 0.5142, 0.485, 0.5033, 1715218),
                listOf(1706745600000, 0.5026, 0.511, 0.4898, 0.5059, 1683851),
                listOf(1706832000000, 0.5057, 0.5138, 0.4987, 0.5103, 1710817),
                listOf(1706918400000, 0.5101, 0.5265, 0.5061, 0.5184, 1509769),
                listOf(1707004800000, 0.5186, 0.5189, 0.5011, 0.5033, 655410),
                listOf(1707091200000, 0.5029, 0.5139, 0.4963, 0.506, 1807504),
                listOf(1707177600000, 0.506, 0.5108, 0.4973, 0.5051, 1705688),
                listOf(1707264000000, 0.5051, 0.5154, 0.499, 0.5135, 1845826),
                listOf(1707350400000, 0.5137, 0.5196, 0.5101, 0.5148, 1650777),
                listOf(1707436800000, 0.5153, 0.5284, 0.5141, 0.5261, 2956384),
                listOf(1707523200000, 0.5264, 0.5283, 0.5191, 0.524, 3244672),
                listOf(1707609600000, 0.5235, 0.536, 0.5219, 0.5262, 4849879),
                listOf(1707696000000, 0.526, 0.5386, 0.5144, 0.5318, 3332563),
                listOf(1707782400000, 0.5319, 0.5339, 0.5146, 0.5248, 1458444),
                listOf(1707868800000, 0.5248, 0.5422, 0.5208, 0.5383, 1919424),
                listOf(1707955200000, 0.5383, 0.577, 0.538, 0.5628, 3519824),
                listOf(1708041600000, 0.5628, 0.5786, 0.5526, 0.5647, 2204228),
                listOf(1708128000000, 0.5649, 0.5655, 0.539, 0.5498, 1057026),
                listOf(1708214400000, 0.5492, 0.5623, 0.5476, 0.5572, 553927),
                listOf(1708300800000, 0.5574, 0.5679, 0.5547, 0.5625, 1678121),
                listOf(1708387200000, 0.5626, 0.5763, 0.545, 0.5624, 2658398),
                listOf(1708473600000, 0.5625, 0.5629, 0.5331, 0.5489, 3455964),
                listOf(1708560000000, 0.5488, 0.552, 0.5367, 0.5408, 1841490),
                listOf(1708646400000, 0.5416, 0.5433, 0.5247, 0.5346, 2189341),
                listOf(1708732800000, 0.5341, 0.5481, 0.5284, 0.5449, 865282),
                listOf(1708819200000, 0.5443, 0.5485, 0.542, 0.5425, 695577),
                listOf(1708905600000, 0.5425, 0.5529, 0.5288, 0.5511, 2008653),
                listOf(1708992000000, 0.5508, 0.5977, 0.5481, 0.5866, 3520953),
                listOf(1709078400000, 0.5884, 0.61, 0.5235, 0.5759, 4082530),
                listOf(1709164800000, 0.5753, 0.6259, 0.5681, 0.5864, 4217527),
                listOf(1709251200000, 0.587, 0.6034, 0.5832, 0.6014, 1801867),
                listOf(1709337600000, 0.6016, 0.6509, 0.6, 0.6449, 2166814),
                listOf(1709424000000, 0.6445, 0.6449, 0.5599, 0.6272, 1741870),
                listOf(1709510400000, 0.6272, 0.6656, 0.6131, 0.648, 3259512),
                listOf(1709596800000, 0.6485, 0.6696, 0.52, 0.5925, 4612196),
                listOf(1709683200000, 0.5926, 0.6234, 0.5763, 0.6123, 4564153),
                listOf(1709769600000, 0.6128, 0.6404, 0.6085, 0.6282, 2974121),
                listOf(1709856000000, 0.6283, 0.6345, 0.5985, 0.6218, 2446922),
                listOf(1709942400000, 0.6215, 0.6337, 0.6182, 0.6213, 1924150),
                listOf(1710028800000, 0.6216, 0.6285, 0.5966, 0.6087, 1662601),
                listOf(1710115200000, 0.6087, 0.7443, 0.5809, 0.7235, 8926516),
                listOf(1710201600000, 0.7216, 0.7337, 0.6535, 0.6882, 6282525),
                listOf(1710288000000, 0.688, 0.7026, 0.65, 0.6889, 3893812),
                listOf(1710374400000, 0.6886, 0.7081, 0.6383, 0.6697, 4688632),
                listOf(1710460800000, 0.669, 0.6753, 0.5911, 0.6333, 5554356),
                listOf(1710547200000, 0.6345, 0.6474, 0.5921, 0.6017, 2030564),
                listOf(1710633600000, 0.6026, 0.6244, 0.587, 0.6186, 1599091),
                listOf(1710720000000, 0.6166, 0.6685, 0.5952, 0.645, 4456106),
                listOf(1710806400000, 0.6454, 0.6486, 0.5699, 0.5844, 6188969),
                listOf(1710892800000, 0.5841, 0.6185, 0.5684, 0.611, 2931918),
                listOf(1710979200000, 0.6109, 0.6548, 0.5977, 0.6401, 4937398),
                listOf(1711065600000, 0.6399, 0.6449, 0.5946, 0.6111, 3366133),
                listOf(1711152000000, 0.6111, 0.6326, 0.6034, 0.6168, 1307104),
                listOf(1711238400000, 0.6168, 0.6356, 0.6137, 0.6325, 1241434),
                listOf(1711324800000, 0.6327, 0.6628, 0.6269, 0.6408, 2463151),
                listOf(1711411200000, 0.6404, 0.6532, 0.6269, 0.6324, 2810925),
                listOf(1711497600000, 0.6313, 0.6345, 0.605, 0.6116, 3242419),
                listOf(1711584000000, 0.6114, 0.6378, 0.6033, 0.6245, 3512596),
                listOf(1711670400000, 0.6247, 0.645, 0.6093, 0.6303, 2798294),
                listOf(1711756800000, 0.6306, 0.6373, 0.618, 0.6215, 1161745),
                listOf(1711843200000, 0.6213, 0.63, 0.6209, 0.6293, 671132),
                listOf(1711929600000, 0.6289, 0.6331, 0.5946, 0.6111, 2994328),
                listOf(1712016000000, 0.6112, 0.6185, 0.5385, 0.5847, 4118415),
                listOf(1712102400000, 0.587, 0.5933, 0.5668, 0.5741, 2729165),
                listOf(1712188800000, 0.5741, 0.6166, 0.5627, 0.5936, 5593925),
                listOf(1712275200000, 0.5933, 0.5937, 0.5692, 0.5872, 3138921),
                listOf(1712361600000, 0.5865, 0.5993, 0.5858, 0.593, 842961),
                listOf(1712448000000, 0.5936, 0.6027, 0.5891, 0.5949, 658537),
                listOf(1712534400000, 0.5941, 0.6262, 0.5883, 0.6147, 2980911),
                listOf(1712620800000, 0.6143, 0.6423, 0.6015, 0.6138, 3754633),
                listOf(1712707200000, 0.6135, 0.6202, 0.5917, 0.6173, 3124168),
                listOf(1712793600000, 0.617, 0.6216, 0.603, 0.6086, 2640646),
                listOf(1712880000000, 0.6081, 0.6157, 0.5, 0.547, 7391361),
                listOf(1712966400000, 0.5466, 0.5489, 0.4, 0.4786, 12309357),
                listOf(1713052800000, 0.4785, 0.5074, 0.4646, 0.5037, 4749639),
                listOf(1713139200000, 0.5038, 0.5195, 0.4816, 0.4979, 7417649)
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
            barSpace = 0.1f // reduce candle body width
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

            invalidate() // Refresh chart
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CoinPageFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CoinPageFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}