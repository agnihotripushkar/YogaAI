package me.pushkaragnihotri.yogaai.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.foundation.isSystemInDarkTheme
import me.pushkaragnihotri.yogaai.features.common.ui.theme.YogaAITheme as FeaturesYogaAITheme

@Composable
fun YogaAITheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    FeaturesYogaAITheme(
        darkTheme = darkTheme,
        dynamicColor = dynamicColor,
        content = content
    )
}
