package com.miasahi.ma_sysbiz_notificationhandsfree.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "setting_info")
data class SettingInfo(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    @ColumnInfo(name = "list_id")
    val listId: Long,
    @ColumnInfo(name = "package_name")
    val packageName: String,
)
