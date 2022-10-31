package com.miasahi.ma_sysbiz_notificationhandsfree

import android.app.Notification
import android.content.Context
import android.content.pm.PackageManager
import android.media.AudioDeviceInfo
import android.media.AudioManager
import android.os.Build
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.room.Room
import com.miasahi.ma_sysbiz_notificationhandsfree.data.AppHandleType
import com.miasahi.ma_sysbiz_notificationhandsfree.database.AppDatabase
import com.miasahi.ma_sysbiz_notificationhandsfree.database.dao.ListInfoDao
import com.miasahi.ma_sysbiz_notificationhandsfree.database.dao.SettingInfoDao
import kotlinx.coroutines.*
import java.util.*

class MyNotificationListenerService : NotificationListenerService(), TextToSpeech.OnInitListener {
    private var textToSpeech: TextToSpeech? = null
    private lateinit var db: AppDatabase
    private lateinit var listInfoDao: ListInfoDao
    private lateinit var settingInfoDao: SettingInfoDao
    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    override fun onCreate() {
        super.onCreate()
        textToSpeech = TextToSpeech(this, this)
        initDatabase()
    }

    override fun onDestroy() {
        textToSpeech?.shutdown()
        scope.cancel()
        super.onDestroy()
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        super.onNotificationPosted(sbn)
        val notificationFlags = sbn.notification.flags
        if (notificationFlags and Notification.FLAG_GROUP_SUMMARY != 0) return
        if (checkBluetoothDeviceConnected()) return

        Log.d(TAG,"[onNotificationPosted] ${sbn.packageName}")
        scope.launch {
            handleNotification(sbn)
        }
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

    private suspend fun handleNotification(sbn: StatusBarNotification){
        val packageName = sbn.packageName
        Log.d(TAG,"[handleNotification] IN ${sbn.packageName}")

        val handleType = getHandleType(packageName) ?: return

        val appName = getAppName(packageName)

        val extras = sbn.notification.extras
        val title = extras.getString(Notification.EXTRA_TITLE)
        val text = extras.getCharSequence(Notification.EXTRA_TEXT)

        val speakText = if (handleType == AppHandleType.NOTIFICATION_ONLY)
            "${appName}で${title}からメッセージが届きました。"
        else
            "${appName}で${title}からメッセージが届きました。$text"
        speak(speakText)
        Log.d(
            TAG, "[handleNotification] appName:$appName " +
                    "package:$packageName " +
                    "handleType:$handleType " +
                    "title:$title" +
                    "text:$text"
        )
    }
    private fun speak(text: String) {
        Log.w(TAG, "[tts] speak:$text.")
        textToSpeech?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "utteranceId")
    }

    private suspend fun getHandleType(packageName: String): AppHandleType? {
        val listAndSettings = listInfoDao.queryAllWithSettingInfo().filter {
            it.list.enabled && it.settings.any { settingInfo ->
                settingInfo.packageName == packageName
            }
        }
        if (listAndSettings.isEmpty()) return null

        val isNotOnly = listAndSettings.any {
            it.list.handleType == AppHandleType.NOTIFICATION_AND_MESSAGE
        }

        return if (isNotOnly) AppHandleType.NOTIFICATION_AND_MESSAGE
        else AppHandleType.NOTIFICATION_ONLY
    }


    private fun getAppName(packageName: String): String {
        val appInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            packageManager.getApplicationInfo(packageName, PackageManager.ApplicationInfoFlags.of(0))
        } else {
            @Suppress("DEPRECATION")
            packageManager.getApplicationInfo(packageName,0)
        }
        val appName = packageManager.getApplicationLabel(appInfo)
        return appName.toString()
    }

    private fun initDatabase() {
        this.db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "app.db"
        ).build()
        this.listInfoDao = db.listInfoDao()
        this.settingInfoDao = db.settingInfoDao()
    }
    private fun checkBluetoothDeviceConnected():Boolean{
        val audioManager = this.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val devices =  audioManager.getDevices(AudioManager.GET_DEVICES_OUTPUTS)
            .filter { it.type == AudioDeviceInfo.TYPE_BLUETOOTH_A2DP}
            .filter { it.productName.contains("NISSAN", ignoreCase = true) }
        Log.d(TAG, "devices:${devices.map { "${it.productName} type:${it.type}" }}")
        return devices.isNotEmpty()
    }
    companion object {
        const val TAG = "MyNotificationListenerService"
    }
}