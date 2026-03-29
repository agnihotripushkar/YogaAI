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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary              = VioletDark,
    onPrimary            = OnVioletContainer,
    primaryContainer     = OnVioletContainer,
    onPrimaryContainer   = VioletDark,
    secondary            = AmberDark,
    onSecondary          = OnAmberContainer,
    secondaryContainer   = OnAmberContainer,
    onSecondaryContainer = AmberDark,
    tertiary             = TealDark,
    onTertiary           = OnTealContainer,
    tertiaryContainer    = OnTealContainer,
    onTertiaryContainer  = TealDark,
    background           = DarkCharcoalBg,
    onBackground         = OnDarkBg,
    surface              = SurfaceDark,
    onSurface            = OnDarkBg,
    surfaceVariant       = SurfaceVariantDark,
    onSurfaceVariant     = OnDarkBg
)

private val LightColorScheme = lightColorScheme(
    primary              = VioletLight,
    onPrimary            = OnViolet,
    primaryContainer     = VioletContainer,
    onPrimaryContainer   = OnVioletContainer,
    secondary            = AmberLight,
    onSecondary          = OnAmber,
    secondaryContainer   = AmberContainer,
    onSecondaryContainer = OnAmberContainer,
    tertiary             = TealLight,
    onTertiary           = OnTeal,
    tertiaryContainer    = TealContainer,
    onTertiaryContainer  = OnTealContainer,
    background           = OffWhiteBg,
    onBackground         = OnLightBg,
    surface              = SurfaceLight,
    onSurface            = OnLightBg,
    surfaceVariant       = SurfaceVariantLight,
    onSurfaceVariant     = OnLightBg
)

@Composable
fun YogaAITheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else      -> LightColorScheme
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
        typography  = Typography,
        shapes      = Shapes,
        content     = content
    )
}
