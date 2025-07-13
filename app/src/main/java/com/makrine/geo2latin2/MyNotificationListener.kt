package com.makrine.geo2latin2

import android.app.Notification
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log

class MyNotificationListener : NotificationListenerService() {

    private val TAG = "MyNotificationListener"

    private val geoToLatinMap = mapOf(
        'ა' to "a", 'ბ' to "b", 'გ' to "g", 'დ' to "d", 'ე' to "e",
        'ვ' to "v", 'ზ' to "z", 'თ' to "t", 'ი' to "i", 'კ' to "k",
        'ლ' to "l", 'მ' to "m", 'ნ' to "n", 'ო' to "o", 'პ' to "p",
        'ჟ' to "zh", 'რ' to "r", 'ს' to "s", 'ტ' to "t", 'უ' to "u",
        'ფ' to "f", 'ქ' to "q", 'ღ' to "gh", 'ყ' to "y", 'შ' to "sh",
        'ჩ' to "ch", 'ც' to "c", 'ძ' to "dz", 'წ' to "w", 'ჭ' to "ch",
        'ხ' to "x", 'ჯ' to "j", 'ჰ' to "h"
    )

    override fun onListenerConnected() {
        super.onListenerConnected()
        Log.d(TAG, "Notification listener connected")
        NotificationHelper.sendLatinNotification(this, "TEST", "Listener is active! Test notification.")
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        Log.d(TAG, "NEW NOTIFICATION from ${sbn.packageName}")
        if (sbn.packageName == "com.facebook.orca") {
            val extras = sbn.notification.extras

            val chatName = extras.getCharSequence(Notification.EXTRA_TITLE)?.toString()
                ?: extras.getCharSequence(Notification.EXTRA_SUMMARY_TEXT)?.toString()
                ?: "Unknown Chat"

            val possibleKeys = listOf(
                Notification.EXTRA_TEXT,
                Notification.EXTRA_BIG_TEXT,
                Notification.EXTRA_SUMMARY_TEXT
            )

            var messageText: String? = null

            for (key in possibleKeys) {
                val text = extras.getCharSequence(key)?.toString()
                if (!text.isNullOrEmpty()) {
                    messageText = text
                    break
                }
            }

            if (messageText == null) {
                val messages = extras.getParcelableArray("android.messages")
                if (messages != null) {
                    val sb = StringBuilder()
                    for (msg in messages) {
                        try {
                            val getTextMethod = msg?.javaClass?.getMethod("getText")
                            val text = getTextMethod?.invoke(msg) as? CharSequence
                            if (text != null) {
                                sb.append(text).append(" ")
                            }
                        } catch (e: Exception) {
                            Log.e(TAG, "Error reading android.messages", e)
                        }
                    }
                    val combinedText = sb.toString().trim()
                    if (combinedText.isNotEmpty()) {
                        messageText = combinedText
                    }
                }
            }

            messageText?.let {
                val latinText = convertGeoToLatin(it)
                var chatNameLatin = convertGeoToLatin(chatName)
                NotificationHelper.sendLatinNotification(this, chatNameLatin, latinText)
            }
        }
    }


    fun convertGeoToLatin(geoText: String): String {
        val sb = StringBuilder()
        for (char in geoText) {
            val latinStr = geoToLatinMap[char]
            if (latinStr != null) {
                sb.append(latinStr)
            } else {
                sb.append(char)
            }
        }
        return sb.toString()
    }
}
