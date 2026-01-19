package me.pushkaragnihotri.yogaai.features.profile.ui

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import me.pushkaragnihotri.yogaai.core.data.HealthConnectManager
import androidx.health.connect.client.HealthConnectClient
import java.time.Instant
import java.time.temporal.ChronoUnit

class ProfileViewModel(private val healthConnectManager: HealthConnectManager) : ViewModel() {

    private val _steps = MutableStateFlow(0L)
    val steps: StateFlow<Long> = _steps.asStateFlow()

    private val _calories = MutableStateFlow(0.0)
    val calories: StateFlow<Double> = _calories.asStateFlow()

    val permissions = healthConnectManager.permissions

    var hasPermissions = mutableStateOf(false)
        private set

    var sdkStatus = mutableStateOf(HealthConnectManager.SDK_UNAVAILABLE)
        private set

    fun initialLoad() {
        sdkStatus.value = healthConnectManager.checkAvailability()
        if (sdkStatus.value == HealthConnectManager.SDK_AVAILABLE) {
            viewModelScope.launch {
                hasPermissions.value = healthConnectManager.hasAllPermissions()
                if (hasPermissions.value) {
                    readHealthData()
                }
            }
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
            val now = Instant.now()
            val startTime = now.minus(1, ChronoUnit.DAYS)
            _steps.value = healthConnectManager.readSteps(startTime, now)
            _calories.value = healthConnectManager.readCalories(startTime, now)
        }
    }
}