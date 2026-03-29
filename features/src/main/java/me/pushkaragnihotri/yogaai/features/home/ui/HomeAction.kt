package me.pushkaragnihotri.yogaai.features.home.ui

sealed interface HomeAction {
    data object OnRefreshClick : HomeAction
    data object OnGrantPermissionClick : HomeAction
    data class OnPermissionsResult(val granted: Set<String>) : HomeAction
    data object OnResumed : HomeAction
}
