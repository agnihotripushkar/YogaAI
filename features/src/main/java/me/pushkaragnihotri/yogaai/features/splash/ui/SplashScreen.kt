package me.pushkaragnihotri.yogaai.features.splash.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.delay
import me.pushkaragnihotri.yogaai.features.splash.ui.components.SplashScreenContent

@Composable
fun SplashScreen(onFinished: () -> Unit) {
    LaunchedEffect(Unit) {
        delay(2000)
        onFinished()
    }
    SplashScreenContent()
}
