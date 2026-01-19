package me.pushkaragnihotri.yogaai.core.data.di

import me.pushkaragnihotri.yogaai.core.data.HealthConnectManager
import me.pushkaragnihotri.yogaai.core.data.UserPreferences
import me.pushkaragnihotri.yogaai.core.data.repository.WellnessRepository
import me.pushkaragnihotri.yogaai.core.data.repository.WellnessRepositoryImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val coreDataModule = module {
    single { UserPreferences(androidContext()) }
    single { HealthConnectManager(androidContext()) }

    single<WellnessRepository> { WellnessRepositoryImpl(get()) }
}
