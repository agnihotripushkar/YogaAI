package me.pushkaragnihotri.yogaai.features.goals.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import me.pushkaragnihotri.yogaai.core.data.UserPreferences

class GoalsViewModel(
    private val userPreferences: UserPreferences
) : ViewModel() {
    
    val stepGoal = userPreferences.stepGoal
        .stateIn(viewModelScope, SharingStarted.Lazily, 6000)
    
    val sleepGoal = userPreferences.sleepGoal
        .stateIn(viewModelScope, SharingStarted.Lazily, 8)
        
    fun updateStepGoal(steps: Int) {
        viewModelScope.launch {
            userPreferences.setStepGoal(steps)
        }
    }

    fun updateSleepGoal(hours: Int) {
        viewModelScope.launch {
            userPreferences.setSleepGoal(hours)
        }
    }
}
