package com.example.tyche

import android.content.Context
import android.os.Bundle
import android.os.Debug
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.tyche.api.EditProfileRequest
import com.example.tyche.api.EditProfileResponse
import com.example.tyche.api.ServiceBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [EditProfilePageFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditProfilePageFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var rootView: View
    private lateinit var errorText: TextView
    private var imgUrl: String? = null
    private var fullname: String? = null
    private var username: String? = null
    private var email: String? = null
    private var nif: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            imgUrl = it.getString("imgUrl")
            fullname = it.getString("fullname")
            username = it.getString("username")
            email = it.getString("email")
            nif = it.getString("nif")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        rootView = inflater.inflate(R.layout.fragment_edit_profile_page, container, false)

        rootView.findViewById<ImageView>(R.id.backBtn)?.setOnClickListener { goBack() }
        rootView.findViewById<Button>(R.id.cancelButton)?.setOnClickListener { openProfilePage() }
        rootView.findViewById<Button>(R.id.nextButton)?.setOnClickListener { submitNewInfos() }

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val imgInput = view.findViewById<EditText>(R.id.imgUrl)
        val nameInput = view.findViewById<EditText>(R.id.full_name)
        val usernameInput = view.findViewById<EditText>(R.id.username)
        val emailInput = view.findViewById<EditText>(R.id.email)
        val nifInput = view.findViewById<EditText>(R.id.nif)

        imgInput.setText(imgUrl)
        nameInput.setText(fullname)
        usernameInput.setText(username)
        emailInput.setText(email)
        nifInput.setText(nif)
    }

    companion object {
        fun newInstance(imgUrl: String?, fullname: String?, username: String?, email: String?, nif: String?) =
            EditProfilePageFragment().apply {
                arguments = Bundle().apply {
                    putString("imgUrl", imgUrl)
                    putString("fullname", fullname)
                    putString("username", username)
                    putString("email", email)
                    putString("nif", nif)
                }
            }
    }

    private fun submitNewInfos() {
        val fragment = ProfilePageFragment()

        errorText = rootView.findViewById(R.id.errorText)

        val newImg = rootView.findViewById<EditText>(R.id.imgUrl).text.toString()
        val name = rootView.findViewById<EditText>(R.id.full_name).text.toString()
        val username = rootView.findViewById<EditText>(R.id.username).text.toString()
        val email = rootView.findViewById<EditText>(R.id.email).text.toString()
        val nif = rootView.findViewById<EditText>(R.id.nif).text.toString()
        var apiKey = rootView.findViewById<EditText>(R.id.apiKey).text.toString()
        var apiSecret = rootView.findViewById<EditText>(R.id.apiSecret).text.toString()
        val password = rootView.findViewById<EditText>(R.id.password).text.toString()
        val newPassword = rootView.findViewById<EditText>(R.id.newPassword).text.toString()
        val confirmPassword = rootView.findViewById<EditText>(R.id.confirm_password).text.toString()

        if (newImg.isNotEmpty() && name.isNotEmpty() && username.isNotEmpty() && email.isNotEmpty() && nif.isNotEmpty()) {
            errorText.visibility = View.GONE
            if (password.isNotEmpty() || newPassword.isNotEmpty() || confirmPassword.isNotEmpty()) {
                if (password.isNotEmpty() && newPassword.isNotEmpty() && confirmPassword.isNotEmpty()){
                    if (password != confirmPassword) {
                        errorText.visibility = View.VISIBLE
                        errorText.text = getString(R.string.errorPassword)
                        return
                    }
                } else {
                    errorText.visibility = View.VISIBLE
                    errorText.text = getString(R.string.errorLoginReg)
                    return
                }
            }

            if (apiKey.isNotEmpty() || apiSecret.isNotEmpty()) {
                if (apiKey.isNotEmpty() && apiSecret.isNotEmpty()){
                    if (apiSecret.isEmpty()) {
                        errorText.visibility = View.VISIBLE
                        errorText.text = getString(R.string.errorLoginReg)
                        apiKey = ""
                        apiSecret = ""
                        return
                    }
                } else {
                    errorText.visibility = View.VISIBLE
                    errorText.text = getString(R.string.errorLoginReg)
                    return
                }
            }
        } else {
            errorText.visibility = View.VISIBLE
            errorText.text = getString(R.string.errorLoginReg)
            return
        }

        val request = EditProfileRequest(
            username = username,
            password = password,
            newPassword = newPassword,
            confirmPassword = confirmPassword,
            newImg = newImg,
            name = name,
            email = email,
            nif = nif,
            apiKey = apiKey,
            apiSecret = apiSecret
        )

        val sharedPreferences = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("TOKEN_KEY", null)

        val retrofit = ServiceBuilder.apiService
        retrofit.editUserProfile("Bearer $token", request).enqueue(object : Callback<EditProfileResponse> {
            override fun onResponse(
                call: Call<EditProfileResponse>,
                response: Response<EditProfileResponse>
            ) {
                if (response.isSuccessful) {
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.nav_frame, fragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .addToBackStack(null)
                        .commit()
                } else {
                    errorText.visibility = View.VISIBLE
                    errorText.text = getString(R.string.errorRegister) + ": " + response.message()
                }
            }

            override fun onFailure(call: Call<EditProfileResponse>, t: Throwable) {
                errorText.visibility = View.VISIBLE
                errorText.text = getString(R.string.errorRegister) + ": " + t.message
            }
        })
    }

    private fun goBack() {
        requireActivity().onBackPressedDispatcher.onBackPressed()
    }

    private fun openProfilePage() {
        val fragment = ProfilePageFragment()

        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.nav_frame, fragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .addToBackStack(null)
            .commit()
    }
}