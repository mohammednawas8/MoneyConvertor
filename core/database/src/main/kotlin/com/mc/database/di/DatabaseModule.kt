package com.mc.database.di

import android.app.Application
import androidx.room.Room
import com.mc.database.db.CurrencyDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideCurrencyDatabase(application: Application): CurrencyDatabase {
        return Room.databaseBuilder(
            application,
            CurrencyDatabase::class.java,
            CurrencyDatabase.NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }

}