package me.pushkaragnihotri.yogaai.features.di

import me.pushkaragnihotri.yogaai.features.home.ui.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val featuresModule = module {
    viewModel { HomeViewModel() }
}
