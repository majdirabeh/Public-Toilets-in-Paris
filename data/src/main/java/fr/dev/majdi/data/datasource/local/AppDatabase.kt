package fr.dev.majdi.data.datasource.local

import androidx.room.Database
import androidx.room.RoomDatabase
import fr.dev.majdi.data.datasource.local.dao.ToiletDao
import fr.dev.majdi.data.datasource.local.entity.ToiletEntity

/**
 * Created by Majdi RABEH on 23/11/2023.
 * Email m.rabeh.majdi@gmail.com
 */
@Database(entities = [ToiletEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract val toiletDao: ToiletDao

    companion object {
        const val DATA_BASE_NAME = "ParisToilets"
    }

}