package com.example.tyche

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class CoinMarketActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_coin_market)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val coinCardFragment = CoinCardFragment()
        val navFragment = NavMenuFragment()
        val bundle = Bundle()
        bundle.putString("selected_button", "coin_list")
        navFragment.arguments = bundle

        supportFragmentManager.beginTransaction()
            .replace(R.id.navigation, navFragment)
            .replace(R.id.coin_card, coinCardFragment)
            .commit()
    }
}