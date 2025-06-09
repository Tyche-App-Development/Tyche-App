package com.example.tyche

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentTransaction

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class CreateStrategyPage2Fragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private var symbol: String? = null
    private var strategy: String? = null

    private lateinit var rootView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            symbol = it.getString("symbol")
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        rootView = inflater.inflate(R.layout.fragment_create_strategy_page2, container, false)

        rootView.findViewById<ImageView>(R.id.backBtn)?.setOnClickListener { goBack() }
        rootView.findViewById<Button>(R.id.cancelButton)?.setOnClickListener { openHomePage() }
        rootView.findViewById<Button>(R.id.nextButton)?.setOnClickListener { openStratPage3() }

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val cardSafe = view.findViewById<CardView>(R.id.lowRisk)
        val cardIntermediate = view.findViewById<CardView>(R.id.mediumRisk)
        val cardAggressive = view.findViewById<CardView>(R.id.highRisk)
        val nextButton = rootView.findViewById<Button>(R.id.nextButton)

        cardSafe.setOnClickListener {
            cardSafe.setCardBackgroundColor(Color.parseColor("#AE8022"))
            cardIntermediate.setCardBackgroundColor(Color.parseColor("#3A3939"))
            cardAggressive.setCardBackgroundColor(Color.parseColor("#3A3939"))
            strategy = "Low"
            nextButton.isEnabled = true
            nextButton.setBackgroundColor(Color.parseColor("#E8AF3B"))
        }

        cardIntermediate.setOnClickListener {
            cardSafe.setCardBackgroundColor(Color.parseColor("#3A3939"))
            cardIntermediate.setCardBackgroundColor(Color.parseColor("#AE8022"))
            cardAggressive.setCardBackgroundColor(Color.parseColor("#3A3939"))
            strategy = "Medium"
            nextButton.isEnabled = true
            nextButton.setBackgroundColor(Color.parseColor("#E8AF3B"))
        }

        cardAggressive.setOnClickListener {
            cardSafe.setCardBackgroundColor(Color.parseColor("#3A3939"))
            cardIntermediate.setCardBackgroundColor(Color.parseColor("#3A3939"))
            cardAggressive.setCardBackgroundColor(Color.parseColor("#AE8022"))
            strategy = "High"
            nextButton.isEnabled = true
            nextButton.setBackgroundColor(Color.parseColor("#E8AF3B"))
        }
    }


    companion object {
        fun newInstance(symbol: String?) =
            CreateStrategyPage2Fragment().apply {
                arguments = Bundle().apply {
                    putString("symbol", symbol)
                }
                println(symbol)
            }
    }

    private fun openStratPage3() {
        val fragment = CreateStrategyPage3Fragment.newInstance(symbol, strategy)

        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.nav_frame, fragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .addToBackStack(null)
            .commit()
    }

    private fun goBack() {
        requireActivity().onBackPressedDispatcher.onBackPressed()
    }

    private fun openHomePage() {
        val fragment = HomePageFragment()

        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.nav_frame, fragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .addToBackStack(null)
            .commit()
    }
}