package com.udacity

import android.app.NotificationManager
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.udacity.utils.Constants
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*

class DetailActivity : AppCompatActivity() {

    private lateinit var notificationManager: NotificationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)

        notificationManager = ContextCompat.getSystemService(applicationContext,
                NotificationManager::class.java) as NotificationManager
        notificationManager.cancel(Constants.MAIN_NOTIFICATION_ID)

        val downloadStatusToDetailActivity = intent.getStringExtra("downloadStatus")
        val downloadedFileNameToDetailActivity = intent.getStringExtra("downloadedFileName")
        println("$downloadStatusToDetailActivity @@@@@ $downloadedFileNameToDetailActivity")

        column2FileName_textView.text = downloadedFileNameToDetailActivity
        column2Status_textView.text = downloadStatusToDetailActivity


        detail_button.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            finish()
            startActivity(intent)
        }
    }

}
