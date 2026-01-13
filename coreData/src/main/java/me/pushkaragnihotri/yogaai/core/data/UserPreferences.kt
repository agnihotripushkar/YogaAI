package me.pushkaragnihotri.yogaai.core.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

class UserPreferences(private val context: Context) {
    
    companion object {
        val KEY_CONSENT_GIVEN = booleanPreferencesKey("consent_given")
        val KEY_ONBOARDING_COMPLETED = booleanPreferencesKey("onboarding_completed")
        val KEY_IS_DEMO_MODE = booleanPreferencesKey("is_demo_mode")
        val KEY_STEP_GOAL = androidx.datastore.preferences.core.intPreferencesKey("step_goal")
        val KEY_SLEEP_GOAL = androidx.datastore.preferences.core.intPreferencesKey("sleep_goal")
    }

    val consentGiven: Flow<Boolean> = context.dataStore.data
        .map { preferences -> preferences[KEY_CONSENT_GIVEN] ?: false }

    val onboardingCompleted: Flow<Boolean> = context.dataStore.data
        .map { preferences -> preferences[KEY_ONBOARDING_COMPLETED] ?: false }

    val isDemoMode: Flow<Boolean> = context.dataStore.data
        .map { preferences -> preferences[KEY_IS_DEMO_MODE] ?: true }

    val stepGoal: Flow<Int> = context.dataStore.data
        .map { preferences -> preferences[KEY_STEP_GOAL] ?: 6000 }

    val sleepGoal: Flow<Int> = context.dataStore.data
        .map { preferences -> preferences[KEY_SLEEP_GOAL] ?: 8 }

    suspend fun setConsent(given: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[KEY_CONSENT_GIVEN] = given
        }
    }

    suspend fun setOnboardingCompleted(completed: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[KEY_ONBOARDING_COMPLETED] = completed
        }
    }
    
    suspend fun setDemoMode(isDemo: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[KEY_IS_DEMO_MODE] = isDemo
        }
    }

    suspend fun setStepGoal(steps: Int) {
        context.dataStore.edit { preferences ->
            preferences[KEY_STEP_GOAL] = steps
        }
    }

    suspend fun setSleepGoal(hours: Int) {
        context.dataStore.edit { preferences ->
            preferences[KEY_SLEEP_GOAL] = hours
        }
    }
}
