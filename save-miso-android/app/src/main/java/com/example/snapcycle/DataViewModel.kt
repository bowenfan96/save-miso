package com.example.snapcycle

import android.content.Context
import android.location.Location
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.snapcycle.itemModel.ItemList
import java.io.File
import java.io.FileNotFoundException

// Holds app level user data
class DataViewModel : ViewModel() {
    private var recycleItemType: String = ""
    private var recycleItemCount: Int = 1

    var recycleablesList = ItemList()
    var binLocation: Location? = null
    var lastLocation: Location? = null
    var isBlueBinSet: Boolean = false

    fun setRecycleItemType(str: String) {
        recycleItemType = str
    }

    fun getRecycleItemType(): String {
        return recycleItemType
    }

    fun setRecycleItemCount(count: Int) {
        recycleItemCount = count
    }

    fun getRecycleItemCount(): Int {
        return recycleItemCount
    }

    fun readBinLocation(context: Context) {
        val binFileName = "datafilebin"
        val binFile = File(context.filesDir, binFileName)
        try {
            readBinLocationFromFile(binFile)
            Log.d("MAINACTIVITY", "Location read from file is: ${binLocation}")
        } catch (e: FileNotFoundException) {
            binLocation = null
        } catch (e: Exception) {
            Log.e("MainActivity", "Error reading bin location from file")
            e.printStackTrace()
        }
    }

    fun readBinLocationFromFile(binFile: File) {
        var locationRead = Location("dummyprovider")
        var binFileInput: List<String> = binFile.readLines()
        if (binFileInput.isNotEmpty()) {
            locationRead.longitude = binFileInput.get(0).toDouble()
            locationRead.latitude = binFileInput.get(1).toDouble()
            locationRead.time = binFileInput.get(2).toLong()
            isBlueBinSet = true
        }
        binLocation = locationRead
    }

    fun saveBinLocation(context: Context) {
        if (binLocation == null) {
            // Use last known location to open a map app with
            binLocation = lastLocation
            isBlueBinSet = true
            Log.d(
                "NOTIFICATIONSFRAGMENT",
                "Setting blue bin location to: ${binLocation}"
            )

            // Save blue bin location to file
            val filename = "datafilebin"
            val file = File(context.filesDir, filename)
            Log.d(
                "NOTIFICATIONSFRAGMENT",
                "SAVING BIN LOCATION AS ${binLocation}"
            )
            saveBinLocationToFile(file)
        } else {
            Log.d(
                "NOTIFICATIONSFRAGMENT",
                "Bin location already is: ${binLocation}"
            )
        }
    }

    fun saveBinLocationToFile(binFile: File) {
        val fileContent = "${binLocation?.longitude}\n" +
                "${binLocation?.latitude}\n" +
                "${binLocation?.time}"
        binFile.writeText(fileContent)
    }

}