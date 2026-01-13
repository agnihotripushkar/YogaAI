package me.pushkaragnihotri.yogaai

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import me.pushkaragnihotri.yogaai.core.data.UserPreferences

class MainViewModel(
    userPreferences: UserPreferences
) : ViewModel() {
    
    val startDestination: StateFlow<String?> = userPreferences.onboardingCompleted
        .map { completed ->
            if (completed) "home" else "onboarding"
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, null)
}
