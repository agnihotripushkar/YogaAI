package me.pushkaragnihotri.yogaai.features.yoga.ui

sealed interface YogaDetectorAction {
    data object OnStopClick : YogaDetectorAction
    data object OnToggleMute : YogaDetectorAction
    data object OnSwitchCamera : YogaDetectorAction
}
