package com.udacity

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import com.udacity.utils.Constants

//TODO 2.1
fun NotificationManager.sendNotification(messageBody: String, applicationContext: Context) {

    //to support old devices, the builder use NotificationCompat
    val builder = NotificationCompat
            //the ID needs to be the same as the ID used to create a Channel with createChannel
            .Builder(applicationContext, Constants.DOWNLOAD_CHANNEL_ID)
            .setContentTitle(applicationContext.getString(R.string.notification_title))
            .setContentText(messageBody)
            .setSmallIcon(R.drawable.baseline_cloud_download_notification_icon)

    notify(Constants.MAIN_NOTIFICATION_ID, builder.build())
}


