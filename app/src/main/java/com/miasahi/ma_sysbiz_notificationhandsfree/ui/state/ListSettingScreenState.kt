package com.miasahi.ma_sysbiz_notificationhandsfree.ui.state

import androidx.compose.runtime.*
import com.miasahi.ma_sysbiz_notificationhandsfree.data.AppHandleType
import com.miasahi.ma_sysbiz_notificationhandsfree.data.MainListItem

data class ListSettingScreenState(
    val mainListItem: MainListItem,
    val changeName: (String) -> Unit,
    val changeHandleType: (AppHandleType) -> Unit,
    val changeAppSetting: (Int, Boolean) -> Unit
)

@Composable
fun rememberListSettingScreenState(initValue: MainListItem): ListSettingScreenState {
    var mainListItem by remember { mutableStateOf(initValue) }
    return remember(mainListItem) {
        ListSettingScreenState(
            mainListItem = mainListItem,
            changeName = { name -> mainListItem = mainListItem.copy(name = name) },
            changeHandleType = { type -> mainListItem = mainListItem.copy(handleType = type) },
            changeAppSetting = { index, isOn ->
                var appItems = mainListItem.appItems.mapIndexed { i, item ->
                    if (i == index) item.copy(isOn = isOn)
                    else item
                }

                mainListItem = mainListItem.copy(appItems = appItems)
            }
        )
    }
}