package com.miasahi.ma_sysbiz_notificationhandsfree.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.miasahi.ma_sysbiz_notificationhandsfree.database.entity.ListAndSetting
import com.miasahi.ma_sysbiz_notificationhandsfree.database.entity.ListInfo

@Dao
interface ListInfoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(listInfo: ListInfo)

    @Update
    suspend fun update(listInfo: ListInfo)

    @Delete
    suspend fun delete(listInfo: ListInfo)

    @Query("select * from list_info")
    suspend fun queryAll(): List<ListInfo>

    @Transaction
    @Query("select * from list_info")
    suspend fun queryAllWithSettingInfo(): List<ListAndSetting>

    @Transaction
    @Query("select * from list_info where id = :listId")
    suspend fun queryWithSettingInfo(listId: Long): ListAndSetting?


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllListInfo(vararg listInfo: ListInfo)

}