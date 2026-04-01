package me.pushkaragnihotri.yogaai.core.di

import me.pushkaragnihotri.yogaai.features.home.data.repository.HomeRepositoryImpl
import me.pushkaragnihotri.yogaai.features.home.domain.HomeRepository
import me.pushkaragnihotri.yogaai.features.home.ui.HomeViewModel
import me.pushkaragnihotri.yogaai.features.onboarding.ui.OnboardingViewModel
import me.pushkaragnihotri.yogaai.features.settings.ui.SettingsViewModel
import me.pushkaragnihotri.yogaai.features.yoga.data.repository.YogaRepositoryImpl
import me.pushkaragnihotri.yogaai.features.yoga.data.source.PoseClassifier
import me.pushkaragnihotri.yogaai.features.yoga.domain.repository.YogaRepository
import me.pushkaragnihotri.yogaai.features.yoga.ui.YogaDetectorViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val featuresModule = module {
    single<HomeRepository> { HomeRepositoryImpl(get()) }

    single { PoseClassifier() }
    single<YogaRepository> { YogaRepositoryImpl() }

    viewModel { HomeViewModel(get(), get(), get(), androidContext()) }
    viewModel { OnboardingViewModel(get(), get()) }
    viewModel { SettingsViewModel(get(), get(), get(), androidContext()) }
    viewModel { YogaDetectorViewModel(get(), get()) }
}
