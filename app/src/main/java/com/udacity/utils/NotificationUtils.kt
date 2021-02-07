package com.udacity

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import com.udacity.utils.Constants

//TODO 2.1
fun NotificationManager.sendNotification(messageBody: String, applicationContext: Context) {


    /** >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> INTENT'S <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< */

    /** An Intent is a messaging object you can use to request an action from another app component
     * Intents can be used for starting an Activity, a service or developing the broadcast
     */
    //here we will use it to open Detail activity after the user press the notification
    val contentIntent = Intent(applicationContext, DetailActivity::class.java)

    /** A PendingIntent grants rights to another application or the system to perfom an operation
     * on behalf of your application. A PendingIntent itself is simply a reference to a token
     * maintained by the system describing the original data used to retrieve it.
     * THIS MEANS THAT EVEN IF THE APP IS CLOSED OR KILLED, with the notification, we will enter
     * to the activity that we want*/
    // The PendingIntent.FLAG's specifies the option to create a new PendingIntent or use an existing
    // one, i set flag_update_current because i dont want to create a new notification, but update
    // one if it exist
    val contentPendingIntent= PendingIntent.getActivity(
            applicationContext,
            Constants.MAIN_NOTIFICATION_ID,
            contentIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
    )

    /** >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>><<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< */

    /** >>>>>>>>>>>>>>>>>>>>>>>>>>>>>> NOTIFICATION BUILDER <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< */
    //to support old devices, the builder use NotificationCompat
    val builder = NotificationCompat
            //the ID needs to be the same as the ID used to create a Channel with createChannel
            .Builder(applicationContext, Constants.DOWNLOAD_CHANNEL_ID)
            .setContentTitle(applicationContext.getString(R.string.notification_title))
            .setContentText(messageBody)
            .setSmallIcon(R.drawable.baseline_cloud_download_notification_icon)
            .setContentIntent(contentPendingIntent)
            .setAutoCancel(true)

    notify(Constants.MAIN_NOTIFICATION_ID, builder.build())


}


