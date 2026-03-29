package me.pushkaragnihotri.yogaai.core.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import me.pushkaragnihotri.yogaai.features.connect.ui.ConnectScreen
import me.pushkaragnihotri.yogaai.features.home.ui.HomeRoot
import me.pushkaragnihotri.yogaai.features.onboarding.ui.OnboardingScreen
import me.pushkaragnihotri.yogaai.features.onboarding.ui.OnboardingViewModel
import me.pushkaragnihotri.yogaai.features.settings.ui.SettingsRoot
import me.pushkaragnihotri.yogaai.features.splash.ui.SplashScreen
import me.pushkaragnihotri.yogaai.features.yoga.ui.PoseResultScreen
import me.pushkaragnihotri.yogaai.features.yoga.ui.YogaDetectorRoot
import org.koin.androidx.compose.koinViewModel

@Composable
fun YogaNavigation(
    navController: NavHostController = rememberNavController(),
    windowSizeClass: WindowWidthSizeClass,
    finalDestination: String
) {
    NavHost(
        navController = navController,
        startDestination = YogaDestinations.SPLASH_ROUTE,
        enterTransition = {
            slideInVertically(initialOffsetY = { 300 }, animationSpec = tween(300)) +
                    fadeIn(animationSpec = tween(300))
        },
        exitTransition = {
            slideOutVertically(targetOffsetY = { -300 }, animationSpec = tween(300)) +
                    fadeOut(animationSpec = tween(300))
        },
        popEnterTransition = {
            slideInVertically(initialOffsetY = { -300 }, animationSpec = tween(300)) +
                    fadeIn(animationSpec = tween(300))
        },
        popExitTransition = {
            slideOutVertically(targetOffsetY = { 300 }, animationSpec = tween(300)) +
                    fadeOut(animationSpec = tween(300))
        }
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
                    viewModel.onAction(me.pushkaragnihotri.yogaai.features.onboarding.ui.OnboardingAction.OnConsentGranted)
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
                    viewModel.onAction(me.pushkaragnihotri.yogaai.features.onboarding.ui.OnboardingAction.OnOnboardingComplete)
                    navController.navigate(YogaDestinations.HOME_ROUTE) {
                        popUpTo(YogaDestinations.CONNECT_ROUTE) { inclusive = true }
                    }
                }
            )
        }

        composable(YogaDestinations.HOME_ROUTE) {
            HomeRoot()
        }

        composable(YogaDestinations.YOGA_DETECTOR_ROUTE) {
            YogaDetectorRoot(
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

        composable(YogaDestinations.SETTINGS_ROUTE) {
            SettingsRoot(onNavigateUp = { navController.navigateUp() })
        }
    }
}
