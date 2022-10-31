package com.miasahi.ma_sysbiz_notificationhandsfree

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.NotificationManagerCompat
import androidx.room.Room

import com.miasahi.ma_sysbiz_notificationhandsfree.database.AppDatabase
import com.miasahi.ma_sysbiz_notificationhandsfree.database.dao.ListInfoDao
import com.miasahi.ma_sysbiz_notificationhandsfree.database.dao.SettingInfoDao
import com.miasahi.ma_sysbiz_notificationhandsfree.ui.screen.App
import com.miasahi.ma_sysbiz_notificationhandsfree.ui.theme.AppTheme

class MainActivity : ComponentActivity() {
    private lateinit var db: AppDatabase
    private lateinit var listInfoDao: ListInfoDao
    private lateinit var settingInfoDao: SettingInfoDao
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initDatabase()
        InstalledAppManager.initialize(this)
        setContent {
            App(
                listInfoDao = listInfoDao,
                settingInfoDao = settingInfoDao
            )
        }
        checkPermission()
    }

    override fun onDestroy() {
        db.close()
        super.onDestroy()
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

    private fun checkPermission() {
        val sets = NotificationManagerCompat.getEnabledListenerPackages(this)
        if (!sets.contains(packageName)) {
            startActivity(Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"))
        }
    }
}
//
//@Composable
//fun Greeting(name: String) {
//    Text(text = "Hello $name!")
//}
//
//@Composable
//fun SettingList(listItems: List<MainListItem>) {
//    LazyColumn(modifier = Modifier
//        .height(500.dp)
//        .clip(shape = RoundedCornerShape(4.dp))
//
//        .padding(5.dp)
//    ) {
//        items(listItems.size) { index ->
//            SettingListRow(item = listItems[index])
//            Divider( thickness = 1.dp, color = Color(0xFFEAEAEA))
//        }
//    }
//}
//
//@Composable
//fun SettingListRow(item: MainListItem) {
//    Row(
//        modifier = Modifier
//            .height(40.dp)
//            .padding(10.dp)
//            .fillMaxSize(),
//        verticalAlignment = Alignment.CenterVertically,
//    ) {
//        Text(text = item.name)
//        Spacer(modifier = Modifier.weight(1f))
//        Switch(checked = item.enabled, onCheckedChange = { _ -> })
//    }
//}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AppTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xffeaeaea)),
            color = MaterialTheme.colors.background
        ) {
//                    Greeting("Android")
//            SettingList(sampleMainListData)
        }
    }
}
