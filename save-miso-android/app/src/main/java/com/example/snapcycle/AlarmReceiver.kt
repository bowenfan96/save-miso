package com.example.snapcycle

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class AlarmReceiver : BroadcastReceiver() {

    lateinit var prefs: SharedPreferences

    val CHANNEL_ID = "SaveMisoNotifChannel"

    override fun onReceive(context: Context, intent: Intent) {
        val mNotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        prefs = context.getSharedPreferences("saveMiso_settings", 0)
        val dateFormatter = SimpleDateFormat("yyyy-MM-dd")
        val lastRecordedDay = prefs.getString("lastDate", "null")

        var daysPassed: Int
        if (lastRecordedDay == "null") {
            daysPassed = 0
        } else {
            val lastDate = dateFormatter.parse(lastRecordedDay)
            println("Date lastDate = $lastDate")
            val currentDate = dateFormatter.format(Date())
            val current = dateFormatter.parse(currentDate)
            val diffInMillies = Math.abs(current.time - lastDate.time)
            daysPassed = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS)
                .toInt()
            println("daysPassed = $daysPassed")
        }

        createNotificationChannel(context)
        showNotification(context, daysPassed)
    }

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel
            val name = "Save Miso Channel"
            val descriptionText = "Notification Channel for Save Miso"
            val importance = NotificationManager.IMPORTANCE_HIGH

            val mChannel = NotificationChannel(CHANNEL_ID, name, importance)
            mChannel.description = descriptionText
            val notificationManager =
                context.getSystemService(AppCompatActivity.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }
    }

    fun showNotification(context: Context, daysPassed: Int) {
        val CHANNEL_ID = "SaveMisoNotifChannel"

        var title = getTitle(daysPassed)
        var text = getText(daysPassed)

        val notificationManager =
            context.getSystemService(AppCompatActivity.NOTIFICATION_SERVICE) as NotificationManager
        var builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.logo)
            .setContentTitle(title)
            .setContentText(text)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
        notificationManager.notify(1995, builder.build())
    }

    private fun getTitle(daysPassed: Int): String {
        if (daysPassed <= 5) {
            return "Don't forget to feed Miso!"
        }
        return "Miso is dying!"
    }

    private fun getText(daysPassed: Int): String {
        if (daysPassed <= 5) {
            return "We haven't seen you recently..."
        }
        return "Recycle something to feed Miso now"
    }
}