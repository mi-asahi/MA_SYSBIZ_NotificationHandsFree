package com.miasahi.ma_sysbiz_notificationhandsfree.ui.screen

import android.graphics.drawable.Drawable
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import com.miasahi.ma_sysbiz_notificationhandsfree.InstalledAppManager
import com.miasahi.ma_sysbiz_notificationhandsfree.data.*
import com.miasahi.ma_sysbiz_notificationhandsfree.database.entity.ListAndSetting
import com.miasahi.ma_sysbiz_notificationhandsfree.database.entity.ListInfo
import com.miasahi.ma_sysbiz_notificationhandsfree.database.entity.SettingInfo
import com.miasahi.ma_sysbiz_notificationhandsfree.ui.state.rememberListSettingScreenState
import com.miasahi.ma_sysbiz_notificationhandsfree.ui.theme.defaultSwitchColors

private const val TAG = "ListSettingScreen"

@Composable
fun ListSettingScreen(
    listAndSetting: ListAndSetting,
    onSave: (ListInfo, List<SettingInfo>) -> Unit
) {
    Log.d(TAG, "[Show] list:${listAndSetting.list}")
    val screenState = rememberListSettingScreenState(initValue = listAndSetting)
    val localFocusManager = LocalFocusManager.current
    Box(
        modifier = Modifier
            .fillMaxHeight(fraction = 0.95f)
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp))
            .background(MaterialTheme.colors.surface)
            .pointerInput(1) {
                detectTapGestures(onTap = {
                    localFocusManager.clearFocus()
                })
            },
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            TitleRow(onSave = {
                localFocusManager.clearFocus()
                onSave(screenState.listInfo, screenState.settingInfos)
            })
            NameRow(name = screenState.listInfo.name, onEdit = screenState.onChangeName)
            HandleTypeDropDown(
                handleType = screenState.listInfo.handleType,
                onChanged = screenState.onChangeHandleType,
            )
            Spacer(modifier = Modifier.height(50.dp))
            AppSettingList(
                enabledPackages = screenState.settingInfos.map { it.packageName }.toList(),
                onEdit = screenState.onChangeAppSetting,
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
        Text(text = "リストタイトル", color = MaterialTheme.colors.primary)
        Spacer(modifier = Modifier.weight(1f))
        Button(
            modifier = Modifier
                .padding(1.dp),
            //border = BorderStroke(1.dp, Color.Gray),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colors.primary,
                backgroundColor = MaterialTheme.colors.surface
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
            onValueChange = { value -> onEdit(value) },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                textColor = MaterialTheme.colors.primary,
            )
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
            border = BorderStroke(
                1.dp,
                MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.disabled)
            ),
            colors = ButtonDefaults.outlinedButtonColors(
                backgroundColor = MaterialTheme.colors.surface
            ),
            onClick = onClick
        ) {
            AppHandleTypeSettingRow(text)
        }
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
fun HandleTypeDropDown(handleType: AppHandleType, onChanged: (AppHandleType) -> Unit) {
    val isShown = remember {
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
                .background(MaterialTheme.colors.surface),
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
                        AppHandleTypeSettingRow(type.text())
                    }
                }

        }
    }
}

@Composable
fun AppHandleTypeSettingRow(text: String) {
    Row {
        Text(
            text = text,
            textAlign = TextAlign.Start,
            fontSize = 14.sp,
            color = MaterialTheme.colors.primary,
        )
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )
    }
}

@Composable
fun AppSettingList(enabledPackages: List<String>, onEdit: (String, Boolean) -> Unit) {
    val apps = InstalledAppManager.appInfos
    LazyColumn() {
        items(apps.size) { index ->
            TabRowDefaults.Divider(thickness = 1.dp, color = Color(0xFFEAEAEA))
            AppSettingListItem(
                appName = apps[index].displayName,
                icon = apps[index].icon,
                packageName = apps[index].packageName,
                enable = enabledPackages.contains(apps[index].packageName),
                onEdit = onEdit
            )
        }
    }
}

@Composable
fun AppSettingListItem(
    appName: String,
    icon: Drawable,
    packageName: String,
    enable: Boolean,
    onEdit: (String, Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .height(50.dp)
            .padding(vertical = 8.dp, horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(bitmap = icon.toBitmap().asImageBitmap(), contentDescription = "")
        Spacer(modifier = Modifier.width(18.dp))
        Text(
            text = appName,
            fontSize = 14.sp,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            color = MaterialTheme.colors.primary,
        )

        Switch(
            checked = enable, onCheckedChange = { value ->
                onEdit(packageName, value)
            },
            colors = defaultSwitchColors()
        )

    }
}
//
//@Composable
//fun TempBody(onClick: () -> Unit) {
//    Box(
//        modifier = Modifier.fillMaxSize(),
//        contentAlignment = Alignment.Center
//    ) {
//        Button(onClick = onClick) {
//            Text(text = "showSheet")
//        }
//    }
//
//}

//@Preview(showBackground = true)
//@Composable
//fun ListSettingScreenPreview() {
//    WithAppTheme {
//        ListSettingScreen(
//            mainListItem = sampleMainListData.first(),
//            onSave = { _ -> },
//        )
//    }
//}