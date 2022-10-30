package com.miasahi.ma_sysbiz_notificationhandsfree.ui.state

import android.util.Log
import androidx.compose.runtime.*
import com.miasahi.ma_sysbiz_notificationhandsfree.InstalledAppManager
import com.miasahi.ma_sysbiz_notificationhandsfree.data.AppHandleType
import com.miasahi.ma_sysbiz_notificationhandsfree.database.dao.ListInfoDao
import com.miasahi.ma_sysbiz_notificationhandsfree.database.dao.SettingInfoDao
import com.miasahi.ma_sysbiz_notificationhandsfree.database.entity.ListInfo
import com.miasahi.ma_sysbiz_notificationhandsfree.database.entity.SettingInfo

import kotlinx.coroutines.launch

private const val TAG = "MainScreenState"

data class MainScreenState(
    val listInfos: List<ListInfo>,
    val onInit: () -> Unit,
    val onChange: (Int, Boolean) -> Unit,
    val onAddItem: () -> Unit
)

@Composable
fun rememberMainScreenState(
    listInfoDao: ListInfoDao,
    settingInfoDao: SettingInfoDao
): MainScreenState {
    var listInfos by remember { mutableStateOf(listOf<ListInfo>()) }
    val scope = rememberCoroutineScope()

    val reload = {
        scope.launch {
            listInfos = listInfoDao.queryAll()
            if (listInfos.isEmpty()) {
                val defaultListInfo = ListInfo(
                    name = "デフォルト",
                    enabled = true,
                    handleType = AppHandleType.NOTIFICATION_ONLY,
                )
                listInfoDao.insert(defaultListInfo)
                Log.d(TAG, "insert default ListInfo")
                listInfos = listInfoDao.queryAll()
                val listInfo = listInfos.findLast { it.name == defaultListInfo.name }
                if (listInfo != null) {
                    val allApps = InstalledAppManager.appInfos.map {
                        SettingInfo(
                            listId = listInfo.id,
                            packageName = it.packageName
                        )
                    }.toList()
                    settingInfoDao.insertAll(allApps)
                    Log.d(TAG, "insert SettingInfos for default ListInfo")
                }

            }
        }
    }
    return remember(listInfos) {
        MainScreenState(
            listInfos = listInfos,
            onInit = {
                reload()
            },
            onChange = { index, enabled ->
                Log.d(TAG,"[onChange] index:$index enabled:$enabled")
                val changedListInfo = listInfos[index].copy(enabled = enabled)
                scope.launch {
                    listInfoDao.update(changedListInfo)
                    reload()
                }
            },
            onAddItem = {
                Log.d(TAG,"[onAddItem]")
                val index = listInfos.size
                scope.launch {
                    listInfoDao.insert(
                        ListInfo(
                            name = "リスト$index",
                            enabled = true,
                            handleType = AppHandleType.NOTIFICATION_ONLY
                        )
                    )
                    reload()
                }
            }
        )
    }
}