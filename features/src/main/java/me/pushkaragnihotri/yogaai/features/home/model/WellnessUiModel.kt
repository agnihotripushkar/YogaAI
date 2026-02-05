package me.pushkaragnihotri.yogaai.features.home.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class WellnessUiModel(
    val titleRes: Int,
    val value: String,
    val icon: ImageVector,
    val color: Color,
    val progress: Float = 0f,
    val score: Int? = null
)
