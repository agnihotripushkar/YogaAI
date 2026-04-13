package me.pushkaragnihotri.yogaai.features.onboarding.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
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

    fun getHealthConnectAvailability(): Int {
        val availability = healthConnectManager.checkAvailability()
        Timber.d("OnboardingViewModel availability: $availability")
        return availability
    }

    fun onAction(action: OnboardingAction) {
        when (action) {
            OnboardingAction.NextStep -> {
                _state.update { it.copy(currentStep = it.currentStep + 1) }
            }
            OnboardingAction.PreviousStep -> {
                if (_state.value.currentStep > 0) {
                    _state.update { it.copy(currentStep = it.currentStep - 1) }
                }
            }
            is OnboardingAction.GoalSelected -> {
                _state.update { it.copy(selectedGoal = action.goal) }
            }
            is OnboardingAction.PainToggled -> {
                _state.update {
                    val updated = it.selectedPains.toMutableSet()
                    if (action.pain in updated) updated.remove(action.pain) else updated.add(action.pain)
                    it.copy(selectedPains = updated)
                }
            }
            OnboardingAction.TinderCardAdvanced -> {
                val newIndex = _state.value.tinderIndex + 1
                _state.update {
                    if (newIndex >= TINDER_CARDS_COUNT) {
                        it.copy(tinderIndex = newIndex, currentStep = it.currentStep + 1)
                    } else {
                        it.copy(tinderIndex = newIndex)
                    }
                }
            }
            is OnboardingAction.NameChanged -> _state.update { it.copy(userName = action.name) }
            is OnboardingAction.AgeChanged -> _state.update { it.copy(userAge = action.age) }
            is OnboardingAction.LevelSelected -> _state.update { it.copy(selectedLevel = action.level) }
            is OnboardingAction.StepGoalChanged -> _state.update { it.copy(stepGoal = action.steps) }
            is OnboardingAction.SleepGoalChanged -> _state.update { it.copy(sleepGoal = action.hours) }
            is OnboardingAction.PoseSelected -> _state.update { it.copy(selectedPose = action.poseName) }
            is OnboardingAction.CameraPermissionResult -> {
                _state.update { it.copy(currentStep = it.currentStep + 1) }
            }
            is OnboardingAction.HealthPermissionsResult -> {
                _state.update { it.copy(currentStep = it.currentStep + 1) }
            }
            OnboardingAction.CompleteOnboarding -> {
                viewModelScope.launch {
                    val s = _state.value
                    userPreferences.setConsent(true)
                    userPreferences.setOnboardingCompleted(true)
                    if (s.userName.isNotBlank()) userPreferences.setUserName(s.userName)
                    s.userAge.toIntOrNull()?.takeIf { it > 0 }?.let { userPreferences.setUserAge(it) }
                    userPreferences.setUserLevel(s.selectedLevel)
                    userPreferences.setStepGoal(s.stepGoal)
                    userPreferences.setSleepGoal(s.sleepGoal)
                    _events.send(OnboardingEvent.NavigateToHome)
                }
            }
        }
    }

    companion object {
        const val TINDER_CARDS_COUNT = 4
    }
}
