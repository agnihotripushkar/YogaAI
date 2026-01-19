package me.pushkaragnihotri.yogaai.features.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import me.pushkaragnihotri.yogaai.features.home.ui.HomeScreen
import me.pushkaragnihotri.yogaai.features.profile.ui.ProfileScreen
import me.pushkaragnihotri.yogaai.features.onboarding.ui.OnboardingScreen

@Composable
fun YogaNavigation(
    navController: NavHostController = rememberNavController(),
    startDestination: String = YogaDestinations.HOME_ROUTE
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(YogaDestinations.ONBOARDING_ROUTE) {
            OnboardingScreen(
                onOnboardingComplete = {
                    navController.navigate(YogaDestinations.HOME_ROUTE) {
                        popUpTo(YogaDestinations.ONBOARDING_ROUTE) { inclusive = true }
                    }
                }
            )
        }
        composable(YogaDestinations.HOME_ROUTE) {
            HomeScreen(
                onNavigateToSettings = {
                    navController.navigate(YogaDestinations.SETTINGS_ROUTE)
                }
            )
        }
        composable(YogaDestinations.INSIGHTS_ROUTE) {
            me.pushkaragnihotri.yogaai.features.insights.ui.InsightsScreen(
                onNavigateToDetail = { date ->
                    navController.navigate("insights_detail/$date")
                }
            )
        }
        composable(
            route = YogaDestinations.INSIGHTS_DETAIL_ROUTE,
            arguments = listOf(androidx.navigation.navArgument("date") { type = androidx.navigation.NavType.StringType })
        ) { backStackEntry ->
            val date = backStackEntry.arguments?.getString("date") ?: ""
            me.pushkaragnihotri.yogaai.features.insights.ui.DailyDetailScreen(
                date = date,
                onNavigateUp = { navController.navigateUp() },
                onNavigateToModelInfo = { navController.navigate(YogaDestinations.MODEL_INFO_ROUTE) }
            )
        }
        composable(YogaDestinations.MODEL_INFO_ROUTE) {
            me.pushkaragnihotri.yogaai.features.insights.ui.ModelInfoScreen(
                onNavigateUp = { navController.navigateUp() }
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
        composable(YogaDestinations.PROFILE_ROUTE) {
             ProfileScreen()
        }
    }
}
