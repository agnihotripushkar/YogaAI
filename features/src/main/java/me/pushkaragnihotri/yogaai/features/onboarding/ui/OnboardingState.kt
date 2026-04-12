package me.pushkaragnihotri.yogaai.features.onboarding.ui

data class OnboardingState(
    val currentStep: Int = 0,
    // Step 1: Goal
    val selectedGoal: String = "",
    // Step 2: Pain points (multi-select)
    val selectedPains: Set<String> = emptySet(),
    // Step 4: Tinder cards
    val tinderIndex: Int = 0,
    // Step 6: Profile
    val userName: String = "",
    val userAge: String = "",
    val selectedLevel: String = "Beginner",
    val stepGoal: Int = 6000,
    val sleepGoal: Int = 8,
    // Step 10: Pose demo selection
    val selectedPose: String = "",
)
