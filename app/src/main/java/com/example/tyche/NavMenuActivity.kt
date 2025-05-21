package com.example.tyche

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView

class NavMenuActivity : AppCompatActivity() {

    @SuppressLint("CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_nav_menu)

        val rootView = findViewById<View>(R.id.main)
        val navMenu = findViewById<BottomNavigationView>(R.id.nav_menu)

        navMenu.itemRippleColor = ColorStateList.valueOf(Color.TRANSPARENT)
        ViewCompat.setOnApplyWindowInsetsListener(rootView) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Prevent BottomNavigationView from getting extra insets
        ViewCompat.setOnApplyWindowInsetsListener(navMenu) { view, insets ->
            view.setPadding(0, 0, 0, 0)
            insets
        }

        // Optional: Also disable clipping, just in case
        navMenu.clipToPadding = false
        navMenu.clipChildren = false

        // Set initial fragment
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.nav_frame, HomePageFragment())
                .commit()
        }

        navMenu.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.nav_frame, HomePageFragment())
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .commit()
                    true
                }
                R.id.nav_coin_list -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.nav_frame, CoinMarketPageFragment())
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .commit()
                    true
                }
                R.id.nav_wallet -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.nav_frame, WalletPageFragment())
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .commit()
                    true
                }
                R.id.nav_account -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.nav_frame, ProfilePageFragment())
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .commit()
                    true
                }
                else -> false
            }
        }
    }

}
