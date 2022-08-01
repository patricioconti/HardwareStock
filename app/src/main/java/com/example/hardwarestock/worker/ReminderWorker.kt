package com.example.hardwarestock.worker

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.hardwarestock.MainActivity
import com.example.hardwarestock.R



class ReminderWorker(
    context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    // Arbitrary id number
    private val notificationId = 17

    override fun doWork(): Result {

        // Create the content intent for the notification, which launches
        // this activity
        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        //You created the intent, but the notification is displayed outside your app.
        // To make an intent work outside your app, you need to create a new PendingIntent.
        val pendingIntent: PendingIntent = PendingIntent
            .getActivity(applicationContext, 0, intent, 0)


        // Add Style
        val stockImage = BitmapFactory.decodeResource(
            applicationContext.resources,
            R.drawable.stock_picture
        )

        val builder = NotificationCompat.Builder(applicationContext, applicationContext.getString(R.string.channel_id))
            .setSmallIcon(R.drawable.ic_stock_notify)
            .setContentTitle(applicationContext.getString(R.string.update_notification_content_title))
            .setContentText(applicationContext.getString(R.string.update_notification_content_text))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            //When you click the notification, the PendingIntent will be triggered, opening up your MainActivity
            .setContentIntent(pendingIntent)
            //When the user taps on the notification, the notification dismisses itself as it takes them to the app
            .setAutoCancel(true)

             //Set the large icon with setLargeIcon() to the stockImage,
            //so the image will be displayed as an icon when notification is collapsed.
            .setLargeIcon(stockImage)

        with(NotificationManagerCompat.from(applicationContext)) {
            notify(notificationId, builder.build())
        }

        return Result.success()
    }


}

//Cancel all notifications
fun NotificationManager.cancelNotifications() {
    cancelAll()
}
