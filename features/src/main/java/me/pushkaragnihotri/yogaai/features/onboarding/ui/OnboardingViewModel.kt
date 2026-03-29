package me.pushkaragnihotri.yogaai.features.onboarding.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import me.pushkaragnihotri.yogaai.core.HealthConnectManager
import me.pushkaragnihotri.yogaai.core.UserPreferences
import timber.log.Timber

class OnboardingViewModel(
    private val userPreferences: UserPreferences,
    private val healthConnectManager: HealthConnectManager
) : ViewModel() {

    private val _state = MutableStateFlow(OnboardingState())
    val state = _state.asStateFlow()

    private val _events = Channel<OnboardingEvent>()
    val events = _events.receiveAsFlow()

    val permissions get() = healthConnectManager.permissions

    val consentGiven = userPreferences.consentGiven
        .stateIn(viewModelScope, SharingStarted.Lazily, false)

    fun onAction(action: OnboardingAction) {
        when (action) {
            OnboardingAction.OnConsentGranted -> {
                viewModelScope.launch {
                    userPreferences.setConsent(true)
                    _state.update { it.copy(consentGiven = true) }
                    _events.send(OnboardingEvent.NavigateToConnect)
                }
            }
            OnboardingAction.OnOnboardingComplete -> {
                viewModelScope.launch {
                    userPreferences.setOnboardingCompleted(true)
                    _events.send(OnboardingEvent.NavigateToHome)
                }
            }
            is OnboardingAction.OnPermissionsResult -> {
                Timber.d("OnboardingViewModel: permissions result: ${action.granted}")
            }
        }
    }

    fun getHealthConnectAvailability(): Int {
        val availability = healthConnectManager.checkAvailability()
        Timber.d("OnboardingViewModel availability: $availability")
        return availability
    }
}
