package me.pushkaragnihotri.yogaai.features.di


import me.pushkaragnihotri.yogaai.features.home.ui.HomeViewModel
import me.pushkaragnihotri.yogaai.features.onboarding.ui.OnboardingViewModel
import me.pushkaragnihotri.yogaai.features.settings.ui.SettingsViewModel
import me.pushkaragnihotri.yogaai.features.yoga.viewmodel.YogaDetectorViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val featuresModule = module {
    factory { me.pushkaragnihotri.yogaai.features.home.domain.GetDailyWellnessUseCase(get()) }

    viewModel { HomeViewModel(get(), get()) }
    viewModel { OnboardingViewModel(get(), get()) }

    viewModel { SettingsViewModel(get(), get(), get()) }
    viewModel { YogaDetectorViewModel(get(), get()) }
}
