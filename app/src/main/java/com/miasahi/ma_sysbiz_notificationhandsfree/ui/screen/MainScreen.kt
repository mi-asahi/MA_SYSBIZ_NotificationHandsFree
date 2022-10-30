package com.miasahi.ma_sysbiz_notificationhandsfree.ui.screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.miasahi.ma_sysbiz_notificationhandsfree.InstalledAppManager
import com.miasahi.ma_sysbiz_notificationhandsfree.database.dao.ListInfoDao
import com.miasahi.ma_sysbiz_notificationhandsfree.database.dao.SettingInfoDao
import com.miasahi.ma_sysbiz_notificationhandsfree.database.entity.ListAndSetting
import com.miasahi.ma_sysbiz_notificationhandsfree.database.entity.ListInfo
import com.miasahi.ma_sysbiz_notificationhandsfree.ui.state.rememberMainScreenState
import kotlinx.coroutines.launch

private const val TAG = "MainScreen"

@Composable
fun MainScreen(
    listInfoDao: ListInfoDao,
    settingInfoDao: SettingInfoDao,
    onShowListSetting: (ListAndSetting) -> Unit
) {
    val screenState = rememberMainScreenState(listInfoDao, settingInfoDao)
    val scope = rememberCoroutineScope()
    screenState.onInit()
    Log.d("MainScreen", "apps:${InstalledAppManager.appInfos}")
    Scaffold(
        topBar = { AppBar() },
        floatingActionButton = { FloatingAddButton(onAdd = screenState.onAddItem) }
    ) { paddingValues ->
        MainList(
            paddingValues = paddingValues,
            listItems = screenState.listInfos,
            onChange = screenState.onChange,
            onClick = {
                scope.launch {
                    val listAndSetting = listInfoDao.queryWithSettingInfo(listId = it.id)
                    Log.d(TAG, "[onClick] list:${listAndSetting?.list}")
                    if (listAndSetting != null) {
                        onShowListSetting(listAndSetting)
                    }
                }
            }
        )
    }
}

@Composable
fun MainList(
    paddingValues: PaddingValues,
    listItems: List<ListInfo>,
    onChange: (Int, Boolean) -> Unit,
    onClick: (ListInfo) -> Unit,
) {

    Box(
        modifier = Modifier
            .padding(
                start = 20.dp,
                top = 10.dp,
                end = 20.dp,
                bottom = 100.dp + paddingValues.calculateBottomPadding()
            )
            .fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier
                .clip(shape = RoundedCornerShape(4.dp))
                .background(color = Color.White)
                .fillMaxSize()
        ) {
            items(listItems.size) { index ->
                MainListRow(
                    item = listItems[index],
                    onChange = { enable -> onChange(index, enable) },
                    onClick = { onClick(listItems[index]) }
                )
                if (index != listItems.lastIndex)
                    TabRowDefaults.Divider(thickness = 1.dp, color = Color(0xFFEAEAEA))
            }
        }
    }
}

@Composable
fun MainListRow(item: ListInfo, onChange: (Boolean) -> Unit, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .height(50.dp)
            .padding(horizontal = 20.dp)
            .fillMaxSize()
            .clickable(enabled = item.enabled, onClick = onClick),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text = item.name)
        Spacer(modifier = Modifier.weight(1f))
        Switch(checked = item.enabled, onCheckedChange = onChange)
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
fun FloatingAddButton(onAdd: () -> Unit) {
    FloatingActionButton(
        onClick = onAdd,
        backgroundColor = Color(0xffF91212),
        contentColor = Color(0xffD2D2D2),
        modifier = Modifier
            .height(45.dp)
            .width(45.dp)
    ) {
        Icon(Icons.Filled.Add, contentDescription = "追加")
    }
}

//@Preview(showBackground = true)
//@Composable
//fun MainScreenPreview() {
//    WithAppTheme {
//        MainScreen(onShowListSetting = {_ ->})
//    }
//}