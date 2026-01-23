package me.pushkaragnihotri.yogaai.core.di

import me.pushkaragnihotri.yogaai.core.HealthConnectManager
import me.pushkaragnihotri.yogaai.core.UserPreferences
import me.pushkaragnihotri.yogaai.core.repository.WellnessRepository
import me.pushkaragnihotri.yogaai.core.repository.WellnessRepositoryImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val coreDataModule = module {
    single { UserPreferences(androidContext()) }
    single { HealthConnectManager(androidContext()) }

    single<WellnessRepository> { WellnessRepositoryImpl(get()) }
}
