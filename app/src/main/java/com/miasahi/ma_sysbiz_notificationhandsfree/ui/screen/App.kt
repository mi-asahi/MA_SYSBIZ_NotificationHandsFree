package com.miasahi.ma_sysbiz_notificationhandsfree.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.miasahi.ma_sysbiz_notificationhandsfree.ui.screen.BottomSheet
import com.miasahi.ma_sysbiz_notificationhandsfree.ui.screen.ListSettingScreen
import com.miasahi.ma_sysbiz_notificationhandsfree.ui.screen.MainScreen
import com.miasahi.ma_sysbiz_notificationhandsfree.ui.screen.TempBody
import com.miasahi.ma_sysbiz_notificationhandsfree.ui.theme.AppTheme
import kotlinx.coroutines.launch

@Composable
fun App() {
    WithAppTheme {
        WithScaffold(
            sheetContent = {
                //ListSettingScreen()
            },
            body = { onShowBottomSheet ->

            }
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun WithScaffold(
    sheetContent: @Composable () -> Unit,
    body: @Composable (onShowBottomSheet: (Boolean) -> Unit) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberBottomSheetScaffoldState()
    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = 0.dp,
        sheetContent = { sheetContent },
        content = {
            body { isExpanded ->
                scope.launch {
                    if (isExpanded) scaffoldState.bottomSheetState.collapse()
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

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    App()
}
