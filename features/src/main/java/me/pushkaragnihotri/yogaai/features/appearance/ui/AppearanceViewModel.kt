package me.pushkaragnihotri.yogaai.features.appearance.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import me.pushkaragnihotri.yogaai.core.UserPreferences

data class AppearanceState(
    val themeMode: Int = 0,
    val colorTheme: String = "Default",
    val appFont: String = "Default",
)

sealed interface AppearanceAction {
    data class OnThemeModeChange(val mode: Int) : AppearanceAction
    data class OnColorThemeChange(val theme: String) : AppearanceAction
    data class OnFontChange(val font: String) : AppearanceAction
}

class AppearanceViewModel(
    private val userPreferences: UserPreferences
) : ViewModel() {

    private val _state = MutableStateFlow(AppearanceState())
    val state = _state.asStateFlow()

    init {
        combine(
            userPreferences.themeMode,
            userPreferences.colorTheme,
            userPreferences.appFont
        ) { mode, theme, font ->
            AppearanceState(themeMode = mode, colorTheme = theme, appFont = font)
        }.onEach { _state.value = it }
            .launchIn(viewModelScope)
    }

    fun onAction(action: AppearanceAction) {
        when (action) {
            is AppearanceAction.OnThemeModeChange ->
                viewModelScope.launch { userPreferences.setThemeMode(action.mode) }
            is AppearanceAction.OnColorThemeChange ->
                viewModelScope.launch { userPreferences.setColorTheme(action.theme) }
            is AppearanceAction.OnFontChange ->
                viewModelScope.launch { userPreferences.setAppFont(action.font) }
        }
    }
}
