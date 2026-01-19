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

    data object Insights : BottomNavItem(
        route = YogaDestinations.INSIGHTS_ROUTE,
        selectedIcon = Icons.Rounded.DateRange, // Using DateRange as placeholder for Insights/History
        unselectedIcon = Icons.Outlined.DateRange,
        labelResId = me.pushkaragnihotri.yogaai.features.R.string.nav_insights
    )

    data object Goals : BottomNavItem(
        route = YogaDestinations.GOALS_ROUTE,
        selectedIcon = Icons.Rounded.CheckCircle, // Using CheckCircle for Goals
        unselectedIcon = Icons.Outlined.CheckCircle,
        labelResId = me.pushkaragnihotri.yogaai.features.R.string.nav_goals
    )

    data object Profile : BottomNavItem(
        route = YogaDestinations.PROFILE_ROUTE,
        selectedIcon = Icons.Rounded.Person,
        unselectedIcon = Icons.Outlined.Person,
        labelResId = me.pushkaragnihotri.yogaai.features.R.string.title_profile
    )
}

val bottomNavItems = listOf(
    BottomNavItem.Home,
    BottomNavItem.Insights,
    BottomNavItem.Goals,
    BottomNavItem.Profile
)
