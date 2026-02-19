package me.pushkaragnihotri.yogaai.features.home.ui

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import me.pushkaragnihotri.yogaai.core.HealthConnectManager
import me.pushkaragnihotri.yogaai.features.home.data.model.RiskPrediction
import me.pushkaragnihotri.yogaai.features.home.domain.HomeRepository
import me.pushkaragnihotri.yogaai.features.home.data.model.WellnessUiModel
import timber.log.Timber

data class HomeUiState(
    val riskPrediction: RiskPrediction? = null,
    val wellnessItems: List<WellnessUiModel> = emptyList(),
    val isLoading: Boolean = true
)

class HomeViewModel(
    private val homeRepository: HomeRepository,
    private val healthConnectManager: HealthConnectManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    /** Expose the Health Connect permission set so the UI can launch the request. */
    val permissions get() = healthConnectManager.permissions

    /** Observable permission state for the UI. */
    var hasPermissions = mutableStateOf(false)
        private set

    /** Observable SDK status for the UI. */
    var sdkStatus = mutableStateOf(HealthConnectManager.SDK_UNAVAILABLE)
        private set

    init {
        loadData()
    }

    /**
     * Called from the screen on every ON_RESUME to re-verify permissions
     * (the user may have revoked them while the app was backgrounded).
     */
    fun checkPermissionsAndLoad() {
        val currentStatus = healthConnectManager.checkAvailability()
        sdkStatus.value = currentStatus

        if (currentStatus == HealthConnectManager.SDK_AVAILABLE) {
            viewModelScope.launch {
                hasPermissions.value = healthConnectManager.hasAllPermissions()
                Timber.d("HomeViewModel checkPermissionsAndLoad: hasPermissions=${hasPermissions.value}")
                loadData()
            }
        } else {
            hasPermissions.value = false
            Timber.d("HomeViewModel: Health Connect SDK not available (status=$currentStatus)")
            loadData()
        }
    }

    /**
     * Called after the user responds to the permission dialog.
     */
    fun onPermissionsResult(granted: Set<String>) {
        hasPermissions.value = granted.containsAll(permissions)
        Timber.d("HomeViewModel onPermissionsResult: hasPermissions=${hasPermissions.value}")
        if (hasPermissions.value) {
            loadData()
        }
    }

    fun loadData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                homeRepository.refreshMetrics()
                val risk = homeRepository.getTodayRisk()
                _uiState.update {
                    it.copy(
                        riskPrediction = risk,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                Timber.e(e, "Failed to load home data")
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }
}
