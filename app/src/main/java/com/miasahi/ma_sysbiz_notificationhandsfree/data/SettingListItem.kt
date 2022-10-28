package com.miasahi.ma_sysbiz_notificationhandsfree.data


data class MainListItem(val name:String, val enabled: Boolean, val handleType:AppHandleType, val appItems:List<AppSettingItem>)

data class AppSettingItem(val packageName:String, val isOn:Boolean)

val sampleMainListData = listOf<MainListItem>(
    MainListItem("test2", true, AppHandleType.NOTIFICATION_AND_MESSAGE, listOf(
        AppSettingItem(packageName = "com.Slack", isOn = true)
    )),
    MainListItem("test3", false, AppHandleType.NOTIFICATION_AND_MESSAGE, listOf(
        AppSettingItem(packageName = "com.Slack", isOn = false)
    )),
    MainListItem("test4", false, AppHandleType.NOTIFICATION_ONLY, listOf(
        AppSettingItem(packageName = "com.Slack", isOn = true)
    )),
    MainListItem("test5", false, AppHandleType.NOTIFICATION_ONLY, listOf(
        AppSettingItem(packageName = "com.Slack", isOn = true)
    )),
    MainListItem("test6", false, AppHandleType.NOTIFICATION_ONLY, listOf(
        AppSettingItem(packageName = "com.Slack", isOn = true)
    )),
    MainListItem("test7", false, AppHandleType.NOTIFICATION_ONLY, listOf(
        AppSettingItem(packageName = "com.Slack", isOn = true)
    )),
    MainListItem("test8", false, AppHandleType.NOTIFICATION_ONLY, listOf(
        AppSettingItem(packageName = "com.Slack", isOn = true)
    )),
    MainListItem("test9", false, AppHandleType.NOTIFICATION_ONLY, listOf(
        AppSettingItem(packageName = "com.Slack", isOn = true)
    )),
    MainListItem("test10", false, AppHandleType.NOTIFICATION_ONLY, listOf(
        AppSettingItem(packageName = "com.Slack", isOn = true)
    )),
    MainListItem("test11", false, AppHandleType.NOTIFICATION_ONLY, listOf(
        AppSettingItem(packageName = "com.Slack", isOn = true)
    )),
)