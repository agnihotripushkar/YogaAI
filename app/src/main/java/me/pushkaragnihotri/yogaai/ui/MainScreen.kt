package me.pushkaragnihotri.yogaai.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import me.pushkaragnihotri.yogaai.core.navigation.YogaBottomBar
import me.pushkaragnihotri.yogaai.features.common.ui.DevicePreviews
import me.pushkaragnihotri.yogaai.features.ui.theme.YogaAITheme
import me.pushkaragnihotri.yogaai.core.navigation.YogaDestinations
import me.pushkaragnihotri.yogaai.core.navigation.YogaNavigation

@Composable
fun MainScreen(
    windowSizeClass: WindowWidthSizeClass,
    finalDestination: String
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val onboardRoutes = listOf(
        YogaDestinations.SPLASH_ROUTE,
        YogaDestinations.ONBOARDING_ROUTE,
        YogaDestinations.CONNECT_ROUTE,
        YogaDestinations.POSE_LIBRARY_ROUTE
    )

    Scaffold(
        bottomBar = {
            if (currentRoute !in onboardRoutes) {
                YogaBottomBar(navController = navController)
            }
        }
    ) { innerPadding ->
        // We need to apply padding to the content to avoid overlap with the bottom bar
        // Since YogaNavigation is the NavHost content, we wrap it in a Box or adjust it
        // Ideally YogaNavigation should take a modifier, but for now we wrap it.
        androidx.compose.foundation.layout.Box(modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding())) {
            YogaNavigation(
                navController = navController,
                windowSizeClass = windowSizeClass,
                finalDestination = finalDestination
            )
        }
    }
}

@DevicePreviews
@Composable
fun MainScreenPreview() {
    YogaAITheme {
        MainScreen(
            windowSizeClass = WindowWidthSizeClass.Compact,
            finalDestination = YogaDestinations.HOME_ROUTE
        )
    }
}
