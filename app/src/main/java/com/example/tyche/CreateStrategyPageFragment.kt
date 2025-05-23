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
import androidx.fragment.app.FragmentTransaction

class CreateStrategyPageFragment : Fragment(), CoinSelectionFragment.OnSymbolSelectedListener {
    private var symbolSelected: String? = null

    private lateinit var rootView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        rootView = inflater.inflate(R.layout.fragment_create_strategy_page, container, false)

        rootView.findViewById<ImageView>(R.id.backBtn)?.setOnClickListener { goBack() }
        rootView.findViewById<Button>(R.id.cancelButton)?.setOnClickListener { openHomePage() }
        rootView.findViewById<Button>(R.id.nextButton)?.setOnClickListener { openStratPage2() }

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val coinSelectionFragment = CoinSelectionFragment()

        childFragmentManager.beginTransaction()
            .replace(R.id.coin_selection, coinSelectionFragment)
            .commit()
    }

    companion object {
        @JvmStatic
        fun newInstance(symbol: String?) =
            CreateStrategyPageFragment().apply {
                arguments = Bundle().apply {
                    putString("symbol", symbol)
                }
            }
    }

    override fun onSymbolSelected(symbol: String, selected: Boolean) {
        Log.d("ParentFragment", "Selected: $selected, Symbol: $symbol")
        val nextButton = rootView.findViewById<Button>(R.id.nextButton)
        if (selected) {
            nextButton.isEnabled = true
            nextButton.setBackgroundColor(Color.parseColor("#E8AF3B"))
            symbolSelected = symbol
        }
    }

    private fun openStratPage2() {
        val fragment = CreateStrategyPage2Fragment.newInstance(symbolSelected)

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