package fr.dev.majdi.data.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import fr.dev.majdi.data.datasource.local.AppDatabase
import fr.dev.majdi.data.datasource.local.dao.ToiletDao
import javax.inject.Singleton

/**
 * Created by Majdi RABEH on 23/11/2023.
 * Email m.rabeh.majdi@gmail.com
 */
@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Provides
    @Singleton
    internal fun provideAppDatabase(application: Application): AppDatabase {
        return Room.databaseBuilder(
            application, AppDatabase::class.java, AppDatabase.DATA_BASE_NAME
        ).allowMainThreadQueries().build()
    }

    @Provides
    internal fun provideToiletDao(appDatabase: AppDatabase): ToiletDao {
        return appDatabase.toiletDao
    }

}