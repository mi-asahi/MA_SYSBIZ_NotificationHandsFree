package com.miasahi.ma_sysbiz_notificationhandsfree.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.miasahi.ma_sysbiz_notificationhandsfree.database.dao.ListInfoDao
import com.miasahi.ma_sysbiz_notificationhandsfree.database.dao.SettingInfoDao
import com.miasahi.ma_sysbiz_notificationhandsfree.database.entity.ListInfo
import com.miasahi.ma_sysbiz_notificationhandsfree.database.entity.SettingInfo

@Database(entities = [ListInfo::class,SettingInfo::class], version = 1)
abstract class AppDatabase :RoomDatabase(){
    abstract fun listInfoDao(): ListInfoDao
    abstract fun settingInfoDao(): SettingInfoDao
}