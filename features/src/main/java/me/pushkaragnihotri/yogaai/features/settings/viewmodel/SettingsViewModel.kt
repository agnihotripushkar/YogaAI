package me.pushkaragnihotri.yogaai.features.settings.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import me.pushkaragnihotri.yogaai.core.HealthConnectManager
import me.pushkaragnihotri.yogaai.core.UserPreferences
import me.pushkaragnihotri.yogaai.core.repository.WellnessRepository

class SettingsViewModel(
    private val userPreferences: UserPreferences,
    private val healthConnectManager: HealthConnectManager,
    private val wellnessRepository: WellnessRepository
) : ViewModel() {

    val themeMode = userPreferences.themeMode
    val language = userPreferences.language

    private val _steps = MutableStateFlow(0L)
    val steps: StateFlow<Long> = _steps.asStateFlow()

    private val _calories = MutableStateFlow(0.0)
    val calories: StateFlow<Double> = _calories.asStateFlow()

    val permissions = healthConnectManager.permissions

    var hasPermissions = mutableStateOf(false)
        private set

    var sdkStatus = mutableStateOf(HealthConnectManager.Companion.SDK_UNAVAILABLE)
        private set

    init {
        viewModelScope.launch {
            wellnessRepository.todayMetrics.collect { metrics ->
                _steps.value = metrics.steps
                _calories.value = metrics.calories
            }
        }
    }

    fun initialLoad() {
        val currentStatus = healthConnectManager.checkAvailability()
        sdkStatus.value = currentStatus

        if (currentStatus == HealthConnectManager.Companion.SDK_AVAILABLE) {
            viewModelScope.launch {
                hasPermissions.value = healthConnectManager.hasAllPermissions()
                if (hasPermissions.value) {
                    readHealthData()
                }
            }
        } else {
            hasPermissions.value = false
        }
    }

    fun onPermissionsResult(granted: Set<String>) {
        if (granted.containsAll(permissions)) {
            hasPermissions.value = true
            readHealthData()
        }
    }

    private fun readHealthData() {
        viewModelScope.launch {
            wellnessRepository.refreshMetrics()
        }
    }

    fun setTheme(mode: Int) {
        viewModelScope.launch {
            userPreferences.setThemeMode(mode)
        }
    }

    fun setLanguage(language: String) {
        viewModelScope.launch {
            userPreferences.setLanguage(language)
        }
    }

    // Logic to disconnect or delete data
    fun disconnectWearable() {
        // Clear tokens if any. For demo, just maybe unset connection flag if we had one.
    }

    fun deleteData() {
        viewModelScope.launch {
            userPreferences.setConsent(false)
            userPreferences.setOnboardingCompleted(false)
            // Trigger app restart or nav to splash in real app
        }
    }
}