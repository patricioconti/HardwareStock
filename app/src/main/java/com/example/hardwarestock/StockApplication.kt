package com.example.hardwarestock

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import com.example.hardwarestock.data.ItemRoomDatabase


class StockApplication : Application() {

/* Instantiate the database instance by calling getDatabase() on ItemRoomDatabase passing in the context.
 * Use lazy delegate so the instance database is lazily created
 * when you first need/access the reference (rather than when the app starts)
 */

    val database: ItemRoomDatabase by lazy {
        ItemRoomDatabase.getDatabase(this)
    }

    override fun onCreate() {
        super.onCreate()

        //Call create channel
        createChannel(getString(R.string.channel_id),
            getString(R.string.channel_name),
            getString(R.string.channel_description) )


    }


    //Fun to Create Notifications Channel
    private fun createChannel(channelId: String, channelName: String, descriptionText: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = descriptionText
                //Show badge on app icon
                setShowBadge(true)
            }


            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

}

