package me.pushkaragnihotri.yogaai.core.di


import me.pushkaragnihotri.yogaai.features.home.data.repository.HomeRepositoryImpl
import me.pushkaragnihotri.yogaai.features.home.domain.HomeRepository
import me.pushkaragnihotri.yogaai.features.home.ui.HomeViewModel
import me.pushkaragnihotri.yogaai.features.yoga.data.repository.YogaRepositoryImpl
import me.pushkaragnihotri.yogaai.features.yoga.data.source.PoseClassifier
import me.pushkaragnihotri.yogaai.features.yoga.domain.repository.YogaRepository
import me.pushkaragnihotri.yogaai.features.onboarding.ui.OnboardingViewModel
import me.pushkaragnihotri.yogaai.features.settings.viewmodel.SettingsViewModel
import me.pushkaragnihotri.yogaai.features.yoga.viewmodel.YogaDetectorViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val featuresModule = module {
    single<HomeRepository> { HomeRepositoryImpl(get(), isDemoMode = false) }
    
    single { PoseClassifier() }
    single<YogaRepository> { YogaRepositoryImpl() }

    viewModel { HomeViewModel(get()) }
    viewModel { OnboardingViewModel(get(), get()) }
    viewModel { SettingsViewModel(get(), get(), get()) }
    viewModel { YogaDetectorViewModel(get(), get()) }
}
