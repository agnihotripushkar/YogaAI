package me.pushkaragnihotri.yogaai.features.onboarding.ui

sealed interface OnboardingAction {
    data object NextStep : OnboardingAction
    data object PreviousStep : OnboardingAction
    data class GoalSelected(val goal: String) : OnboardingAction
    data class PainToggled(val pain: String) : OnboardingAction
    data object TinderCardAdvanced : OnboardingAction
    data class NameChanged(val name: String) : OnboardingAction
    data class AgeChanged(val age: String) : OnboardingAction
    data class LevelSelected(val level: String) : OnboardingAction
    data class StepGoalChanged(val steps: Int) : OnboardingAction
    data class SleepGoalChanged(val hours: Int) : OnboardingAction
    data class PoseSelected(val poseName: String) : OnboardingAction
    data class CameraPermissionResult(val granted: Boolean) : OnboardingAction
    data class HealthPermissionsResult(val granted: Set<String>) : OnboardingAction
    data object CompleteOnboarding : OnboardingAction
}
