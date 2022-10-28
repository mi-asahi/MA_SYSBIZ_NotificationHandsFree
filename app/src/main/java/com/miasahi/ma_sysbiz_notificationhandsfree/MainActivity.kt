package com.miasahi.ma_sysbiz_notificationhandsfree

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Switch
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.miasahi.ma_sysbiz_notificationhandsfree.data.MainListItem
import com.miasahi.ma_sysbiz_notificationhandsfree.data.sampleMainListData
import com.miasahi.ma_sysbiz_notificationhandsfree.ui.theme.AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
//                    Greeting("Android")
                    SettingList(sampleMainListData)
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Composable
fun SettingList(listItems: List<MainListItem>) {
    LazyColumn(modifier = Modifier
        .height(500.dp)
        .clip(shape = RoundedCornerShape(4.dp))

        .padding(5.dp)
    ) {
        items(listItems.size) { index ->
            SettingListRow(item = listItems[index])
            Divider( thickness = 1.dp, color = Color(0xFFEAEAEA))
        }
    }
}

@Composable
fun SettingListRow(item: MainListItem) {
    Row(
        modifier = Modifier
            .height(40.dp)
            .padding(10.dp)
            .fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text = item.name)
        Spacer(modifier = Modifier.weight(1f))
        Switch(checked = item.enabled, onCheckedChange = { _ -> })
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AppTheme {
        Surface(
            modifier = Modifier.fillMaxSize().background(Color(0xffeaeaea)),
            color = MaterialTheme.colors.background
        ) {
//                    Greeting("Android")
            SettingList(sampleMainListData)
        }
    }
}
