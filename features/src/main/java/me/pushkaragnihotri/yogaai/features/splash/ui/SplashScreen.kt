package me.pushkaragnihotri.yogaai.features.splash.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.delay
import me.pushkaragnihotri.yogaai.features.splash.ui.components.SplashScreenContent
import androidx.compose.ui.tooling.preview.Preview
import me.pushkaragnihotri.yogaai.features.ui.theme.YogaAITheme

@Composable
fun SplashScreen(onFinished: () -> Unit) {
    LaunchedEffect(Unit) {
        delay(2000)
        onFinished()
    }
    SplashScreenContent()
}

@Preview(name = "Splash Screen")
@Composable
private fun SplashScreenPreview() {
    YogaAITheme {
        SplashScreenContent()
    }
}
