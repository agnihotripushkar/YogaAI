package me.pushkaragnihotri.yogaai.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import me.pushkaragnihotri.yogaai.features.navigation.YogaBottomBar
import me.pushkaragnihotri.yogaai.features.navigation.YogaDestinations
import me.pushkaragnihotri.yogaai.features.navigation.YogaNavigation

@Composable
fun MainScreen(startDestination: String) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            if (currentRoute != YogaDestinations.ONBOARDING_ROUTE) {
                YogaBottomBar(navController = navController)
            }
        }
    ) { innerPadding ->
        // We need to apply padding to the content to avoid overlap with the bottom bar
        // Since YogaNavigation is the NavHost content, we wrap it in a Box or adjust it
        // Ideally YogaNavigation should take a modifier, but for now we wrap it.
        androidx.compose.foundation.layout.Box(modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding())) {
            YogaNavigation(navController = navController, startDestination = startDestination)
        }
    }
}
