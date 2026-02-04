package me.pushkaragnihotri.yogaai.features.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.ui.graphics.vector.ImageVector

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
        labelResId = me.pushkaragnihotri.yogaai.features.R.string.nav_home
    )

    data object Goals : BottomNavItem(
        route = YogaDestinations.GOALS_ROUTE,
        selectedIcon = Icons.Rounded.CheckCircle,
        unselectedIcon = Icons.Outlined.CheckCircle,
        labelResId = me.pushkaragnihotri.yogaai.features.R.string.nav_goals
    )

    data object Settings : BottomNavItem(
        route = YogaDestinations.SETTINGS_ROUTE,
        selectedIcon = Icons.Rounded.Settings,
        unselectedIcon = Icons.Outlined.Settings,
        labelResId = me.pushkaragnihotri.yogaai.features.R.string.title_settings
    )
}

val bottomNavItems = listOf(
    BottomNavItem.Home,
    BottomNavItem.Goals,
    BottomNavItem.Settings
)
