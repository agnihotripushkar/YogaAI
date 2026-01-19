package me.pushkaragnihotri.yogaai.features.di

import me.pushkaragnihotri.yogaai.features.goals.ui.GoalsViewModel
import me.pushkaragnihotri.yogaai.features.home.ui.HomeViewModel
import me.pushkaragnihotri.yogaai.features.insights.ui.InsightsViewModel
import me.pushkaragnihotri.yogaai.features.onboarding.ui.OnboardingViewModel
import me.pushkaragnihotri.yogaai.features.profile.ui.ProfileViewModel
import me.pushkaragnihotri.yogaai.features.settings.ui.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val featuresModule = module {
    viewModel { HomeViewModel(get()) }
    viewModel { OnboardingViewModel(get(), get()) }
    viewModel { InsightsViewModel(get()) }
    viewModel { GoalsViewModel(get()) }
    viewModel { SettingsViewModel(get()) }
    viewModel { ProfileViewModel(get()) }
}
