package fr.dev.majdi.data.datasource.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import fr.dev.majdi.data.datasource.local.entity.ToiletEntity

/**
 * Created by Majdi RABEH on 23/11/2023.
 * Email m.rabeh.majdi@gmail.com
 */
@Dao
interface ToiletDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(toiletEntity: ToiletEntity): Long

    @Query("SELECT * FROM Toilet")
    fun loadAll(): MutableList<ToiletEntity>

    @Delete
    fun delete(toiletEntity: ToiletEntity)

    @Query("DELETE FROM Toilet")
    fun deleteAll()

    @Query("SELECT * FROM Toilet where recordId = :toiletId")
    fun loadOneByToiletId(toiletId: Long): ToiletEntity?

    @Update
    fun update(toiletEntity: ToiletEntity)

}