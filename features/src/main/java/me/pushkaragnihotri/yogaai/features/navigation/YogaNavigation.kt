package me.pushkaragnihotri.yogaai.features.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import me.pushkaragnihotri.yogaai.features.home.ui.HomeScreen
import me.pushkaragnihotri.yogaai.features.onboarding.ui.OnboardingScreen
import me.pushkaragnihotri.yogaai.features.splash.ui.SplashScreen

import me.pushkaragnihotri.yogaai.features.connect.ui.ConnectScreen

import org.koin.androidx.compose.koinViewModel
import me.pushkaragnihotri.yogaai.features.onboarding.ui.OnboardingViewModel
import me.pushkaragnihotri.yogaai.features.poseresult.PoseResultScreen
import me.pushkaragnihotri.yogaai.features.yogadetector.YogaDetectorScreen

@Composable
fun YogaNavigation(
    navController: NavHostController = rememberNavController(),
    windowSizeClass: WindowWidthSizeClass,
    finalDestination: String
) {
    NavHost(
        navController = navController,
        startDestination = YogaDestinations.SPLASH_ROUTE
    ) {
        composable(YogaDestinations.SPLASH_ROUTE) {
            SplashScreen(
                onFinished = {
                    navController.navigate(finalDestination) {
                        popUpTo(YogaDestinations.SPLASH_ROUTE) { inclusive = true }
                    }
                }
            )
        }
        composable(YogaDestinations.ONBOARDING_ROUTE) {
            val viewModel: OnboardingViewModel = koinViewModel()
            OnboardingScreen(
                windowSizeClass = windowSizeClass,
                onOnboardingComplete = {
                    viewModel.onConsentGranted()
                    navController.navigate(YogaDestinations.CONNECT_ROUTE) {
                        popUpTo(YogaDestinations.ONBOARDING_ROUTE) { inclusive = true }
                    }
                }
            )
        }
        composable(YogaDestinations.CONNECT_ROUTE) {
            val viewModel: OnboardingViewModel = koinViewModel()
            ConnectScreen(
                viewModel = viewModel,
                onFinished = {
                    viewModel.completeOnboarding()
                    navController.navigate(YogaDestinations.HOME_ROUTE) {
                        popUpTo(YogaDestinations.CONNECT_ROUTE) { inclusive = true }
                    }
                }
            )
        }
        composable(YogaDestinations.HOME_ROUTE) {
            HomeScreen(
                onStartYogaSession = {
                    navController.navigate(YogaDestinations.YOGA_DETECTOR_ROUTE)
                }
            )
        }
        composable(YogaDestinations.YOGA_DETECTOR_ROUTE) {
            YogaDetectorScreen(
                onNavigateToResult = { poseName, duration, feedback ->
                    navController.navigate("pose_result/$poseName/$duration/$feedback")
                }
            )
        }
        composable(
            route = YogaDestinations.POSE_RESULT_ROUTE,
            arguments = listOf(
                navArgument("poseName") { type = NavType.StringType },
                navArgument("duration") { type = NavType.StringType },
                navArgument("feedback") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val poseName = backStackEntry.arguments?.getString("poseName") ?: ""
            val duration = backStackEntry.arguments?.getString("duration") ?: ""
            val feedback = backStackEntry.arguments?.getString("feedback") ?: ""
            PoseResultScreen(
                poseName = poseName,
                duration = duration,
                feedback = feedback,
                onHomeClick = {
                    navController.navigate(YogaDestinations.HOME_ROUTE) {
                        popUpTo(YogaDestinations.HOME_ROUTE) { inclusive = true }
                    }
                }
            )
        }
        composable(YogaDestinations.GOALS_ROUTE) {
            me.pushkaragnihotri.yogaai.features.goals.ui.GoalsScreen()
        }
        composable(YogaDestinations.SETTINGS_ROUTE) {
             me.pushkaragnihotri.yogaai.features.settings.ui.SettingsScreen(
                 onNavigateUp = { navController.navigateUp() }
             )
        }
    }
}
