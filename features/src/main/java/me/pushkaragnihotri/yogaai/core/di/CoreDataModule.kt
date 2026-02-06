package me.pushkaragnihotri.yogaai.core.di

import me.pushkaragnihotri.yogaai.core.HealthConnectManager
import me.pushkaragnihotri.yogaai.core.UserPreferences
import me.pushkaragnihotri.yogaai.core.ai.PoseClassifier
import me.pushkaragnihotri.yogaai.core.ai.PoseDetectionManager
import me.pushkaragnihotri.yogaai.core.ai.PoseDetectionManagerImpl
import me.pushkaragnihotri.yogaai.core.repository.WellnessRepository
import me.pushkaragnihotri.yogaai.core.repository.WellnessRepositoryImpl
import me.pushkaragnihotri.yogaai.core.wellness.GeminiNanoExplanationProvider
import me.pushkaragnihotri.yogaai.core.wellness.WellnessExplanationGenerator
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val coreDataModule = module {
    single { UserPreferences(androidContext()) }
    single { HealthConnectManager(androidContext()) }

    // AI
    single { PoseClassifier() }
    single<PoseDetectionManager> { PoseDetectionManagerImpl() }
    single<WellnessExplanationGenerator> { GeminiNanoExplanationProvider() }

    single<WellnessRepository> { WellnessRepositoryImpl(get(), get()) }
}
