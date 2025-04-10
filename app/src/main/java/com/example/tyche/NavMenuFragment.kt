package com.example.tyche

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [NavMenuFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NavMenuFragment : Fragment() {
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
        val view = inflater.inflate(R.layout.fragment_nav_menu, container, false)

        val selected_button = arguments?.getString("selected_button")

        val homeButton = view.findViewById<ImageView>(R.id.nav_home)
        val coinListButton = view.findViewById<ImageView>(R.id.nav_coin_list)
        val walletButton = view.findViewById<ImageView>(R.id.nav_wallet)
        val accountButton = view.findViewById<ImageView>(R.id.nav_account)


        // CHANGE NAV MENU ICON COLORS ACCORDING TO THE PAGE OPENED
        if(selected_button == "home"){
            homeButton.setColorFilter(Color.parseColor("#E8AF3B"))
            coinListButton.setColorFilter(Color.WHITE)
            walletButton.setColorFilter(Color.WHITE)
            accountButton.setColorFilter(Color.WHITE)
        }else {
            if(selected_button == "coin_list"){
                coinListButton.setColorFilter(Color.parseColor("#E8AF3B"))
                homeButton.setColorFilter(Color.WHITE)
                walletButton.setColorFilter(Color.WHITE)
                accountButton.setColorFilter(Color.WHITE)
            }else {
                if(selected_button == "wallet"){
                    walletButton.setColorFilter(Color.parseColor("#E8AF3B"))
                    coinListButton.setColorFilter(Color.WHITE)
                    homeButton.setColorFilter(Color.WHITE)
                    accountButton.setColorFilter(Color.WHITE)
                }else {
                    if(selected_button == "account"){
                        accountButton.setColorFilter(Color.parseColor("#E8AF3B"))
                        coinListButton.setColorFilter(Color.WHITE)
                        walletButton.setColorFilter(Color.WHITE)
                        homeButton.setColorFilter(Color.WHITE)
                    }
                }
            }
        }

        homeButton.setOnClickListener {
            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
        }
        coinListButton.setOnClickListener {
            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
        }
        walletButton.setOnClickListener {
            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
        }
        accountButton.setOnClickListener {
            val intent = Intent(requireContext(), Profile::class.java)
            startActivity(intent)
        }

        return view
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment NavMenuFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            NavMenuFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}