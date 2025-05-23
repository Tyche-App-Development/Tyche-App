package com.example.tyche

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.tyche.api.ServiceBuilder
import com.example.tyche.api.UserResponse
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfilePageFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfilePageFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null

    private lateinit var rootView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        rootView = inflater.inflate(R.layout.fragment_profile_page, container, false)

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPreferences = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("TOKEN_KEY", null)

        val btnLogout = view.findViewById<Button>(R.id.logoutButton)
        val btnHistory = view.findViewById<Button>(R.id.historico)
        val btnEditProfile = view.findViewById<Button>(R.id.editarInfo)
        val nameTextView = view.findViewById<TextView>(R.id.fullName)
        val usernameTextView = view.findViewById<TextView>(R.id.username)
        val emailTextView = view.findViewById<TextView>(R.id.email)
        val nifTextView = view.findViewById<TextView>(R.id.nif)
        val imageView = view.findViewById<ImageView>(R.id.profileImage)

        btnLogout.setOnClickListener {
            logoutUser()
        }

        btnHistory.setOnClickListener {
            openHistoryPage()
        }

        btnEditProfile.setOnClickListener {
            openEditInfosPage()
        }

        ServiceBuilder.apiService.getUser("Bearer $token").enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    val userProfile = response.body()
                    nameTextView.text = userProfile?.user?.name ?: "Nome não encontrado"
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
    }

    private fun logoutUser() {
        val sharedPref = requireContext().getSharedPreferences("auth", Context.MODE_PRIVATE)
        sharedPref.edit().remove("token").apply()

        val intent = Intent(requireContext(), LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfilePageFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun openHistoryPage() {
        val fragment = HistoryPageFragment()

        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.nav_frame, fragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .addToBackStack(null)
            .commit()
    }

    private fun openEditInfosPage() {
        val fragment = EditProfilePageFragment()

        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.nav_frame, fragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .addToBackStack(null)
            .commit()
    }
}
