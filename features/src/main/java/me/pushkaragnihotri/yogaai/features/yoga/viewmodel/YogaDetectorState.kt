package me.pushkaragnihotri.yogaai.features.yoga.ui

import com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarkerResult
import me.pushkaragnihotri.yogaai.core.presentation.UiText

data class YogaDetectorState(
    val poseResult: PoseLandmarkerResult? = null,
    val errorMessage: UiText? = null,
    val poseName: String = "Detecting...",
    val isPoseCorrect: Boolean = false,
    val confidence: Float = 0f,
    val holdTimeSeconds: Int = 0,
    /** Target hold (seconds) before auto-complete; shown as ring progress in HUD. */
    val holdTargetSeconds: Int = 30,
    val isPoseCompleted: Boolean = false,
    val feedback: String = "",
    /** Number of times the hold timer was started (each correct-pose streak = 1 attempt). */
    val attemptCount: Int = 0
)
