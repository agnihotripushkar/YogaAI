package me.pushkaragnihotri.yogaai.features.home.di

import me.pushkaragnihotri.yogaai.features.home.data.repository.HomeRepositoryImpl
import me.pushkaragnihotri.yogaai.features.home.domain.repository.HomeRepository
import me.pushkaragnihotri.yogaai.features.home.ui.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val homeModule = module {
    single<HomeRepository> { 
        HomeRepositoryImpl(
            healthConnectManager = get(),
            // explanationGenerator = get(), // Assuming integrated later or null
            isDemoMode = true // Or configured via build config/flag
        ) 
    }

    viewModel { HomeViewModel(homeRepository = get()) }
}
