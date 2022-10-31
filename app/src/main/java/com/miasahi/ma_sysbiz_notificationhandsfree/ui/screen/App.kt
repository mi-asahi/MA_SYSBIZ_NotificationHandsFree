package com.miasahi.ma_sysbiz_notificationhandsfree.ui.screen

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.miasahi.ma_sysbiz_notificationhandsfree.data.AppHandleType
import com.miasahi.ma_sysbiz_notificationhandsfree.database.dao.ListInfoDao
import com.miasahi.ma_sysbiz_notificationhandsfree.database.dao.SettingInfoDao
import com.miasahi.ma_sysbiz_notificationhandsfree.database.entity.ListAndSetting
import com.miasahi.ma_sysbiz_notificationhandsfree.database.entity.ListInfo
import com.miasahi.ma_sysbiz_notificationhandsfree.ui.theme.AppTheme
import kotlinx.coroutines.launch

private const val TAG = "App"

@Composable
fun App(listInfoDao: ListInfoDao, settingInfoDao: SettingInfoDao) {
    val listAndSetting = remember {
        mutableStateOf<ListAndSetting?>(null)
    }
    val scope = rememberCoroutineScope()
    WithAppTheme {
        WithScaffold(
            listAndSetting = listAndSetting.value,
            sheetContent = { data, onDismiss ->
                Log.d(TAG, "[SheetContent] list:${data.list}")
                ListSettingScreen(listAndSetting = data, onSave = { listInfo, settingInfos ->
                    scope.launch {
                        listInfoDao.insert(listInfo = listInfo)
                        settingInfoDao.delete(listId = listInfo.id)
                        settingInfoDao.insertAll(settingInfos)
                        Log.d(TAG, "[onSave] saved")
                        onDismiss()
                    }
                })
            },
            body = { onShowBottomSheet ->
                MainScreen(
                    listInfoDao = listInfoDao,
                    settingInfoDao = settingInfoDao,
                    onShowListSetting = {
                        listAndSetting.value = it
                        Log.d("App", "${listAndSetting.value}")
                        onShowBottomSheet(true)
                    })
            },
            onDismiss = { listAndSetting.value = null }
        )
    }
}

@Composable
fun WithAppTheme(content: @Composable () -> Unit) {
    AppTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background,
            content = content
        )
    }
}

@Suppress("OPT_IN_IS_NOT_ENABLED")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun WithScaffold(
    listAndSetting: ListAndSetting?,
    sheetContent: @Composable (ListAndSetting, onDismiss: () -> Unit) -> Unit,
    body: @Composable (onShowBottomSheet: (Boolean) -> Unit) -> Unit,
    onDismiss: () -> Unit
) {
    Log.d(TAG, "[Scaffold] list:${listAndSetting?.list}")
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberBottomSheetState(
            initialValue = BottomSheetValue.Collapsed,
            confirmStateChange = {
                if (it == BottomSheetValue.Collapsed) {
                    onDismiss()
                }
                true
            }
        ),
    )
    val defaultListAndSetting = ListAndSetting(
        list = ListInfo(
            id = -1,
            name = "",
            enabled = false,
            handleType = AppHandleType.NOTIFICATION_ONLY
        ), settings = listOf()
    )
    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = 0.dp,
        sheetContent = {
            sheetContent(listAndSetting ?: defaultListAndSetting) {
                scope.launch {
                    scaffoldState.bottomSheetState.collapse()
                }
            }
        },
        content = {
            body { goExpand ->
                Log.d("App", "goExpand:$goExpand")
                scope.launch {
                    if (!goExpand) scaffoldState.bottomSheetState.collapse()
                    else scaffoldState.bottomSheetState.expand()
                }
            }
        }
    )
//    {
//        TempBody {
//            scope.launch {
//                if (scaffoldState.bottomSheetState.isCollapsed) {
//                    scaffoldState.bottomSheetState.expand()
//                } else {
//                    scaffoldState.bottomSheetState.collapse()
//                }
//            }
//        }
//    }
}

//@Preview(showBackground = true)
//@Composable
//fun DefaultPreview() {
//    App()
//}
