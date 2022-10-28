package com.miasahi.ma_sysbiz_notificationhandsfree.data

enum class AppHandleType {
    NOTIFICATION_ONLY,
    NOTIFICATION_AND_MESSAGE
}

fun AppHandleType.text() = if (this == AppHandleType.NOTIFICATION_ONLY){
    "アプリ通知のみ"
}else {
    "アプリ通知＋メッセージ読み上げ"
}