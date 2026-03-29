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
    val isPoseCompleted: Boolean = false,
    val feedback: String = ""
)
