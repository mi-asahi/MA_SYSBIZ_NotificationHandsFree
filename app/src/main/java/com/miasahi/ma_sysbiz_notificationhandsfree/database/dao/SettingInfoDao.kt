package com.miasahi.ma_sysbiz_notificationhandsfree.database.dao

import androidx.room.*
import com.miasahi.ma_sysbiz_notificationhandsfree.database.entity.SettingInfo

@Dao
interface SettingInfoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(settingInfo: SettingInfo)

    @Update
    suspend fun update(settingInfo: SettingInfo)

    @Delete
    suspend fun delete(settingInfo: SettingInfo)

    @Query("DELETE FROM setting_info WHERE list_id = :listId")
    suspend fun delete(listId: Long)

//    @Query("select * from setting_info")
//    suspend fun queryAll(): List<SettingInfo>
//
//    @Query("select * from setting_info where list_id = :listId")
//    suspend fun query(listId: Long): List<SettingInfo>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(settingInfo: List<SettingInfo>)
}