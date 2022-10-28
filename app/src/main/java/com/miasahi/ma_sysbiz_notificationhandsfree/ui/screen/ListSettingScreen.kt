package com.miasahi.ma_sysbiz_notificationhandsfree.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.miasahi.ma_sysbiz_notificationhandsfree.data.*
import com.miasahi.ma_sysbiz_notificationhandsfree.screen.WithAppTheme
import com.miasahi.ma_sysbiz_notificationhandsfree.ui.state.rememberListSettingScreenState


@Composable
fun ListSettingScreen(mainListItem: MainListItem, onSave: (MainListItem) -> Unit) {
    val screenState = rememberListSettingScreenState(initValue = mainListItem)
    Box(
        modifier = Modifier
            .fillMaxHeight(fraction = 0.95f)
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp))
            .background(Color.White), contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            TitleRow(onSave = { onSave(screenState.mainListItem) })
            NameRow(name = screenState.mainListItem.name, onEdit = screenState.changeName)
            HandleTypeDropDown(
                handleType = screenState.mainListItem.handleType,
                onChanged = screenState.changeHandleType,
            )
            Spacer(modifier = Modifier.height(50.dp))
            appSettingList(
                listItems = screenState.mainListItem.appItems,
                onEdit = screenState.changeAppSetting,
            )
        }
    }
}


@Composable
fun TitleRow(onSave: () -> Unit) {
    Row(
        modifier = Modifier.padding(horizontal = 5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "リストタイトル")
        Spacer(modifier = Modifier.weight(1f))
        Button(
            modifier = Modifier
                .padding(1.dp),
            //border = BorderStroke(1.dp, Color.Gray),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = Color.Black,
                backgroundColor = Color.White
            ),
            onClick = onSave
        ) {
            Text("保存")
        }
    }
}

@Composable
fun NameRow(name: String, onEdit: (String) -> Unit) {
    Row(
        modifier = Modifier.padding(horizontal = 5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            modifier = Modifier.width(302.dp),
            value = name,
            maxLines = 1,
            singleLine = true,
            textStyle = TextStyle(
                textAlign = TextAlign.Start,
                fontSize = 14.sp,
            ),
            placeholder = { Text("リスト名を入力してください") },
            onValueChange = { value -> onEdit(value) }
        )
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
fun HandleTypeRow(text: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier.padding(horizontal = 5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            modifier = Modifier
                .width(302.dp),
            border = BorderStroke(1.dp, Color.Gray),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = Color.Black,
                backgroundColor = Color.White
            ),
            onClick = onClick
        ) {
            appHandleTypeSettingRow(text)
        }
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
fun HandleTypeDropDown(handleType: AppHandleType, onChanged: (AppHandleType) -> Unit) {
    var isShown = remember {
        mutableStateOf(false)
    }
    Box(
        modifier = Modifier
            .wrapContentSize(Alignment.TopStart)
    ) {
        HandleTypeRow(text = handleType.text()) {
            isShown.value = true
        }
        DropdownMenu(
            expanded = isShown.value,
            onDismissRequest = {
                isShown.value = false
            },
            offset = DpOffset(x = 5.dp, y = 0.dp),
            modifier = Modifier
                .wrapContentSize()
                .background(Color.White),
        ) {
            AppHandleType.values()
                .filter { it != handleType }
                .forEach { type ->
                    DropdownMenuItem(
                        onClick = {
                            onChanged(type)
                            isShown.value = false
                        },
                        modifier = Modifier.width(302.dp),
                    ) {
                        appHandleTypeSettingRow(type.text())
                    }
                }

        }
    }
}

@Composable
fun appHandleTypeSettingRow(text: String) {
    Row {
        Text(
            text = text,
            textAlign = TextAlign.Start,
            fontSize = 14.sp,
        )
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )
    }
}

@Composable
fun appSettingList(listItems: List<AppSettingItem>, onEdit: (Int, Boolean) -> Unit) {
    LazyColumn() {
        items(listItems.size) { index ->
            TabRowDefaults.Divider(thickness = 1.dp, color = Color(0xFFEAEAEA))
            appSettingListItem(listItems[index], onEdit = { value -> onEdit(index, value) })
        }
    }
}

@Composable
fun appSettingListItem(listItem: AppSettingItem, onEdit: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .height(50.dp)
            .padding(vertical = 8.dp, horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Image(imageVector = Icons.Filled.Settings, contentDescription = "")
        Spacer(modifier = Modifier.width(18.dp))
        Text(
            text = listItem.packageName,
            fontSize = 14.sp,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )

        Switch(checked = listItem.isOn, onCheckedChange = onEdit)

    }
}

@Composable
fun TempBody(onClick: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Button(onClick = onClick) {
            Text(text = "showSheet")
        }
    }

}

@Preview(showBackground = true)
@Composable
fun ListSettingScreenPreview() {
    WithAppTheme {
        ListSettingScreen(
            mainListItem = sampleMainListData.first(),
            onSave = { _ -> },
        )
    }
}