package com.udacity

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import com.udacity.utils.Constants
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0
    private var ID: Int = 0

    private lateinit var notificationManager: NotificationManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var action: NotificationCompat.Action

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        /** Sometimes, when you click a RadioButton when the animation is triggered, the RadioGroup
         * can have more than one option clicked, so, whit this clear_button, i fix that problem */
        clear_button.setOnClickListener{
            glide_radioButton.isChecked = false
            loadApp_radioButton.isChecked = false
            retrofit_radioButton.isChecked = false
            buttonGroup_radioGroup.clearCheck()
        }
        /** <>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> */

        /** >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> */
        custom_button.setOnClickListener {
            if (buttonGroup_radioGroup.checkedRadioButtonId == -1) {
                Toast.makeText(
                        applicationContext,
                        "Please select the file to download",
                        Toast.LENGTH_SHORT
                ).show()
            } else {
                when (buttonGroup_radioGroup.checkedRadioButtonId) {
                    glide_radioButton.id -> {
                        glide_radioButton.id = ID
                        glide_radioButton.isChecked = false
                        buttonGroup_radioGroup.clearCheck()
                    }
                    loadApp_radioButton.id -> {
                        loadApp_radioButton.id = ID
                        loadApp_radioButton.isChecked = false
                        buttonGroup_radioGroup.clearCheck()
                    }
                    retrofit_radioButton.id -> {
                        retrofit_radioButton.id = ID
                        retrofit_radioButton.isChecked = false
                        buttonGroup_radioGroup.clearCheck()
                    }
                }
                /*            download(ID)*/

                custom_button.buttonState = ButtonState.Loading
            }

        }
        /** <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< */

        /** Creating a notification channel, the channel id is the same as the channel id used
         * in the builder in NotificationManager.sendNotification Method*/
        //TODO 2.4
        createChannel(Constants.DOWNLOAD_CHANNEL_ID,Constants.DOWNLOAD_CHANNEL_NAME)
}


    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
        }
    }

    private fun download(id: Int) {
        val request =
            DownloadManager.Request(Uri.parse(URL))
                .setTitle(getString(R.string.app_name))
                .setDescription(getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID =
            downloadManager.enqueue(request)// enqueue puts the download request in the queue.
    }

    //TODO 2.3
    private fun createChannel(channelId: String, channelName: String) {
        //START create a channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            val notificationChannel = NotificationChannel(
                    channelId,
                    channelName,
                    NotificationManager.IMPORTANCE_HIGH
            )
                    .apply {
                        setShowBadge(false)
                    }

            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = "amaya"

            val notificationManager = applicationContext.getSystemService(
                    NotificationManager::class.java
            )

            notificationManager.createNotificationChannel(notificationChannel)

        }
    }


    companion object {
        private const val URL =
            "https://github.com/udacity/nd940-c3-advanced-android-" +
                    "programming-project-starter/archive/master.zip"
        private const val CHANNEL_ID = "channelId"
    }

}
