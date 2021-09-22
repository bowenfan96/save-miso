package com.example.snapcycle.ui.notifications

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.snapcycle.DataViewModel
import com.example.snapcycle.R
import com.example.snapcycle.ToMapsDialogFragment
import com.example.snapcycle.server.ServerAPI
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class NotificationsFragment : Fragment() {

    lateinit var username: EditText
    lateinit var prefs: SharedPreferences
    lateinit var storedUsername: String
    var serverErrorCode: Int = 0
    val dataViewModel: DataViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_notifications, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setGetUserName()

        val butUpdateBin: Button = view.findViewById(R.id.butUpdateBinLocation)
        if (dataViewModel.isBlueBinSet) {
            butUpdateBin.text = "Update Bin Location"
        } else {
            butUpdateBin.text = "Set Bin Location"
        }
        butUpdateBin.setOnClickListener {
            // Launch google map to check location, then come back to app and record location
            dataViewModel.saveBinLocation(requireContext())

            val dialog = ToMapsDialogFragment()
            dialog.show(childFragmentManager, "mapsDialog")
        }

        prefs = requireContext().getSharedPreferences("saveMiso_settings", 0)
        val storedScore = prefs.getInt("score", 0).toString()
    }


    // THIS WHOLE CHUNK (ONVIEWCREATED) IS FOR CREATING NEW USERNAME AND FETCHING SCORE IN SERVER
    // COPY AND PASTE IT ELSEWHERE IF WE DECIDE TO SHIFT USER CREATION TO ANOTHER PAGE
    fun setGetUserName() {
        val butSignUp: Button = requireView().findViewById(R.id.butSignUp)
        val butSignIn: Button = requireView().findViewById(R.id.butSignIn)
        username = requireView().findViewById(R.id.username)

        prefs = requireContext().getSharedPreferences("saveMiso_settings", 0)
        storedUsername = prefs.getString("username", "").toString()

        // IF THE USERNAME IS ALREADY SET AND STORED, DISPLAY IT IN THE TEXTBOX
        if (storedUsername.isNotEmpty()) {
            username.setText(storedUsername)
            username.isFocusable = true
            username.isFocusableInTouchMode = true
        }

        // for first time users to set a username
        else {
            butSignUp.setOnClickListener {
                ServerAPI
                    .service
                    .createNewUser(username.text.toString())
                    .enqueue(object : Callback<ResponseBody> {
                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                            Toast.makeText(
                                activity,
                                "New user: Not works (Check Internet)",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        override fun onResponse(
                            call: Call<ResponseBody>,
                            response: Response<ResponseBody>
                        ) {
                            if (response.isSuccessful) {
                                val msg = response.body()?.string()
                                Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()
                                // SET USERNAME ON CLIENT SIDE TOO IF SERVER RETURNS SUCCESS
                                val editor = prefs.edit()
                                editor.putString("username", username.text.toString())
                                editor.putInt("score", 0)
                                editor.apply()
                            } else {
                                val msg = response.body()?.string()
                                Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()
                            }
                        }
                    })
            }

            // For existing users to sign in
            butSignIn.setOnClickListener {
                ServerAPI
                    .service
                    .getUserScore(username.text.toString())
                    .enqueue(object : Callback<ResponseBody> {
                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                            val msg = "Trouble reaching server, please check Internet"
                            Toast.makeText(requireActivity(), msg, Toast.LENGTH_LONG).show()
                        }

                        override fun onResponse(
                            call: Call<ResponseBody>,
                            response: Response<ResponseBody>
                        ) {
                            if (response.isSuccessful) {
                                val msg = response.body()?.string()

                                // write SharedPreferences with userscore
                                val editor = prefs.edit()
                                editor.putString("username", username.text.toString())
                                if (msg != null) {
                                    editor.putInt("score", msg.toInt())
                                }
                                editor.apply()

                                Toast.makeText(
                                    requireActivity(),
                                    username.text.toString() + " logged in with a score of " + msg,
                                    Toast.LENGTH_LONG
                                ).show()

                                serverErrorCode = 1
                            } else {
                                val msg = response.body()?.string()
                                Toast.makeText(requireActivity(), msg, Toast.LENGTH_SHORT).show()
                                serverErrorCode = -2
                            }
                        }
                    })
            }
        }
    }
}