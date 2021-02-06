package com.udacity

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat

fun NotificationManager.sendNotification(messageBody: String, applicationContext: Context) {

    //to support old devices, the builder use NotificationCompat
    val builder = NotificationCompat
            .Builder(applicationContext, "simpleNotification_channel")
            .

}

