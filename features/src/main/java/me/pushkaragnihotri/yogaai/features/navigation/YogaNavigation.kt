package me.pushkaragnihotri.yogaai.features.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import me.pushkaragnihotri.yogaai.features.classes.ui.ClassesScreen
import me.pushkaragnihotri.yogaai.features.home.ui.HomeScreen
import me.pushkaragnihotri.yogaai.features.profile.ui.ProfileScreen
import me.pushkaragnihotri.yogaai.features.progress.ui.ProgressScreen
import me.pushkaragnihotri.yogaai.features.videoplayer.ui.VideoPlayerScreen

@Composable
fun YogaNavigation(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = YogaDestinations.HOME_ROUTE
    ) {
        composable(YogaDestinations.HOME_ROUTE) {
            HomeScreen(
                onNavigateToClasses = {
                    navController.navigate(YogaDestinations.CLASSES_ROUTE)
                }
            )
        }
        composable(YogaDestinations.CLASSES_ROUTE) {
            ClassesScreen()
        }
        composable(YogaDestinations.VIDEO_PLAYER_ROUTE) {
            VideoPlayerScreen()
        }
        composable(YogaDestinations.PROGRESS_ROUTE) {
            ProgressScreen()
        }
        composable(YogaDestinations.PROFILE_ROUTE) {
            ProfileScreen()
        }
    }
}
