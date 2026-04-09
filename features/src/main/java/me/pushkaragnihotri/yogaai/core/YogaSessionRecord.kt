package me.pushkaragnihotri.yogaai.core

import kotlinx.serialization.Serializable

@Serializable
data class YogaSessionRecord(
    val poseName: String,
    val durationSeconds: Int,
    val completedAtEpochMs: Long,
    val isCompleted: Boolean = false,
    val attemptCount: Int = 1
)
