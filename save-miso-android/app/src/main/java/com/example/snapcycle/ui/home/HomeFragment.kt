package com.example.snapcycle.ui.home

//import com.example.snapcycle.CameraActivity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.snapcycle.ClassifierActivity
import com.example.snapcycle.R

class HomeFragment : Fragment() {

    var navc: NavController? = null
    lateinit var prefs: SharedPreferences
    lateinit var storedUsername: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navc = Navigation.findNavController(view)

        val butToUnity: ImageView = view.findViewById(R.id.butForest)
        butToUnity.setOnClickListener {
            navc?.navigate(R.id.action_navHome_to_unityFragment)
        }

        // Button to start Camera activity from fragment
        val butToCamera: ImageView = view.findViewById(R.id.butCamera)
        val intent = Intent(activity, ClassifierActivity::class.java)
        butToCamera.setOnClickListener {
            startActivity(intent)
        }

        // Navigate to set up account if this is the first run
        prefs = requireContext().getSharedPreferences("saveMiso_settings", 0)
        storedUsername = prefs.getString("username", "").toString()
        if (storedUsername.isEmpty()) {
            Toast.makeText(
                activity,
                "Welcome to Save Miso! Please set up an account to start recycling",
                Toast.LENGTH_LONG
            ).show()
            navc?.navigate(R.id.navNotifications)
        }
    }
}