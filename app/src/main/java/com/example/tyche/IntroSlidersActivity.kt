package com.example.tyche

import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2

class IntroSlidersActivity : AppCompatActivity() {

    private lateinit var nextButton: Button
    private lateinit var skipButton: Button
    private lateinit var viewPager: ViewPager2
    private lateinit var layouts: Array<Int>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContentView(R.layout.activity_intro_sliders)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        nextButton = findViewById(R.id.nextButton)
        skipButton = findViewById(R.id.skipButton)
        viewPager = findViewById(R.id.viewPager)

        layouts = arrayOf(
            R.layout.slider_1,
            R.layout.slider_2,
            R.layout.slider_3,
            R.layout.slider_4,
            R.layout.slider_5
        )

        viewPager.adapter = object : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
                val view = layoutInflater.inflate(viewType, parent, false)
                return object : RecyclerView.ViewHolder(view) {}
            }

            override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                // No binding needed for static layouts
            }

            override fun getItemCount(): Int = layouts.size

            override fun getItemViewType(position: Int): Int = layouts[position]
        }

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                nextButton.text = if (position == layouts.lastIndex) {
                    getString(R.string.start)
                } else {
                    getString(R.string.next)
                }
            }
        })

        nextButton.setOnClickListener {
            val current = viewPager.currentItem
            if (current < layouts.size - 1) {
                viewPager.currentItem = current + 1
            } else {
                openHomePage()
            }
        }

        skipButton.setOnClickListener { openHomePage() }
    }

    private fun openHomePage() {
        val intent = Intent(this@IntroSlidersActivity, NavMenuActivity::class.java)
        startActivity(intent)
        finish()
    }
}