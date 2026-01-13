package me.pushkaragnihotri.yogaai.features.settings.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import me.pushkaragnihotri.yogaai.core.data.UserPreferences

class SettingsViewModel(
    private val userPreferences: UserPreferences
) : ViewModel() {

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
