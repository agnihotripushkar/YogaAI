package me.pushkaragnihotri.yogaai.core.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.rounded.SelfImprovement
import androidx.compose.material.icons.outlined.SelfImprovement
import androidx.compose.ui.graphics.vector.ImageVector
import me.pushkaragnihotri.yogaai.features.R

sealed class BottomNavItem(
    val route: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val labelResId: Int
) {
    data object Home : BottomNavItem(
        route = YogaDestinations.HOME_ROUTE,
        selectedIcon = Icons.Rounded.Home,
        unselectedIcon = Icons.Outlined.Home,
        labelResId = R.string.nav_home
    )

    data object Pose : BottomNavItem(
        route = YogaDestinations.YOGA_DETECTOR_ROUTE,
        selectedIcon = Icons.Rounded.SelfImprovement,
        unselectedIcon = Icons.Outlined.SelfImprovement,
        labelResId = R.string.title_yoga_detector
    )

    data object Settings : BottomNavItem(
        route = YogaDestinations.SETTINGS_ROUTE,
        selectedIcon = Icons.Rounded.Settings,
        unselectedIcon = Icons.Outlined.Settings,
        labelResId = R.string.title_settings
    )
}

val bottomNavItems = listOf(
    BottomNavItem.Home,
    BottomNavItem.Pose,
    BottomNavItem.Settings
)
