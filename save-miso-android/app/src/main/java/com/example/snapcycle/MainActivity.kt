package com.example.snapcycle


import android.Manifest
import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.DialogFragment
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.snapcycle.R.id
import com.example.snapcycle.R.layout
import com.google.android.gms.location.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.File


class MainActivity : AppCompatActivity(), ToMapsDialogFragment.NoticeDialogListener {

    val NOTIFICATION_ID = 1005

    // Reminder intervals in milliseconds
    val reminderInterval: Long = 24 * 60 * 60 * 1000 // Daily reminders to recycle
    val dataViewModel: DataViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_main)
        val navView: BottomNavigationView = findViewById(id.navView)

        val navController = findNavController(id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                id.navHome, id.navInstructions, id.navNotifications, id.navMyRecycling
            )
        )
        navView.setupWithNavController(navController)

        setNotificationsTimer()
        getDataFromFileStorage()
        tryUpdateLocation(this)

        // Floating action button set to start classifier activity / camera on click
        val butToCamera: FloatingActionButton = findViewById(id.fabCamera)
        val intentCamera = Intent(this, ClassifierActivity::class.java)
        butToCamera.setOnClickListener {
            startActivity(intentCamera)
        }
    }

    private fun getDataFromFileStorage() {
        // Gets list of recycleables from file storage once activity is started
        // Contents of file are saved in Confirm Add Fragment
        val filename = "datafile"
        val file = File(this.filesDir, filename)
        try {
            dataViewModel.recycleablesList.readFromFile(file)
        } catch (e: Exception) {
            Log.e("MainActivity", "Error reading recycleables list from file")
            e.printStackTrace()
        }

        // Gets saved blue bin location
        // Data is saved in Notifications Fragment
        dataViewModel.readBinLocation(this)
    }

    private fun setNotificationsTimer() {
        // Set up notifications
        val notifyIntent = Intent(this, AlarmReceiver::class.java)
        val notifyPendingIntent = PendingIntent.getBroadcast(
            this,
            NOTIFICATION_ID,
            notifyIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val alarmManager = this.getSystemService(ALARM_SERVICE) as AlarmManager

        // Reset alarm when app opens
        try {
            alarmManager.cancel(notifyPendingIntent)
        } catch (e: Exception) {
            // Do nothing
        }

        alarmManager.setRepeating(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            SystemClock.elapsedRealtime() + reminderInterval,
            reminderInterval,
            notifyPendingIntent
        )
    }

    override fun onResume() {
        super.onResume()
        val navc = findNavController(id.nav_host_fragment)
        if (intent.getIntExtra("FromClassifierActivity", 0) == 1) {
            // go to ConfirmAddFragment from Camera after object is scanned
            if (intent.getStringExtra("ObjectName") == "Non-recyclable") {
                // not sure what has been scanned, go to ManualInputFragment
                navc.navigate(R.id.manualInputFragment)
            } else {
                if (intent.getIntExtra("ItemDetected", 0) == 1) {
                    // item scanned with confidence, go to ConfirmAddFragment
                    navc.navigate(R.id.confirmAddFragment)
                } else {
                    // item scanned but with low confidence, go to manual input fragment
                    navc.navigate(R.id.manualInputFragment)
                }
            }
        }
    }

    private val PERMISSIONS_REQUEST_LOCATION = 99
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    val LOG_DESC = "LocationModel"

    fun tryUpdateLocation(context: Context) {
        var request: LocationRequest = LocationRequest.create()
        request.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        val callback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                val location = locationResult.lastLocation
                dataViewModel.lastLocation = location
            }
        }

        checkLocationPermission(context) // need pop up for location
        if (checkGPSEnabled(context)) {
            // Listen for user location
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
            fusedLocationClient.requestLocationUpdates(request, callback, null)
        }
    }

    fun checkLocationPermission(context: Context) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.d(LOG_DESC, "Requesting location permissions")
            // Not requesting yet...
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf("Manifest.permission.ACCESS_FINE_LOCATION"),
                PERMISSIONS_REQUEST_LOCATION
            )
        }
    }

    fun checkGPSEnabled(context: Context): Boolean {
        // Check GPS enabled
        val locationManager =
            context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        Log.d(LOG_DESC, "Checking GPS enabled")
        val gpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if (!gpsStatus) {
            Toast.makeText(context, "Please enable GPS", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    override fun onDialogPositiveClick(dialog: DialogFragment) {

        var locationString: String =
            "geo:0,0?q=${dataViewModel.binLocation?.latitude},${dataViewModel.binLocation?.longitude}(My Blue Bin)"
        val location = Uri.parse(locationString)

        val mapIntent = Intent(Intent.ACTION_VIEW, location)
        try {
            startActivity(mapIntent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, "You do not have a map app installed.", Toast.LENGTH_SHORT)
        }
    }

    override fun onDialogNegativeClick(dialog: DialogFragment) {
        // Do nothing
    }


}
