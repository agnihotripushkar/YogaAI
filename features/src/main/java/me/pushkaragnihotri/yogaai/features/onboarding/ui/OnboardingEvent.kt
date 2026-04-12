package me.pushkaragnihotri.yogaai.features.onboarding.ui

sealed interface OnboardingEvent {
    data object NavigateToHome : OnboardingEvent
}
