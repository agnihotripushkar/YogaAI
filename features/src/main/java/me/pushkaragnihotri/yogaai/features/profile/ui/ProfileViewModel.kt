package me.pushkaragnihotri.yogaai.features.profile.ui

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import me.pushkaragnihotri.yogaai.core.HealthConnectManager
import androidx.health.connect.client.HealthConnectClient
import me.pushkaragnihotri.yogaai.core.repository.WellnessRepository
import timber.log.Timber
import java.time.Instant

class ProfileViewModel(
    private val healthConnectManager: HealthConnectManager,
    private val wellnessRepository: WellnessRepository
) : ViewModel() {

    private val _steps = MutableStateFlow(0L)
    val steps: StateFlow<Long> = _steps.asStateFlow()

    private val _calories = MutableStateFlow(0.0)
    val calories: StateFlow<Double> = _calories.asStateFlow()

    val permissions = healthConnectManager.permissions

    var hasPermissions = mutableStateOf(false)
        private set

    var sdkStatus = mutableStateOf(HealthConnectManager.SDK_UNAVAILABLE)
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
        Timber.d("ProfileViewModel initialLoad status: $currentStatus")
        
        if (currentStatus == HealthConnectManager.SDK_AVAILABLE) {
            viewModelScope.launch {
                hasPermissions.value = healthConnectManager.hasAllPermissions()
                Timber.d("ProfileViewModel permissions: ${hasPermissions.value}")
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
}