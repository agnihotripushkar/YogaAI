package me.pushkaragnihotri.yogaai.features.di

import me.pushkaragnihotri.yogaai.features.home.ui.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val featuresModule = module {
    viewModel { HomeViewModel(get()) }
    viewModel { OnboardingViewModel(get(), get()) }
    viewModel { InsightsViewModel(get()) }
    viewModel { GoalsViewModel(get()) }
    viewModel { me.pushkaragnihotri.yogaai.features.settings.ui.SettingsViewModel(get()) }
}
