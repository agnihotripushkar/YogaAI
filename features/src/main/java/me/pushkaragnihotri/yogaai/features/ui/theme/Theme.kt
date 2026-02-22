package me.pushkaragnihotri.yogaai.features.ui.theme

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
    primary = MintGreenDark,
    onPrimary = OnMintContainer,
    primaryContainer = OnMintContainer,
    onPrimaryContainer = MintGreenDark,
    secondary = SkyBlueDark,
    onSecondary = OnSkyBlueContainer,
    secondaryContainer = OnSkyBlueContainer,
    onSecondaryContainer = SkyBlueDark,
    tertiary = CoralDark,
    onTertiary = OnCoralContainer,
    tertiaryContainer = OnCoralContainer,
    onTertiaryContainer = CoralDark,
    background = DarkCharcoalBg,
    onBackground = OnDarkBg,
    surface = SurfaceDark,
    onSurface = OnDarkBg,
    surfaceVariant = SurfaceVariantDark,
    onSurfaceVariant = OnDarkBg
)

private val LightColorScheme = lightColorScheme(
    primary = MintGreenLight,
    onPrimary = OnMint,
    primaryContainer = MintContainer,
    onPrimaryContainer = OnMintContainer,
    secondary = SkyBlueLight,
    onSecondary = OnSkyBlue,
    secondaryContainer = SkyBlueContainer,
    onSecondaryContainer = OnSkyBlueContainer,
    tertiary = CoralLight,
    onTertiary = OnCoral,
    tertiaryContainer = CoralContainer,
    onTertiaryContainer = OnCoralContainer,
    background = OffWhiteBg,
    onBackground = OnLightBg,
    surface = SurfaceLight,
    onSurface = OnLightBg,
    surfaceVariant = SurfaceVariantLight,
    onSurfaceVariant = OnLightBg
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
            window.statusBarColor = android.graphics.Color.TRANSPARENT
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}