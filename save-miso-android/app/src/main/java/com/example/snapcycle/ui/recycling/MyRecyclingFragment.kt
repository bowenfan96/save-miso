package com.example.snapcycle.ui.recycling

import android.Manifest
import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.snapcycle.DataViewModel
import com.example.snapcycle.R
import com.example.snapcycle.itemModel.ItemAdapter
import com.example.snapcycle.server.ServerAPI
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.material.floatingactionbutton.FloatingActionButton
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class MyRecyclingFragment : Fragment() {


    private val LOG_DESC = "MYRECYCLING"
    private val DISTANCE_THRESHOLD = 50.0

    var navc: NavController? = null
    lateinit var prefs: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    lateinit var storedUsername: String
    lateinit var adapter: ItemAdapter
    var scoreCount: Int = 0
    val dataViewModel: DataViewModel by activityViewModels()

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_my_recycling, container, false)
    }

    @SuppressLint("MissingPermission", "SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navc = Navigation.findNavController(view)
        var recycleablesList = dataViewModel.recycleablesList

        // Clear the intent so that navigation doesn't cause action to be repeated
        activity?.intent?.removeExtra("ObjectName")
        activity?.intent?.removeExtra("ItemDetected")
        activity?.intent?.removeExtra("FromClassifierActivity")

        // Get shared preferences
        prefs = requireContext().getSharedPreferences("saveMiso_settings", 0)
        storedUsername = prefs.getString("username", "")!!
        scoreCount = prefs.getInt("score", 0)

        // Display current score
        val scoreView: TextView = view.findViewById(R.id.scoreMyRecycling)
        scoreView.text = "${scoreCount}g"

        // Recycleables list set up
        val recyclerView = view.findViewById(R.id.tasksRecyclerView) as RecyclerView
        val layoutManager = LinearLayoutManager(context)
        adapter = ItemAdapter(recycleablesList)
        recyclerView.layoutManager = layoutManager
        val itemTouchHelperCallback = SwipeToDeleteCallback()
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
        recyclerView.adapter = adapter

        // Floating action button set to add checked items when pressed
        val butRecycled: FloatingActionButton = view.findViewById(R.id.fabRecycled)

        if (dataViewModel.lastLocation == null) {
            Toast.makeText(
                context,
                "Please enable location services for this app",
                Toast.LENGTH_LONG
            ).show()

            ActivityCompat.requestPermissions(
                this.requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1
            )
        }
        if (dataViewModel.binLocation == null) {
            Toast.makeText(
                context,
                "Please set your recycling bin location for your account",
                Toast.LENGTH_LONG
            ).show()

            navc?.navigate(R.id.navNotifications)
        }

        butRecycled.setOnClickListener {

            // FOR DEBUGGING
            Log.d("MYRECYCLINGFRAGMENT", "${dataViewModel.lastLocation}")
            Log.d("MYRECYCLINGFRAGMENT", "${dataViewModel.binLocation}")
            val distance = dataViewModel.lastLocation!!.distanceTo(dataViewModel.binLocation)
            Log.d("MYRECYCLINGFRAGMENT", "DISTANCE between locations is $distance")

            if (dataViewModel.lastLocation != null && dataViewModel.binLocation != null) {
                if (dataViewModel.lastLocation!!.distanceTo(dataViewModel.binLocation) <= DISTANCE_THRESHOLD) {
                    recycleItems(adapter, scoreView)
                    syncScore()
                } else {
                    Toast.makeText(
                        context,
                        "Please go closer to your recycling bin!",
                        Toast.LENGTH_LONG
                    ).show()
                    // OR START NAVIGATION TO YOUR BIN
                }
            }
        }

    }

    private fun recycleItems(
        adapter: ItemAdapter,
        scoreView: TextView
    ) {
        var scoreIncrease = scoreAndRemoveCheckedItems(adapter)

        scoreCount += scoreIncrease
        editor = prefs.edit()
        editor.putInt("score", scoreCount)
        editor.apply()
        scoreView.text = "${scoreCount}g"
    }

    private fun scoreAndRemoveCheckedItems(adapter: ItemAdapter): Int {
        var scoreIncrease = 0

        for (i in adapter.tasks.list.size - 1 downTo 0 step 1) {
            if (adapter.tasks.list[i].status) {
                scoreIncrease += adapter.tasks.list[i].carbon
                adapter.tasks.removeAt(i)
                adapter.notifyItemRemoved(i)
            }
        }
        // Update internal storage file with list of recycleables
        saveListToFileStorage(adapter)
        return scoreIncrease
    }

    private fun saveListToFileStorage(adapter: ItemAdapter) {
        for (i in adapter.tasks.list.size - 1 downTo 0 step 1) {
            Log.d("LISTITEMS", adapter.tasks.list[i].description)
        }
        dataViewModel.recycleablesList = adapter.tasks
        val filename = "datafile"
        val file = File(requireContext().filesDir, filename)
        try {
            dataViewModel.recycleablesList.storeToFile(file)
            Log.d(LOG_DESC, "saving new list to file...")
        } catch (e: Exception) {
            Log.e("ConfirmAddFragment", "Error writing recycleables list to file")
            e.printStackTrace()
        }
    }

    fun deleteItem(position: Int) {
        Log.d(LOG_DESC, "deleting item at $position")
        adapter.tasks.removeAt(position)
        adapter.notifyItemRemoved(position)

        saveListToFileStorage(adapter)
    }

    inner class SwipeToDeleteCallback : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            Log.d(LOG_DESC, "delete item function called")
            deleteItem(viewHolder.adapterPosition)
        }
    }

    fun syncScore() {
        Toast.makeText(activity, "Backing up score to server, please wait", Toast.LENGTH_LONG)
            .show()
        val localScore = prefs.getInt("score", 0)
        val serverCommand = "$storedUsername;$localScore"
        ServerAPI
            .service
            .setUserScore(serverCommand)
            .enqueue(object : Callback<ResponseBody> {
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Toast.makeText(
                        activity,
                        "Failed to sync score",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    val msg = response.body()?.string()
                    Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()
                }
            })
    }

}
