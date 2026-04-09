package me.pushkaragnihotri.yogaai.features.history.ui

data class PoseHistoryState(
    val sessions: List<PoseHistoryUiItem> = emptyList(),
    val isLoading: Boolean = true
)

data class PoseHistoryUiItem(
    val poseName: String,
    val dateLabel: String,
    val timeLabel: String,
    val isCompleted: Boolean,
    val attemptCount: Int,
    val durationSeconds: Int
)
