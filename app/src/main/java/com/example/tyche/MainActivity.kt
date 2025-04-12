package com.example.tyche

import android.content.Context
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tyche.api.ServiceBuilder
import com.example.tyche.api.UserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("TOKEN_KEY", null)


        val navFragment = NavMenuFragment()
        val bundle = Bundle()
        bundle.putString("selected_button", "home")
        navFragment.arguments = bundle

        val nameTextView = findViewById<TextView>(R.id.name)


        ServiceBuilder.apiService.getUser("Bearer $token").enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    val userProfile = response.body()
                    nameTextView.text = userProfile?.user?.fullName ?: "Nome não encontrado"
                } else {
                    nameTextView.text = "Erro: ${response.code()}"
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                nameTextView.text = "Erro na conexão: ${t.message}"
            }
        })



        supportFragmentManager.beginTransaction()
            .replace(R.id.navigation, navFragment)
            .commit()
    }
}