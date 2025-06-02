package com.example.tyche

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.tyche.api.RegisterRequest
import com.example.tyche.api.RegisterResponse
import com.example.tyche.api.ServiceBuilder
import com.example.tyche.models.LoginRequest
import com.example.tyche.models.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    private lateinit var errorText: TextView

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)

        val btnRegister = findViewById<Button>(R.id.login_button)
        errorText = findViewById(R.id.errorText)

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

            if (name.isNotEmpty() && username.isNotEmpty() && email.isNotEmpty() && nif.isNotEmpty() && age != 0 && apiKey.isNotEmpty() && apiSecret.isNotEmpty() &&  password.isNotEmpty() && confirmPassword.isNotEmpty()) {
                errorText.visibility = View.GONE
                if (age < 18) {
                    errorText.visibility = View.VISIBLE
                    errorText.text = getString(R.string.errorAge)
                    return@setOnClickListener
                }
                if (password != confirmPassword) {
                    errorText.visibility = View.VISIBLE
                    errorText.text = getString(R.string.errorPassword)
                    return@setOnClickListener
                }
            } else {
                errorText.visibility = View.VISIBLE
                errorText.text = getString(R.string.errorLoginReg)
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
                        login(username, password)
                    } else {
                        errorText.visibility = View.VISIBLE
                        errorText.text = getString(R.string.errorRegister)+ ": " + response.message()
                    }
                }

                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    errorText.visibility = View.VISIBLE
                    errorText.text = getString(R.string.errorRegister)+ ": " + t.message
                }
            })
        }

        findViewById<TextView>(R.id.already_have_account).setOnClickListener{
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun login(username: String, password: String) {
        val loginRequest = LoginRequest(username, password)

        ServiceBuilder.apiService.login(loginRequest).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    loginResponse?.let {

                        saveToken(it.token)

                        val intent = Intent(this@RegisterActivity, IntroSlidersActivity::class.java)
                        startActivity(intent)


                    }
                } else {
                    errorText.visibility = View.VISIBLE
                    errorText.text = getString(R.string.errorLogin) + ": " + response.message()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                errorText.visibility = View.VISIBLE
                errorText.text = getString(R.string.errorLogin) + ": " + t.message
            }
        })
    }

    private fun saveToken(token: String) {
        val editor = sharedPreferences.edit()
        editor.putString("TOKEN_KEY", token)
        editor.apply()
    }
}