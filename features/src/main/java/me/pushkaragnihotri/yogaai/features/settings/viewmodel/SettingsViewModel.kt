package me.pushkaragnihotri.yogaai.features.settings.ui

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import me.pushkaragnihotri.yogaai.core.HealthConnectManager
import me.pushkaragnihotri.yogaai.core.UserPreferences
import me.pushkaragnihotri.yogaai.features.R

class SettingsViewModel(
    private val userPreferences: UserPreferences,
    private val healthConnectManager: HealthConnectManager,
    private val appContext: Context
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsState())
    val state = _state.asStateFlow()

    private val _events = Channel<SettingsEvent>()
    val events = _events.receiveAsFlow()

    val permissions = healthConnectManager.permissions

    init {
        viewModelScope.launch {
            combine(
                combine(
                    userPreferences.themeMode,
                    userPreferences.language,
                    userPreferences.dynamicColorEnabled
                ) { theme, lang, dynamic ->
                    Triple(theme, lang, dynamic)
                },
                combine(
                    userPreferences.userName,
                    userPreferences.userAge,
                    userPreferences.userLevel
                ) { userName, userAge, userLevel ->
                    Triple(userName, userAge, userLevel)
                },
                combine(
                    userPreferences.userSex,
                    userPreferences.userHeight,
                    userPreferences.userWeight,
                    userPreferences.userTargetWeight
                ) { sex, height, weight, targetWeight ->
                    listOf(sex, height.toString(), weight.toString(), targetWeight.toString())
                }
            ) { themeTriple, profileTriple, bodyList ->
                SettingsSnapshot(
                    theme = themeTriple.first,
                    lang = themeTriple.second,
                    dynamic = themeTriple.third,
                    userName = profileTriple.first,
                    userAge = profileTriple.second,
                    userLevel = profileTriple.third,
                    userSex = bodyList[0],
                    userHeight = bodyList[1].toIntOrNull() ?: 0,
                    userWeight = bodyList[2].toFloatOrNull() ?: 0f,
                    userTargetWeight = bodyList[3].toFloatOrNull() ?: 0f
                )
            }.collect { snap ->
                _state.update {
                    it.copy(
                        themeMode = snap.theme,
                        language = snap.lang,
                        dynamicColor = snap.dynamic,
                        userName = snap.userName,
                        userAge = snap.userAge,
                        userLevel = snap.userLevel,
                        userSex = snap.userSex,
                        userHeight = snap.userHeight,
                        userWeight = snap.userWeight,
                        userTargetWeight = snap.userTargetWeight
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
            is SettingsAction.OnDynamicColorChange -> {
                viewModelScope.launch { userPreferences.setDynamicColor(action.enabled) }
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
                _state.update { it }
            }
            SettingsAction.OnDisconnectWearable -> { /* Clear tokens if applicable */ }
            SettingsAction.OnFeedbackClick -> {
                viewModelScope.launch {
                    _events.send(SettingsEvent.OpenUrl("mailto:${appContext.getString(R.string.settings_feedback_email)}"))
                }
            }
            SettingsAction.OnDeleteData -> {
                viewModelScope.launch {
                    userPreferences.setConsent(false)
                    userPreferences.setOnboardingCompleted(false)
                }
            }
            SettingsAction.OnPrivacyPolicyClick -> {
                viewModelScope.launch {
                    _events.send(SettingsEvent.OpenUrl(appContext.getString(R.string.settings_privacy_policy_url)))
                }
            }
            SettingsAction.OnRateAppClick -> {
                viewModelScope.launch { _events.send(SettingsEvent.RateApp) }
            }
            SettingsAction.OnShareAppClick -> {
                viewModelScope.launch {
                    val pkg = appContext.packageName
                    val storeUrl = "https://play.google.com/store/apps/details?id=$pkg"
                    val message = appContext.getString(R.string.settings_share_message, storeUrl)
                    _events.send(SettingsEvent.ShareApp(message))
                }
            }
            SettingsAction.OnAppearanceClick -> {
                viewModelScope.launch { _events.send(SettingsEvent.NavigateToAppearance) }
            }
            SettingsAction.OnProfileEditorOpen -> {
                _state.update { it.copy(showProfileEditor = true) }
            }
            SettingsAction.OnProfileEditorDismiss -> {
                _state.update { it.copy(showProfileEditor = false) }
            }
            is SettingsAction.OnProfileSave -> {
                viewModelScope.launch {
                    userPreferences.setUserName(action.name.trim())
                    userPreferences.setUserAge(action.age.coerceAtLeast(0))
                    if (action.sex.isNotBlank()) userPreferences.setUserSex(action.sex)
                    if (action.height > 0) userPreferences.setUserHeight(action.height)
                    if (action.weight > 0f) userPreferences.setUserWeight(action.weight)
                    if (action.targetWeight > 0f) userPreferences.setUserTargetWeight(action.targetWeight)
                    userPreferences.setUserLevel(action.level)
                    _state.update { it.copy(showProfileEditor = false) }
                }
            }
        }
    }

    private fun initialLoad() {
        val versionPair = readAppVersion()
        _state.update {
            it.copy(
                appVersionName = versionPair.first,
                appVersionCode = versionPair.second
            )
        }
    }

    private fun readAppVersion(): Pair<String, Long> {
        return try {
            val pm = appContext.packageManager
            val pkg = appContext.packageName
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                val info = pm.getPackageInfo(pkg, PackageManager.PackageInfoFlags.of(0))
                (info.versionName ?: "—") to (info.longVersionCode)
            } else {
                @Suppress("DEPRECATION")
                val info = pm.getPackageInfo(pkg, 0)
                val code = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    info.longVersionCode
                } else {
                    @Suppress("DEPRECATION")
                    info.versionCode.toLong()
                }
                (info.versionName ?: "—") to code
            }
        } catch (_: Exception) {
            "—" to 0L
        }
    }

    private data class SettingsSnapshot(
        val theme: Int,
        val lang: String,
        val dynamic: Boolean,
        val userName: String,
        val userAge: Int,
        val userLevel: String,
        val userSex: String,
        val userHeight: Int,
        val userWeight: Float,
        val userTargetWeight: Float
    )
}
