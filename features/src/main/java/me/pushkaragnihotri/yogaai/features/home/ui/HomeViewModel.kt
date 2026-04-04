package me.pushkaragnihotri.yogaai.features.home.ui

import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DirectionsRun
import androidx.compose.material.icons.rounded.EmojiEvents
import androidx.compose.material.icons.rounded.LocalFireDepartment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import me.pushkaragnihotri.yogaai.core.HealthConnectManager
import me.pushkaragnihotri.yogaai.core.UserPreferences
import me.pushkaragnihotri.yogaai.core.YogaSessionRecord
import me.pushkaragnihotri.yogaai.core.presentation.UiText
import me.pushkaragnihotri.yogaai.features.R
import me.pushkaragnihotri.yogaai.features.home.data.YogaPracticeStats
import me.pushkaragnihotri.yogaai.features.ui.theme.WellnessCalories
import me.pushkaragnihotri.yogaai.features.ui.theme.WellnessSteps
import me.pushkaragnihotri.yogaai.features.ui.theme.WellnessStreak
import me.pushkaragnihotri.yogaai.features.home.data.model.DailyMetric
import me.pushkaragnihotri.yogaai.features.home.data.model.RiskPrediction
import me.pushkaragnihotri.yogaai.features.home.domain.HomeRepository
import timber.log.Timber

class HomeViewModel(
    private val homeRepository: HomeRepository,
    private val healthConnectManager: HealthConnectManager,
    private val userPreferences: UserPreferences,
    private val appContext: Context
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    private val _events = Channel<HomeEvent>()
    val events = _events.receiveAsFlow()

    init {
        checkPermissionsAndLoad()
        viewModelScope.launch {
            combine(
                userPreferences.yogaSessions,
                homeRepository.todayMetrics
            ) { sessions, metrics -> sessions to metrics }
                .collect { (sessions, metrics) ->
                    if (!_state.value.isLoading) {
                        patchPracticeWellness(metrics, sessions)
                    }
                }
        }
    }

    fun onAction(action: HomeAction) {
        when (action) {
            HomeAction.OnRefreshClick -> loadData()
            HomeAction.OnGrantPermissionClick -> {
                viewModelScope.launch {
                    _events.send(HomeEvent.RequestPermissions(healthConnectManager.permissions))
                }
            }
            is HomeAction.OnPermissionsResult -> {
                val granted = action.granted.containsAll(healthConnectManager.permissions)
                _state.update { it.copy(hasPermissions = granted) }
                Timber.d("HomeViewModel onPermissionsResult: hasPermissions=$granted")
                if (granted) loadData()
            }
            HomeAction.OnResumed -> checkPermissionsAndLoad()
        }
    }

    private fun checkPermissionsAndLoad() {
        val sdkAvailable = healthConnectManager.checkAvailability() == HealthConnectManager.SDK_AVAILABLE
        _state.update { it.copy(sdkAvailable = sdkAvailable) }

        if (sdkAvailable) {
            viewModelScope.launch {
                val hasPerms = healthConnectManager.hasAllPermissions()
                _state.update { it.copy(hasPermissions = hasPerms) }
                Timber.d("HomeViewModel checkPermissionsAndLoad: hasPermissions=$hasPerms")
                loadData()
            }
        } else {
            _state.update { it.copy(hasPermissions = false) }
            Timber.d("HomeViewModel: Health Connect SDK not available")
            loadData()
        }
    }

    private fun loadData() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                homeRepository.refreshMetrics()
                val metrics = homeRepository.todayMetrics.value
                val risk = homeRepository.getTodayRisk()
                val sessions = userPreferences.yogaSessions.first()
                val items = buildWellnessItems(metrics, sessions)
                _state.update {
                    it.copy(
                        riskPrediction = risk,
                        wellnessItems = items,
                        isLoading = false,
                        error = null
                    )
                }
            } catch (e: Exception) {
                Timber.e(e, "Failed to load home data")
                val errorText = UiText.DynamicString(e.message ?: "Failed to load data")
                _state.update { it.copy(isLoading = false, error = errorText) }
                _events.send(HomeEvent.ShowSnackbar(errorText))
            }
        }
    }

    private fun patchPracticeWellness(metrics: DailyMetric, sessions: List<YogaSessionRecord>) {
        _state.update {
            it.copy(wellnessItems = buildWellnessItems(metrics, sessions))
        }
    }

    private fun buildWellnessItems(metrics: DailyMetric, sessions: List<YogaSessionRecord>): List<WellnessUiModel> {
        val streak = YogaPracticeStats.streakDays(sessions)
        val week = YogaPracticeStats.sessionsLast7Days(sessions)
        return listOf(
            WellnessUiModel(
                titleRes = R.string.metric_streak,
                value = appContext.getString(R.string.practice_streak_days, streak),
                icon = Icons.Rounded.EmojiEvents,
                color = WellnessStreak,
                subtitle = appContext.getString(R.string.practice_sessions_week_line, week)
            ),
            WellnessUiModel(
                titleRes = R.string.metric_calories,
                value = "${metrics.calories.toInt()} kcal",
                icon = Icons.Rounded.LocalFireDepartment,
                color = WellnessCalories
            ),
            WellnessUiModel(
                titleRes = R.string.metric_steps,
                value = "${metrics.steps}",
                icon = Icons.Rounded.DirectionsRun,
                color = WellnessSteps
            )
        )
    }
}
