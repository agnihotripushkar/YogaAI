package me.pushkaragnihotri.yogaai.core.di

import androidx.room.Room
import me.pushkaragnihotri.yogaai.core.HealthConnectManager
import me.pushkaragnihotri.yogaai.core.UserPreferences
import me.pushkaragnihotri.yogaai.core.database.YogaDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val coreDataModule = module {
    single { UserPreferences(androidContext()) }
    single { HealthConnectManager(androidContext()) }

    single {
        Room.databaseBuilder(androidContext(), YogaDatabase::class.java, "yoga_db")
            .fallbackToDestructiveMigration()
            .build()
    }
    single { get<YogaDatabase>().yogaSessionDao() }
}
