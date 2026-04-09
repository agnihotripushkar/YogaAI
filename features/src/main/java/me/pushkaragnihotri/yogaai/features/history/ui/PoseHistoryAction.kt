package me.pushkaragnihotri.yogaai.features.history.ui

sealed interface PoseHistoryAction {
    data object OnClearHistory : PoseHistoryAction
}
