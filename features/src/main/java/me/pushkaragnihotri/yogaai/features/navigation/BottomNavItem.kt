package me.pushkaragnihotri.yogaai.features.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val label: String
) {
    data object Home : BottomNavItem(
        route = YogaDestinations.HOME_ROUTE,
        selectedIcon = Icons.Rounded.Home,
        unselectedIcon = Icons.Outlined.Home,
        label = "Home"
    )

    data object Classes : BottomNavItem(
        route = YogaDestinations.CLASSES_ROUTE,
        selectedIcon = Icons.Rounded.PlayArrow,
        unselectedIcon = Icons.Outlined.PlayArrow,
        label = "Classes"
    )

    data object Progress : BottomNavItem(
        route = YogaDestinations.PROGRESS_ROUTE,
        selectedIcon = Icons.Rounded.DateRange,
        unselectedIcon = Icons.Outlined.DateRange,
        label = "Progress"
    )

    data object Profile : BottomNavItem(
        route = YogaDestinations.PROFILE_ROUTE,
        selectedIcon = Icons.Rounded.Person,
        unselectedIcon = Icons.Outlined.Person,
        label = "Profile"
    )
}

val bottomNavItems = listOf(
    BottomNavItem.Home,
    BottomNavItem.Classes,
    BottomNavItem.Progress,
    BottomNavItem.Profile
)
