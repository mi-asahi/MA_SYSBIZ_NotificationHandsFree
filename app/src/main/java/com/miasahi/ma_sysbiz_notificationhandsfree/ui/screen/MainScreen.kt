package com.miasahi.ma_sysbiz_notificationhandsfree.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.miasahi.ma_sysbiz_notificationhandsfree.data.MainListItem
import com.miasahi.ma_sysbiz_notificationhandsfree.data.sampleMainListData
import com.miasahi.ma_sysbiz_notificationhandsfree.screen.WithAppTheme



@Composable
fun MainScreen() {
    Scaffold(
        topBar = { AppBar() },
        floatingActionButton = { FloatingAddButton() }
    ) {
        MainList(listItems = sampleMainListData)
    }
}

@Composable
fun MainList(listItems: List<MainListItem>) {
    Box(
        modifier = Modifier
            .padding(20.dp)
            .fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier
                .clip(shape = RoundedCornerShape(4.dp))
                .background(color = Color.White)
                .defaultMinSize(minHeight = 200.dp)
                .fillMaxWidth()
        ) {
            items(listItems.size) { index ->
                MainListRow(item = listItems[index])
                if (index != listItems.lastIndex)
                    TabRowDefaults.Divider(thickness = 1.dp, color = Color(0xFFEAEAEA))
            }
        }
    }
}

@Composable
fun MainListRow(item: MainListItem) {
    Row(
        modifier = Modifier
            .height(50.dp)
            .padding(horizontal = 20.dp)
            .fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text = item.name)
        Spacer(modifier = Modifier.weight(1f))
        Switch(checked = item.enabled, onCheckedChange = { _ -> })
    }
}

@Composable
fun AppBar() {
    TopAppBar(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = MaterialTheme.colors.background,
        elevation = 0.dp
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "音声読み上げ", textAlign = TextAlign.Center,
            style = TextStyle(color = Color.Black, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        )
    }
}

@Composable
fun FloatingAddButton() {
    FloatingActionButton(onClick = { /*do something*/ }) {
        Icon(Icons.Filled.Add, contentDescription = "追加")
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    WithAppTheme {
        MainScreen()
    }
}