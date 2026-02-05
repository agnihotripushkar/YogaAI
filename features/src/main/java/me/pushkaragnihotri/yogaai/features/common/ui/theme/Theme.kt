package me.pushkaragnihotri.yogaai.features.common.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = SageGreenDark,
    onPrimary = OnSageContainer,
    primaryContainer = OnSageContainer,
    onPrimaryContainer = SageGreenDark,
    secondary = LavenderDark,
    onSecondary = OnLavenderContainer,
    secondaryContainer = OnLavenderContainer,
    onSecondaryContainer = LavenderDark,
    tertiary = EarthDark,
    onTertiary = OnEarthContainer,
    tertiaryContainer = OnEarthContainer,
    onTertiaryContainer = EarthDark,
    background = CharcoalBackground,
    onBackground = OnCharcoal,
    surface = CharcoalSurface,
    onSurface = OnCharcoal,
    surfaceVariant = CharcoalSurfaceVariant,
    onSurfaceVariant = OnCharcoal
)

private val LightColorScheme = lightColorScheme(
    primary = SageGreen,
    onPrimary = OnSage,
    primaryContainer = SageContainer,
    onPrimaryContainer = OnSageContainer,
    secondary = Lavender,
    onSecondary = OnLavender,
    secondaryContainer = LavenderContainer,
    onSecondaryContainer = OnLavenderContainer,
    tertiary = Earth,
    onTertiary = OnEarth,
    tertiaryContainer = EarthContainer,
    onTertiaryContainer = OnEarthContainer,
    background = CreamWhite,
    onBackground = OnCream,
    surface = CreamSurface,
    onSurface = OnCream,
    surfaceVariant = CreamSurfaceVariant,
    onSurfaceVariant = OnCream
)

@Composable
fun YogaAITheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}