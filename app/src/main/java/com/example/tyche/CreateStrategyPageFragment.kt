package com.example.tyche

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.FragmentTransaction

class CreateStrategyPageFragment : Fragment() {
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

        rootView.findViewById<Button>(R.id.createStratBtn)?.setOnClickListener { openStratPage2() }
        rootView.findViewById<ImageView>(R.id.backBtn)?.setOnClickListener { goBack() }
        rootView.findViewById<Button>(R.id.cancelButton)?.setOnClickListener { goBack() }
        rootView.findViewById<Button>(R.id.nextButton)?.setOnClickListener { openStratPage2() }

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val coinCardFragment = CoinCardFragment()

        childFragmentManager.beginTransaction()
            .replace(R.id.coin_card, coinCardFragment)
            .commit()
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CreateStrategyPageFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    private fun openStratPage2() {
        val fragment = CreateStrategyPage2Fragment()

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