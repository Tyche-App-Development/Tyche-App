package com.example.tyche

import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.mikephil.charting.charts.CandleStickChart
import com.github.mikephil.charting.data.CandleData
import com.github.mikephil.charting.data.CandleDataSet
import com.github.mikephil.charting.data.CandleEntry

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

        val klineList = listOf(
            listOf(1745967600000, 2.238, 2.2467, 2.2223, 2.2392, 2390360.8),
            listOf(1745971200000, 2.2387, 2.2521, 2.2337, 2.2447, 1560541.7),
            listOf(1745974800000, 2.2451, 2.2457, 2.2252, 2.2382, 904438.6),
            listOf(1745978400000, 2.2379, 2.2486, 2.2329, 2.2418, 367067.2),
            listOf(1745982000000, 2.2419, 2.2508, 2.2357, 2.2489, 515818.9),
            listOf(1745967600000, 2.238, 2.2467, 2.2223, 2.2392, 2390360.8),
            listOf(1745971200000, 2.2387, 2.2521, 2.2337, 2.2447, 1560541.7),
            listOf(1745974800000, 2.2451, 2.2457, 2.2252, 2.2382, 904438.6),
            listOf(1745978400000, 2.2379, 2.2486, 2.2329, 2.2418, 367067.2),
            listOf(1745982000000, 2.2419, 2.2508, 2.2357, 2.2489, 515818.9),
            listOf(1745967600000, 2.238, 2.2467, 2.2223, 2.2392, 2390360.8),
            listOf(1745971200000, 2.2387, 2.2521, 2.2337, 2.2447, 1560541.7),
            listOf(1745974800000, 2.2451, 2.2457, 2.2252, 2.2382, 904438.6),
            listOf(1745978400000, 2.2379, 2.2486, 2.2329, 2.2418, 367067.2),
            listOf(1745982000000, 2.2419, 2.2508, 2.2357, 2.2489, 515818.9),
            listOf(1745967600000, 2.238, 2.2467, 2.2223, 2.2392, 2390360.8),
            listOf(1745971200000, 2.2387, 2.2521, 2.2337, 2.2447, 1560541.7),
            listOf(1745974800000, 2.2451, 2.2457, 2.2252, 2.2382, 904438.6),
            listOf(1745978400000, 2.2379, 2.2486, 2.2329, 2.2418, 367067.2),
            listOf(1745982000000, 2.2419, 2.2508, 2.2357, 2.2489, 515818.9),
            listOf(1745967600000, 2.238, 2.2467, 2.2223, 2.2392, 2390360.8),
            listOf(1745971200000, 2.2387, 2.2521, 2.2337, 2.2447, 1560541.7),
            listOf(1745974800000, 2.2451, 2.2457, 2.2252, 2.2382, 904438.6),
            listOf(1745978400000, 2.2379, 2.2486, 2.2329, 2.2418, 367067.2),
            listOf(1745982000000, 2.2419, 2.2508, 2.2357, 2.2489, 515818.9),
            listOf(1745967600000, 2.238, 2.2467, 2.2223, 2.2392, 2390360.8),
            listOf(1745971200000, 2.2387, 2.2521, 2.2337, 2.2447, 1560541.7),
            listOf(1745974800000, 2.2451, 2.2457, 2.2252, 2.2382, 904438.6),
            listOf(1745978400000, 2.2379, 2.2486, 2.2329, 2.2418, 367067.2),
            listOf(1745982000000, 2.2419, 2.2508, 2.2357, 2.2489, 515818.9),
            listOf(1745967600000, 2.238, 2.2467, 2.2223, 2.2392, 2390360.8),
            listOf(1745971200000, 2.2387, 2.2521, 2.2337, 2.2447, 1560541.7),
            listOf(1745974800000, 2.2451, 2.2457, 2.2252, 2.2382, 904438.6),
            listOf(1745978400000, 2.2379, 2.2486, 2.2329, 2.2418, 367067.2),
            listOf(1745982000000, 2.2419, 2.2508, 2.2357, 2.2489, 515818.9)
        )

        val entries = mutableListOf<CandleEntry>()

        // Example: loop through your Kline array
        for ((index, kline) in klineList.withIndex()) {
            val open = kline[1].toFloat()
            val high = kline[2].toFloat()
            val low = kline[3].toFloat()
            val close = kline[4].toFloat()

            entries.add(CandleEntry(index.toFloat(), high, low, open, close))
        }

        val dataSet = CandleDataSet(entries, "Candlestick Data")
        dataSet.color = Color.rgb(80, 80, 80)
        dataSet.shadowColor = Color.DKGRAY
        dataSet.shadowWidth = 0.7f
        dataSet.decreasingColor = Color.RED
        dataSet.decreasingPaintStyle = Paint.Style.FILL
        dataSet.increasingColor = Color.GREEN
        dataSet.increasingPaintStyle = Paint.Style.FILL
        dataSet.neutralColor = Color.BLUE

        val candleData = CandleData(dataSet)

        val chart = view.findViewById<CandleStickChart>(R.id.candleStickChart)
        chart.data = candleData
        chart.invalidate() // refresh chart

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