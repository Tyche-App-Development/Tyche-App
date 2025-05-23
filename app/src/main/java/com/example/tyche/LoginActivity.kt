package com.example.tyche

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.tyche.api.ServiceBuilder
import com.example.tyche.models.LoginRequest
import com.example.tyche.models.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var errorText: TextView

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Thread.sleep(1000)
        installSplashScreen()
        setContentView(R.layout.activity_login)

        sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)

        usernameEditText = findViewById(R.id.username_input)
        passwordEditText = findViewById(R.id.password_input)
        loginButton = findViewById(R.id.login_button)
        errorText = findViewById(R.id.errorText)

        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                login(username, password)
                errorText.visibility = View.GONE
            } else {
                errorText.visibility = View.VISIBLE
                errorText.text = getString(R.string.errorLoginReg)
            }
        }

        findViewById<TextView>(R.id.signup_link).setOnClickListener{
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
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

                        val intent = Intent(this@LoginActivity, NavMenuActivity::class.java)
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

    private fun getToken(): String? {
        return sharedPreferences.getString("TOKEN_KEY", null)
    }


    private fun clearToken() {
        val editor = sharedPreferences.edit()
        editor.remove("TOKEN_KEY")
        editor.apply()
    }
}
