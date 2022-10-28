package com.miasahi.ma_sysbiz_notificationhandsfree

import android.app.Notification
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.speech.tts.TextToSpeech
import android.util.Log
import com.miasahi.ma_sysbiz_notificationhandsfree.data.AppHandleType
import java.util.*

class MyNotificationListenerService : NotificationListenerService(), TextToSpeech.OnInitListener {
    private var textToSpeech: TextToSpeech? = null
    override fun onCreate() {
        super.onCreate()
        textToSpeech = TextToSpeech(this, this)
    }

    override fun onDestroy() {
        textToSpeech?.shutdown()
        super.onDestroy()
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        super.onNotificationPosted(sbn)
        val packageName = sbn.packageName
        val handleType = getHandleType(packageName)
        if (handleType == AppHandleType.UNHANDLED) return

        val notificationFlags = sbn.notification.flags
        if (notificationFlags and Notification.FLAG_GROUP_SUMMARY !== 0) return

        val appName = getAppName(packageName)

        val extras = sbn.notification.extras
        val title = extras.getString(Notification.EXTRA_TITLE)
        val text = extras.getCharSequence(Notification.EXTRA_TEXT)

        val speakText = if (handleType == AppHandleType.NOTIFICATION_ONLY)
            "${appName}で${title}からメッセージが届きました。"
        else
            "${appName}で${title}からメッセージが届きました。$text"

        Log.d(
            TAG, "[NotificationPosted] appName:$appName " +
                    "package:$packageName " +
                    "handleType:$handleType " +
                    "title:$title" +
                    "text:$text"
        )
    }

    override fun onInit(status: Int) {
        if (status != TextToSpeech.SUCCESS) {
            return
        }
        textToSpeech?.let { tts ->
            val locale = Locale.getDefault()
            if (tts.isLanguageAvailable(locale) > TextToSpeech.LANG_AVAILABLE) {
                tts.language = locale
            } else {
                Log.w(TAG, "[tts] language setting failed.")
            }
        }
    }

    private fun speak(text: String) {
        Log.w(TAG, "[tts] speak:$text.")
        textToSpeech?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "utteranceId")
    }

    private fun getHandleType(packageName: String): AppHandleType {
        // TODO
        return AppHandleType.UNHANDLED
    }

    private fun getAppName(packageName: String): String {
        val appInfo = packageManager.getApplicationInfo(packageName, 0)
        val appName = packageManager.getApplicationLabel(appInfo)
        return appName.toString()
    }

    companion object {
        const val TAG = "MyNotificationListenerService"
    }
}