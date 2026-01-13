package me.pushkaragnihotri.yogaai.features.onboarding.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import me.pushkaragnihotri.yogaai.core.data.HealthConnectManager
import me.pushkaragnihotri.yogaai.core.data.UserPreferences

class OnboardingViewModel(
    private val userPreferences: UserPreferences,
    private val healthConnectManager: HealthConnectManager
) : ViewModel() {

    private val _currentStep = MutableStateFlow(OnboardingStep.SPLASH)
    val currentStep: StateFlow<OnboardingStep> = _currentStep

    val consentGiven = userPreferences.consentGiven
        .stateIn(viewModelScope, SharingStarted.Lazily, false)

    // Check if permissions are granted (to update UI state in Connect screen)
    // Note: This needs to be polled or updated on resume in real app. For now, we check on request.
    
    fun onSplashFinished() {
        _currentStep.value = OnboardingStep.INTRO
    }

    fun onIntroFinished() {
        _currentStep.value = OnboardingStep.CONSENT
    }

    fun onConsentGranted() {
        viewModelScope.launch {
            userPreferences.setConsent(true)
            _currentStep.value = OnboardingStep.CONNECT
        }
    }

    fun onConnectFinished() {
        _currentStep.value = OnboardingStep.PROFILE
    }
    
    fun onProfileFinished(name: String, age: Int, level: String) {
         viewModelScope.launch {
             userPreferences.setUserName(name)
             userPreferences.setUserAge(age)
             userPreferences.setUserLevel(level)
             userPreferences.setOnboardingCompleted(true)
             // Navigate to Home handled by main nav
         }
    }
}

enum class OnboardingStep {
    SPLASH, INTRO, CONSENT, CONNECT, PROFILE
}
