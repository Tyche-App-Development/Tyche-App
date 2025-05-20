package com.example.tyche

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.tyche.api.RegisterRequest
import com.example.tyche.api.RegisterResponse
import com.example.tyche.api.ServiceBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)


        val btnRegister = findViewById<Button>(R.id.login_button)

        btnRegister.setOnClickListener {
            val name = findViewById<EditText>(R.id.full_name).text.toString()
            val username = findViewById<EditText>(R.id.username).text.toString()
            val email = findViewById<EditText>(R.id.email).text.toString()
            val nif = findViewById<EditText>(R.id.nif).text.toString()
            val age = findViewById<EditText>(R.id.idade).text.toString().toIntOrNull() ?: 0
            val apiKey = findViewById<EditText>(R.id.apikey).text.toString()
            val apiSecret = findViewById<EditText>(R.id.secretkey).text.toString()
            val password = findViewById<EditText>(R.id.password).text.toString()
            val confirmPassword = findViewById<EditText>(R.id.confirm_password).text.toString()

            if (password != confirmPassword) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val request = RegisterRequest(
                username = username,
                password = password,
                name = name,
                email = email,
                age = age,
                nif = nif,
                apiKey = apiKey,
                balance = 0,
                apiSecret = apiSecret
            )

            val retrofit = ServiceBuilder.apiService
            retrofit.registerUser(request).enqueue(object : Callback<RegisterResponse> {
                override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@RegisterActivity, "User registered successfully!", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this@RegisterActivity, "Registration failed: ${response.message()}", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    Toast.makeText(this@RegisterActivity, "Error: ${t.message}", Toast.LENGTH_LONG).show()
                }
            })
        }
    }
}
