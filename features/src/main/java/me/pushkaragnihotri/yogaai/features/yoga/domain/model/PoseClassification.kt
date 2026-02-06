package me.pushkaragnihotri.yogaai.features.yoga.domain.model

data class PoseClassification(
    val poseName: String,
    val confidence: Float,
    val isCorrect: Boolean = false,
    val feedback: String = ""
)
