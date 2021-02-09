package com.udacity

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.udacity.utils.Constants

//TODO 2.1
fun NotificationManager.sendNotification(notificationTitle:String,
                                         notificationBody: String,
                                         downloadStatus: String,
                                         downloadedFileName:String,
                                         applicationContext: Context) {


    /** >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> INTENT'S <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< */

    /** An Intent is a messaging object you can use to request an action from another app component
     * Intents can be used for starting an Activity, a service or developing the broadcast
     */
    //here we will use it to open Detail activity after the user press the notification
    val intent = Intent(applicationContext, DetailActivity::class.java)
    /*intent.putExtra()*/
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
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
    )

    /** >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>><<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< */

    /** >>>>>>>>>>>>>>>>>>>>>>>>>>>>>> NOTIFICATION BUILDER <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< */
    //to support old devices, the builder use NotificationCompat
    val builder = NotificationCompat
            //the ID needs to be the same as the ID used to create a Channel with createChannel
            .Builder(applicationContext, Constants.DOWNLOAD_CHANNEL_ID)
            .setContentTitle(notificationTitle)
            .setContentText(notificationBody)
            .setSmallIcon(R.drawable.assistant_back)
            /*.setContentIntent(contentPendingIntent)*/
            .setAutoCancel(true)
            .addAction(R.drawable.assistant_back,
                    applicationContext.getString(R.string.notification_button_text),
                    contentPendingIntent)

    notify(Constants.MAIN_NOTIFICATION_ID, builder.build())


}


