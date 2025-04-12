package com.example.tyche

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tyche.api.ServiceBuilder
import com.example.tyche.api.UserResponse
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Profile : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("TOKEN_KEY", null)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profile)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        val btnLogout = findViewById<Button>(R.id.logoutButton)
        btnLogout.setOnClickListener {
            logoutUser()
        }

        val nameTextView = findViewById<TextView>(R.id.fullName)
        val usernameTextView = findViewById<TextView>(R.id.username)
        val emailTextView = findViewById<TextView>(R.id.email)
        val nifTextView = findViewById<TextView>(R.id.nif)
        val imageView = findViewById<ImageView>(R.id.profileImage)


        ServiceBuilder.apiService.getUser("Bearer $token").enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    val userProfile = response.body()
                    nameTextView.text = userProfile?.user?.fullName ?: "Nome não encontrado"
                    usernameTextView.text = userProfile?.user?.username
                    emailTextView.text = userProfile?.user?.email
                    nifTextView.text = userProfile?.user?.nif

                    val profileImageUrl = userProfile?.user?.imageProfile

                    if (!profileImageUrl.isNullOrEmpty()) {
                        Picasso.get()
                            .load(profileImageUrl)
                            .into(imageView)
                    }

                } else {
                    nameTextView.text = "Erro: ${response.code()}"
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                nameTextView.text = "Erro na conexão: ${t.message}"
            }
        })

        val navFragment = NavMenuFragment()
        val bundle = Bundle()
        bundle.putString("selected_button", "account")
        navFragment.arguments = bundle

        supportFragmentManager.beginTransaction()
            .replace(R.id.navigation, navFragment)
            .commit()
    }

    private fun logoutUser() {
        // Remove token
        val sharedPref = getSharedPreferences("auth", MODE_PRIVATE)
        sharedPref.edit().remove("token").apply()

        //Redirect to Login Page
        val intent = Intent(this, Login::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}
