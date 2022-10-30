package com.miasahi.ma_sysbiz_notificationhandsfree.database.entity

import androidx.room.Embedded
import androidx.room.Relation

data class ListAndSetting(
    @Embedded val list: ListInfo,
    @Relation(
        parentColumn = "id",
        entityColumn = "list_id"
    )
    val settings: List<SettingInfo>
)
