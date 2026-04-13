package me.pushkaragnihotri.yogaai.core

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

class UserPreferences(private val context: Context) {
    
    suspend fun setThemeMode(mode: Int) {
        context.dataStore.edit { preferences ->
            preferences[KEY_THEME_MODE] = mode
        }
    }

    suspend fun setLanguage(language: String) {
        context.dataStore.edit { preferences ->
            preferences[KEY_LANGUAGE] = language
        }
    }

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

    suspend fun setUserName(name: String) {
        context.dataStore.edit { preferences ->
            preferences[KEY_USER_NAME] = name
        }
    }

    suspend fun setUserAge(age: Int) {
        context.dataStore.edit { preferences ->
            preferences[KEY_USER_AGE] = age
        }
    }

    suspend fun setUserLevel(level: String) {
        context.dataStore.edit { preferences ->
            preferences[KEY_USER_LEVEL] = level
        }
    }

    suspend fun setColorTheme(theme: String) {
        context.dataStore.edit { it[KEY_COLOR_THEME] = theme }
    }

    suspend fun setAppFont(font: String) {
        context.dataStore.edit { it[KEY_APP_FONT] = font }
    }

    suspend fun setUserSex(sex: String) {
        context.dataStore.edit { it[KEY_USER_SEX] = sex }
    }

    suspend fun setUserHeight(cm: Int) {
        context.dataStore.edit { it[KEY_USER_HEIGHT] = cm }
    }

    suspend fun setUserWeight(kg: Float) {
        context.dataStore.edit { it[KEY_USER_WEIGHT] = kg }
    }

    suspend fun setUserTargetWeight(kg: Float) {
        context.dataStore.edit { it[KEY_USER_TARGET_WEIGHT] = kg }
    }

    suspend fun setDynamicColor(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[KEY_DYNAMIC_COLOR] = enabled
        }
    }

companion object {
        val KEY_CONSENT_GIVEN = booleanPreferencesKey("consent_given")
        val KEY_COLOR_THEME = androidx.datastore.preferences.core.stringPreferencesKey("color_theme")
        val KEY_APP_FONT = androidx.datastore.preferences.core.stringPreferencesKey("app_font")
        val KEY_USER_SEX = androidx.datastore.preferences.core.stringPreferencesKey("user_sex")
        val KEY_USER_HEIGHT = androidx.datastore.preferences.core.intPreferencesKey("user_height")
        val KEY_USER_WEIGHT = androidx.datastore.preferences.core.floatPreferencesKey("user_weight")
        val KEY_USER_TARGET_WEIGHT = androidx.datastore.preferences.core.floatPreferencesKey("user_target_weight")
        val KEY_ONBOARDING_COMPLETED = booleanPreferencesKey("onboarding_completed")
        val KEY_IS_DEMO_MODE = booleanPreferencesKey("is_demo_mode")
        val KEY_STEP_GOAL = androidx.datastore.preferences.core.intPreferencesKey("step_goal")
        val KEY_SLEEP_GOAL = androidx.datastore.preferences.core.intPreferencesKey("sleep_goal")
        val KEY_USER_NAME = androidx.datastore.preferences.core.stringPreferencesKey("user_name")
        val KEY_USER_AGE = androidx.datastore.preferences.core.intPreferencesKey("user_age")
        val KEY_USER_LEVEL = androidx.datastore.preferences.core.stringPreferencesKey("user_level")
        val KEY_THEME_MODE = androidx.datastore.preferences.core.intPreferencesKey("theme_mode") // 0=System, 1=Light, 2=Dark
        val KEY_LANGUAGE = androidx.datastore.preferences.core.stringPreferencesKey("language")
        val KEY_DYNAMIC_COLOR = booleanPreferencesKey("dynamic_color")
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

    val userName: Flow<String> = context.dataStore.data
        .map { preferences -> preferences[KEY_USER_NAME] ?: "" }

    val userAge: Flow<Int> = context.dataStore.data
        .map { preferences -> preferences[KEY_USER_AGE] ?: 0 }

    val userLevel: Flow<String> = context.dataStore.data
        .map { preferences -> preferences[KEY_USER_LEVEL] ?: "Beginner" }

    val themeMode: Flow<Int> = context.dataStore.data
        .map { preferences -> preferences[KEY_THEME_MODE] ?: 0 }

    val language: Flow<String> = context.dataStore.data
        .map { preferences -> preferences[KEY_LANGUAGE] ?: "English" }

    val dynamicColorEnabled: Flow<Boolean> = context.dataStore.data
        .map { preferences -> preferences[KEY_DYNAMIC_COLOR] ?: false }

    val colorTheme: Flow<String> = context.dataStore.data
        .map { it[KEY_COLOR_THEME] ?: "Default" }

    val appFont: Flow<String> = context.dataStore.data
        .map { it[KEY_APP_FONT] ?: "Default" }

    val userSex: Flow<String> = context.dataStore.data
        .map { it[KEY_USER_SEX] ?: "" }

    val userHeight: Flow<Int> = context.dataStore.data
        .map { it[KEY_USER_HEIGHT] ?: 0 }

    val userWeight: Flow<Float> = context.dataStore.data
        .map { it[KEY_USER_WEIGHT] ?: 0f }

    val userTargetWeight: Flow<Float> = context.dataStore.data
        .map { it[KEY_USER_TARGET_WEIGHT] ?: 0f }

}
