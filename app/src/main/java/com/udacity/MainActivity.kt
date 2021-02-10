package com.udacity

import android.Manifest
import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.udacity.utils.Constants
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {

    private val STORAGE_PERMISSION_CODE: Int = 1000
    private lateinit var downloadLink: String
    private lateinit var downloadedFileName: String

    private lateinit var notificationManager: NotificationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        notificationManager = ContextCompat.getSystemService(applicationContext,
                NotificationManager::class.java) as NotificationManager

        /** Creating a notification channel, the channel id is the same as the channel id used
         * in the builder in NotificationManager.sendNotification Method*/
        //TODO 2.4
        createChannel(Constants.DOWNLOAD_CHANNEL_ID,Constants.DOWNLOAD_CHANNEL_NAME)

        /**********************************BUTTONS*************************************************/

        /** Sometimes, when you click a RadioButton when the animation is triggered, the RadioGroup
         * can have more than one option clicked, so, whit this clear_button, i fix that problem */
        clear_button.setOnClickListener{
            glide_radioButton.isChecked = false
            loadApp_radioButton.isChecked = false
            retrofit_radioButton.isChecked = false
            buttonGroup_radioGroup.clearCheck()
        }

        //////////////////////////////////////
        custom_button.setOnClickListener {
            if (buttonGroup_radioGroup.checkedRadioButtonId == -1) {
                Toast.makeText(
                        this,
                        "Please select the file to download",
                        Toast.LENGTH_SHORT
                ).show()
            } else {
                when (buttonGroup_radioGroup.checkedRadioButtonId) {
                    glide_radioButton.id -> {
                        downloadLink = Constants.GLIDE_DOWNLOAD_LINK
                        downloadedFileName = glide_radioButton.text.toString()
                        glide_radioButton.isChecked = false
                        buttonGroup_radioGroup.clearCheck()
                    }
                    loadApp_radioButton.id -> {
                        downloadLink = Constants.LOADAPP_DOWNLOAD_LINK
                        downloadedFileName = loadApp_radioButton.text.toString()
                        loadApp_radioButton.isChecked = false
                        buttonGroup_radioGroup.clearCheck()
                    }
                    retrofit_radioButton.id -> {
                        downloadLink = Constants.RETROFIT_DOWNLOAD_LINK
                        downloadedFileName = retrofit_radioButton.text.toString()
                        retrofit_radioButton.isChecked = false
                        buttonGroup_radioGroup.clearCheck()
                    }
                }
                checkPermissionAndDownload(downloadLink)
            }

        }

        /******************************************************************************************/
    }
    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            DownloadManager.PAUSED_WAITING_FOR_NETWORK
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager

            var rowAndColumnOfInterest: Int? = null

            //Query class may be used to filter downloadManager queries
            val downloadedFileNotifiedViaIntent = id?.let { DownloadManager.Query().setFilterById(it) }
            //query the downloadManager about downloads that have been requested
            val cursor = downloadManager.query(downloadedFileNotifiedViaIntent)

            /** Prints to understand the function and how works Cursors and Queries */
            println("${cursor.columnCount}@@@@@@@@@@@@@@ \n" +
                    "${cursor.columnNames.asList()}@@@@@@@@@@@@@@ \n" +
                    "${cursor.count}")

            //THIS Move the cursor to the first row  (if exist's) and get the int value of the
            // column 7 or status. the possible status are: STATUS_FAILED = 16, STATUS_PAUSED = 4,
            // STATUS_PENDING = 1, STATUS_RUNNING = 2, STATUS_SUCCESSFUL = 8
            if (cursor.moveToFirst()) {
                rowAndColumnOfInterest = cursor.getInt(7)
                println(rowAndColumnOfInterest)
            }
            if(context != null){
                when(rowAndColumnOfInterest){
                    //TODO 2.2
                    8 -> notificationManager.sendNotification(
                            context.getString(R.string.notification_title),
                            context.getString(R.string.notification_body),
                            "Successful",
                            downloadedFileName,
                            context
                    )

                    16 -> notificationManager.sendNotification(
                            context.getString(R.string.notification_title),
                            context.getString(R.string.notification_body),
                            "Failed",
                            downloadedFileName,
                            context
                    )
                }
                custom_button.buttonState = ButtonState.Completed
            }
        }
    }

    private fun checkPermissionAndDownload(URL: String){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_DENIED){
                //show popup for runtime permission
                requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        STORAGE_PERMISSION_CODE)

            }else{
                //animation started
                custom_button.buttonState = ButtonState.Loading
                //permission already granted, perform download
             download(URL)
            }
        }else{
            //animation started
            custom_button.buttonState = ButtonState.Loading
            //system os is less than marshmallow, runtime permission not required, perform download
            download(URL)
        }
    }

    private fun download(URL: String) {
        //Request class contains all the information necessary to request a new download
        val request = DownloadManager.Request(Uri.parse(URL))
                .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or
                                        DownloadManager.Request.NETWORK_MOBILE)
                .setTitle(getString(R.string.app_name))
                .setDescription(getString(R.string.app_description))
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)

                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,
                        "${System.currentTimeMillis()}")
/*
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)
                .setRequiresCharging(false)
                .allowScanningByMediaScanner()

*/

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
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
            notificationChannel.description = "This notification's are to 'notify' when the file is downloaded"
            val notificationManager = applicationContext.getSystemService(
                    NotificationManager::class.java
            )

            notificationManager.createNotificationChannel(notificationChannel)

        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<out String>,
                                            grantResults: IntArray) {
        when(requestCode){
            STORAGE_PERMISSION_CODE ->{
                if(grantResults.isNotEmpty() && grantResults[0]==
                        PackageManager.PERMISSION_GRANTED){
                    //permission from popup was granted, perform download

                }else{
                    //permission from popup was denied, show toast message
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}
