package com.miasahi.ma_sysbiz_notificationhandsfree.data

import androidx.room.TypeConverter

enum class AppHandleType {
    NOTIFICATION_ONLY,
    NOTIFICATION_AND_MESSAGE;

    object Converter {
        @TypeConverter
        fun toInt(appHandleType: AppHandleType): Int = appHandleType.ordinal

        @TypeConverter
        fun fromInt(int: Int): AppHandleType = values()[int]
    }
}

fun AppHandleType.text() = if (this == AppHandleType.NOTIFICATION_ONLY){
    "アプリ通知のみ"
}else {
    "アプリ通知＋メッセージ読み上げ"
}