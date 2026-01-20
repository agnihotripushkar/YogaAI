package me.pushkaragnihotri.yogaai.features.onboarding.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import me.pushkaragnihotri.yogaai.core.data.HealthConnectManager
import me.pushkaragnihotri.yogaai.core.data.UserPreferences

class OnboardingViewModel(
    private val userPreferences: UserPreferences,
    private val healthConnectManager: HealthConnectManager
) : ViewModel() {

    val consentGiven = userPreferences.consentGiven
        .stateIn(viewModelScope, SharingStarted.Lazily, false)

    val permissions get() = healthConnectManager.permissions
    
    fun getHealthConnectAvailability(): Int {
        return healthConnectManager.checkAvailability()
    }

    fun onPermissionsResult(granted: Set<String>) {
        // Handled in navigation common flow, but keeping for logic if needed
    }

    fun onConsentGranted() {
        viewModelScope.launch {
            userPreferences.setConsent(true)
        }
    }

    fun onProfileFinished(name: String, age: Int, level: String) {
         viewModelScope.launch {
             userPreferences.setUserName(name)
             userPreferences.setUserAge(age)
             userPreferences.setUserLevel(level)
             userPreferences.setOnboardingCompleted(true)
         }
    }
}
