package com.miasahi.ma_sysbiz_notificationhandsfree.ui.state

import android.util.Log
import androidx.compose.runtime.*
import com.miasahi.ma_sysbiz_notificationhandsfree.data.AppHandleType
import com.miasahi.ma_sysbiz_notificationhandsfree.database.entity.ListAndSetting
import com.miasahi.ma_sysbiz_notificationhandsfree.database.entity.ListInfo
import com.miasahi.ma_sysbiz_notificationhandsfree.database.entity.SettingInfo

private const val TAG = "ListSettingScreenState"

data class ListSettingScreenState(
    val listInfo: ListInfo,
    val settingInfos: List<SettingInfo>,

    val onChangeName: (String) -> Unit,
    val onChangeHandleType: (AppHandleType) -> Unit,
    val onChangeAppSetting: (String, Boolean) -> Unit
)

@Composable
fun rememberListSettingScreenState(
    initValue: ListAndSetting,
): ListSettingScreenState {
    Log.d(TAG,"test")

    var listInfo by remember { mutableStateOf(initValue.list) }
    var settingInfos by remember { mutableStateOf(initValue.settings) }
    if(listInfo.id != initValue.list.id){
        listInfo = initValue.list
        settingInfos = initValue.settings
    }
    return remember(listInfo, settingInfos) {
        ListSettingScreenState(
            listInfo = listInfo,
            settingInfos = settingInfos,

            onChangeName = { name ->
                Log.d(TAG,"[onChangeName] name:$name")
                listInfo = listInfo.copy(name = name)
            },
            onChangeHandleType = { type ->
                Log.d(TAG,"[onChangeHandleType] type:$type")
                listInfo = listInfo.copy(handleType = type)
            },
            onChangeAppSetting = { packageName, enabled ->
                Log.d(TAG,"[onChangeAppSetting] enable:$enabled package:$packageName")
                val isNotExist = settingInfos.none { it.packageName == packageName }
                Log.d(TAG, "[onChangeAppSetting] isNotExist:$isNotExist")
                val orgList = settingInfos.toMutableList()
                if (enabled && isNotExist) {
                    orgList.add(SettingInfo(listId = listInfo.id, packageName = packageName))
                    settingInfos = orgList.toList()
                } else if (!enabled && !isNotExist) {
                    orgList.removeIf { it.packageName == packageName }
                    settingInfos = orgList.toList()
                    Log.d(TAG, "[onChangeAppSetting] $settingInfos")
                }
            }
        )
    }
}