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
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView

class NavMenuActivity : AppCompatActivity() {

    private val fragmentCache = mutableMapOf<Int, Fragment>()
    private var currentFragmentId: Int = -1

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

        ViewCompat.setOnApplyWindowInsetsListener(navMenu) { view, insets ->
            view.setPadding(0, 0, 0, 0)
            insets
        }

        navMenu.clipToPadding = false
        navMenu.clipChildren = false

        if (savedInstanceState == null) {
            navigateToFragment(R.id.nav_home)
        }

        navMenu.setOnItemSelectedListener { item ->
            navigateToFragment(item.itemId)
            true
        }
    }

    private fun navigateToFragment(menuId: Int) {
        if (currentFragmentId == menuId) {
            return
        }

        val fragment = getOrCreateFragment(menuId)

        supportFragmentManager.beginTransaction()
            .replace(R.id.nav_frame, fragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .commit()

        currentFragmentId = menuId
    }

    private fun getOrCreateFragment(menuId: Int): Fragment {
        return fragmentCache[menuId] ?: createFragment(menuId).also {
            fragmentCache[menuId] = it
        }
    }

    private fun createFragment(menuId: Int): Fragment {
        return when (menuId) {
            R.id.nav_home -> HomePageFragment()
            R.id.nav_coin_list -> CoinMarketPageFragment()
            R.id.nav_wallet -> WalletPageFragment()
            R.id.nav_account -> ProfilePageFragment()
            else -> HomePageFragment()
        }
    }
}
