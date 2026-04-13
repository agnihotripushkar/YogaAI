package me.pushkaragnihotri.yogaai

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import me.pushkaragnihotri.yogaai.core.UserPreferences
import me.pushkaragnihotri.yogaai.navigation.AppDestinations

class MainViewModel(
    userPreferences: UserPreferences
) : ViewModel() {
    
    val finalDestination: StateFlow<String?> = kotlinx.coroutines.flow.combine(
        userPreferences.onboardingCompleted,
        userPreferences.consentGiven
    ) { onboardingCompleted, consentGiven ->
        when {
            onboardingCompleted -> AppDestinations.HOME
            consentGiven -> AppDestinations.CONNECT
            else -> AppDestinations.ONBOARDING
        }
    }
    .stateIn(viewModelScope, SharingStarted.Lazily, null)
        
    val themeMode: StateFlow<Int> = userPreferences.themeMode
        .stateIn(viewModelScope, SharingStarted.Lazily, 0)

    val dynamicColorEnabled: StateFlow<Boolean> = userPreferences.dynamicColorEnabled
        .stateIn(viewModelScope, SharingStarted.Lazily, false)

    val colorTheme: StateFlow<String> = userPreferences.colorTheme
        .stateIn(viewModelScope, SharingStarted.Lazily, "Default")

    val appFont: StateFlow<String> = userPreferences.appFont
        .stateIn(viewModelScope, SharingStarted.Lazily, "Default")
}
