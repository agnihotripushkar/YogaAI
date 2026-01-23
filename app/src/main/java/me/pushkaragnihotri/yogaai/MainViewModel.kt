package me.pushkaragnihotri.yogaai

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import me.pushkaragnihotri.yogaai.core.UserPreferences

class MainViewModel(
    userPreferences: UserPreferences
) : ViewModel() {
    
    val finalDestination: StateFlow<String?> = kotlinx.coroutines.flow.combine(
        userPreferences.onboardingCompleted,
        userPreferences.consentGiven
    ) { onboardingCompleted, consentGiven ->
        when {
            onboardingCompleted -> "home"
            consentGiven -> "connect"
            else -> "onboarding"
        }
    }
    .stateIn(viewModelScope, SharingStarted.Lazily, null)
        
    val themeMode: StateFlow<Int> = userPreferences.themeMode
        .stateIn(viewModelScope, SharingStarted.Lazily, 0)
}
