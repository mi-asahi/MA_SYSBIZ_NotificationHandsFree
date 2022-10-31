package com.miasahi.ma_sysbiz_notificationhandsfree.ui.theme

import androidx.compose.material.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Purple700 = Color(0xFF3700B3)
val Teal200 = Color(0xFF03DAC5)
val Gray = Color(0xFFC4C4C4)

@Composable
fun defaultSwitchColors() = SwitchDefaults.colors(
    checkedThumbColor = Color.White,
    uncheckedThumbColor = Color.White,
    checkedTrackColor = Color(0xFF027715),
    uncheckedTrackColor = Color(0xffC4C4C4),
)