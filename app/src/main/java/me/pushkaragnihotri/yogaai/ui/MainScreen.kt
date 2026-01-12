package me.pushkaragnihotri.yogaai.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import me.pushkaragnihotri.yogaai.features.navigation.YogaBottomBar
import me.pushkaragnihotri.yogaai.features.navigation.YogaNavigation

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            YogaBottomBar(navController = navController)
        }
    ) { innerPadding ->
        // We need to apply padding to the content to avoid overlap with the bottom bar
        // Since YogaNavigation is the NavHost content, we wrap it in a Box or adjust it
        // Ideally YogaNavigation should take a modifier, but for now we wrap it.
        androidx.compose.foundation.layout.Box(modifier = Modifier.padding(innerPadding)) {
            YogaNavigation(navController = navController)
        }
    }
}
