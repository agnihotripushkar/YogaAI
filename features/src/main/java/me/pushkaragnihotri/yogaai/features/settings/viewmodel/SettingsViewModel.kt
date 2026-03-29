package me.pushkaragnihotri.yogaai.features.settings.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import me.pushkaragnihotri.yogaai.core.HealthConnectManager
import me.pushkaragnihotri.yogaai.core.UserPreferences
import me.pushkaragnihotri.yogaai.features.home.domain.HomeRepository

class SettingsViewModel(
    private val userPreferences: UserPreferences,
    private val healthConnectManager: HealthConnectManager,
    private val homeRepository: HomeRepository
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsState())
    val state = _state.asStateFlow()

    private val _events = Channel<SettingsEvent>()
    val events = _events.receiveAsFlow()

    val permissions = healthConnectManager.permissions

    init {
        viewModelScope.launch {
            combine(
                userPreferences.themeMode,
                userPreferences.language,
                homeRepository.todayMetrics
            ) { theme, lang, metrics ->
                Triple(theme, lang, metrics)
            }.collect { (theme, lang, metrics) ->
                _state.update {
                    it.copy(
                        themeMode = theme,
                        language = lang,
                        steps = metrics.steps,
                        calories = metrics.calories
                    )
                }
            }
        }
    }

    fun onAction(action: SettingsAction) {
        when (action) {
            SettingsAction.OnInitialLoad -> initialLoad()
            is SettingsAction.OnThemeChange -> {
                viewModelScope.launch { userPreferences.setThemeMode(action.mode) }
            }
            is SettingsAction.OnLanguageChange -> {
                viewModelScope.launch { userPreferences.setLanguage(action.language) }
            }
            SettingsAction.OnConnectClick -> {
                viewModelScope.launch {
                    _events.send(SettingsEvent.RequestPermissions(healthConnectManager.permissions))
                }
            }
            is SettingsAction.OnPermissionsResult -> {
                val granted = action.granted.containsAll(permissions)
                _state.update { it.copy(hasPermissions = granted) }
                if (granted) {
                    viewModelScope.launch { homeRepository.refreshMetrics() }
                }
            }
            SettingsAction.OnDisconnectWearable -> { /* Clear tokens if applicable */ }
            SettingsAction.OnDeleteData -> {
                viewModelScope.launch {
                    userPreferences.setConsent(false)
                    userPreferences.setOnboardingCompleted(false)
                }
            }
            SettingsAction.OnPrivacyPolicyClick -> {
                viewModelScope.launch {
                    _events.send(SettingsEvent.OpenUrl("https://www.google.com"))
                }
            }
        }
    }

    private fun initialLoad() {
        val sdkAvailable = healthConnectManager.checkAvailability() == HealthConnectManager.SDK_AVAILABLE
        _state.update { it.copy(sdkAvailable = sdkAvailable) }

        if (sdkAvailable) {
            viewModelScope.launch {
                val hasPerms = healthConnectManager.hasAllPermissions()
                _state.update { it.copy(hasPermissions = hasPerms) }
                if (hasPerms) homeRepository.refreshMetrics()
            }
        } else {
            _state.update { it.copy(hasPermissions = false) }
        }
    }
}
