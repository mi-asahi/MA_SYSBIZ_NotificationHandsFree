package com.miasahi.ma_sysbiz_notificationhandsfree.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.miasahi.ma_sysbiz_notificationhandsfree.data.AppHandleType

@Entity(tableName = "list_info")
data class ListInfo(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,

    val name: String,
    val enabled: Boolean,
    @TypeConverters(AppHandleType.Converter::class)
    @ColumnInfo(name = "handle_type")
    val handleType: AppHandleType,
)
