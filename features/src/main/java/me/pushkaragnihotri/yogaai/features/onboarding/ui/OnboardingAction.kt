package me.pushkaragnihotri.yogaai.features.onboarding.ui

sealed interface OnboardingAction {
    data object OnConsentGranted : OnboardingAction
    data object OnOnboardingComplete : OnboardingAction
    data class OnPermissionsResult(val granted: Set<String>) : OnboardingAction
}
