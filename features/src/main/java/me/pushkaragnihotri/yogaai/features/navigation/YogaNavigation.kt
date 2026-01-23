package me.pushkaragnihotri.yogaai.features.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import me.pushkaragnihotri.yogaai.features.home.ui.HomeScreen
import me.pushkaragnihotri.yogaai.features.onboarding.ui.OnboardingScreen
import me.pushkaragnihotri.yogaai.features.splash.ui.SplashScreen
import me.pushkaragnihotri.yogaai.features.consent.ui.ConsentScreen
import me.pushkaragnihotri.yogaai.features.connect.ui.ConnectScreen
import me.pushkaragnihotri.yogaai.features.profilesetup.ui.ProfileSetupScreen
import org.koin.androidx.compose.koinViewModel
import me.pushkaragnihotri.yogaai.features.onboarding.ui.OnboardingViewModel

@Composable
fun YogaNavigation(
    navController: NavHostController = rememberNavController(),
    startDestination: String = YogaDestinations.SPLASH_ROUTE
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(YogaDestinations.SPLASH_ROUTE) {
            SplashScreen(
                onFinished = {
                    navController.navigate(YogaDestinations.ONBOARDING_ROUTE) {
                        popUpTo(YogaDestinations.SPLASH_ROUTE) { inclusive = true }
                    }
                }
            )
        }
        composable(YogaDestinations.ONBOARDING_ROUTE) {
            OnboardingScreen(
                onOnboardingComplete = {
                    navController.navigate(YogaDestinations.CONSENT_ROUTE) {
                        popUpTo(YogaDestinations.ONBOARDING_ROUTE) { inclusive = true }
                    }
                }
            )
        }
        composable(YogaDestinations.CONSENT_ROUTE) {
            val viewModel: OnboardingViewModel = koinViewModel()
            ConsentScreen(
                onConsentGiven = {
                    viewModel.onConsentGranted()
                    navController.navigate(YogaDestinations.CONNECT_ROUTE) {
                        popUpTo(YogaDestinations.CONSENT_ROUTE) { inclusive = true }
                    }
                }
            )
        }
        composable(YogaDestinations.CONNECT_ROUTE) {
            val viewModel: OnboardingViewModel = koinViewModel()
            ConnectScreen(
                viewModel = viewModel,
                onFinished = {
                    navController.navigate(YogaDestinations.PROFILE_SETUP_ROUTE) {
                        popUpTo(YogaDestinations.CONNECT_ROUTE) { inclusive = true }
                    }
                }
            )
        }
        composable(YogaDestinations.PROFILE_SETUP_ROUTE) {
            val viewModel: OnboardingViewModel = koinViewModel()
            ProfileSetupScreen(
                onFinished = { name, age, level ->
                    viewModel.onProfileFinished(name, age, level)
                    navController.navigate(YogaDestinations.HOME_ROUTE) {
                        popUpTo(YogaDestinations.PROFILE_SETUP_ROUTE) { inclusive = true }
                    }
                }
            )
        }
        composable(YogaDestinations.HOME_ROUTE) {
            HomeScreen()
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
