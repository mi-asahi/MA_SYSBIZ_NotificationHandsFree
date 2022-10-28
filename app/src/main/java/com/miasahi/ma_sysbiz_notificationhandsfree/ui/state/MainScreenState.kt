package com.miasahi.ma_sysbiz_notificationhandsfree.ui.state

import androidx.compose.runtime.*
import com.miasahi.ma_sysbiz_notificationhandsfree.data.MainListItem
import com.miasahi.ma_sysbiz_notificationhandsfree.ui.screen.ListSettingScreenState

data class MainScreenState(
    val mainListItems: List<MainListItem>,
    val onChange: (Boolean)->Unit
)
//
//@Composable
//fun rememberMainScreenState(): ListSettingScreenState {
//    var mainListItem by remember { mutableStateOf(initValue) }
//    return remember(mainListItem) {
//        ListSettingScreenState(
//            mainListItem = mainListItem,
//            changeName = { name -> mainListItem = mainListItem.copy(name = name) },
//            changeHandleType = { type -> mainListItem = mainListItem.copy(handleType = type) },
//            changeAppSetting = { index, isOn ->
//                var appItems = mainListItem.appItems.mapIndexed { i, item ->
//                    if (i == index) item.copy(isOn = isOn)
//                    else item
//                }
//
//                mainListItem = mainListItem.copy(appItems = appItems)
//            }
//        )
//    }
//}