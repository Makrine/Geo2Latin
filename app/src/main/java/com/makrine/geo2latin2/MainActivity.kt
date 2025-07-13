package com.makrine.geo2latin2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startActivity(Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"))

        findViewById<Button>(R.id.btn_test_notification).setOnClickListener {
            val latinText = MyNotificationListener().convertGeoToLatin("ტესტი")
            NotificationHelper.sendLatinNotification(this, "Title", latinText)
        }
    }
}
