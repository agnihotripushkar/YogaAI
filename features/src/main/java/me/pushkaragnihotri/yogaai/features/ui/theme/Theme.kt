package me.pushkaragnihotri.yogaai.features.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontFamily
import androidx.core.view.WindowCompat

// ── Default (Violet / Amber / Teal) ──────────────────────────────────────
private val DefaultDark = darkColorScheme(
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

private val DefaultLight = lightColorScheme(
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

// ── Sakura ────────────────────────────────────────────────────────────────
private val SakuraLightScheme = lightColorScheme(
    primary            = SakuraLight,
    onPrimary          = OnViolet,
    primaryContainer   = SakuraContainer,
    onPrimaryContainer = OnSakuraContainer,
    background         = OffWhiteBg,
    onBackground       = OnLightBg,
    surface            = SurfaceLight,
    onSurface          = OnLightBg,
    surfaceVariant     = SurfaceVariantLight,
    onSurfaceVariant   = OnLightBg
)
private val SakuraDarkScheme = darkColorScheme(
    primary            = SakuraDark,
    onPrimary          = OnSakuraContainer,
    primaryContainer   = OnSakuraContainer,
    onPrimaryContainer = SakuraDark,
    background         = DarkCharcoalBg,
    onBackground       = OnDarkBg,
    surface            = SurfaceDark,
    onSurface          = OnDarkBg,
    surfaceVariant     = SurfaceVariantDark,
    onSurfaceVariant   = OnDarkBg
)

// ── Canyon ────────────────────────────────────────────────────────────────
private val CanyonLightScheme = lightColorScheme(
    primary            = CanyonLight,
    onPrimary          = OnViolet,
    primaryContainer   = CanyonContainer,
    onPrimaryContainer = OnCanyonContainer,
    background         = OffWhiteBg,
    onBackground       = OnLightBg,
    surface            = SurfaceLight,
    onSurface          = OnLightBg,
    surfaceVariant     = SurfaceVariantLight,
    onSurfaceVariant   = OnLightBg
)
private val CanyonDarkScheme = darkColorScheme(
    primary            = CanyonDark,
    onPrimary          = OnCanyonContainer,
    primaryContainer   = OnCanyonContainer,
    onPrimaryContainer = CanyonDark,
    background         = DarkCharcoalBg,
    onBackground       = OnDarkBg,
    surface            = SurfaceDark,
    onSurface          = OnDarkBg,
    surfaceVariant     = SurfaceVariantDark,
    onSurfaceVariant   = OnDarkBg
)

// ── Harvest ───────────────────────────────────────────────────────────────
private val HarvestLightScheme = lightColorScheme(
    primary            = HarvestLight,
    onPrimary          = OnViolet,
    primaryContainer   = HarvestContainer,
    onPrimaryContainer = OnHarvestContainer,
    background         = OffWhiteBg,
    onBackground       = OnLightBg,
    surface            = SurfaceLight,
    onSurface          = OnLightBg,
    surfaceVariant     = SurfaceVariantLight,
    onSurfaceVariant   = OnLightBg
)
private val HarvestDarkScheme = darkColorScheme(
    primary            = HarvestDark,
    onPrimary          = OnHarvestContainer,
    primaryContainer   = OnHarvestContainer,
    onPrimaryContainer = HarvestDark,
    background         = DarkCharcoalBg,
    onBackground       = OnDarkBg,
    surface            = SurfaceDark,
    onSurface          = OnDarkBg,
    surfaceVariant     = SurfaceVariantDark,
    onSurfaceVariant   = OnDarkBg
)

// ── Forest ────────────────────────────────────────────────────────────────
private val ForestLightScheme = lightColorScheme(
    primary            = ForestLight,
    onPrimary          = OnViolet,
    primaryContainer   = ForestContainer,
    onPrimaryContainer = OnForestContainer,
    background         = OffWhiteBg,
    onBackground       = OnLightBg,
    surface            = SurfaceLight,
    onSurface          = OnLightBg,
    surfaceVariant     = SurfaceVariantLight,
    onSurfaceVariant   = OnLightBg
)
private val ForestDarkScheme = darkColorScheme(
    primary            = ForestDark,
    onPrimary          = OnForestContainer,
    primaryContainer   = OnForestContainer,
    onPrimaryContainer = ForestDark,
    background         = DarkCharcoalBg,
    onBackground       = OnDarkBg,
    surface            = SurfaceDark,
    onSurface          = OnDarkBg,
    surfaceVariant     = SurfaceVariantDark,
    onSurfaceVariant   = OnDarkBg
)

// ── Ocean ─────────────────────────────────────────────────────────────────
private val OceanLightScheme = lightColorScheme(
    primary            = OceanLight,
    onPrimary          = OnViolet,
    primaryContainer   = OceanContainer,
    onPrimaryContainer = OnOceanContainer,
    background         = OffWhiteBg,
    onBackground       = OnLightBg,
    surface            = SurfaceLight,
    onSurface          = OnLightBg,
    surfaceVariant     = SurfaceVariantLight,
    onSurfaceVariant   = OnLightBg
)
private val OceanDarkScheme = darkColorScheme(
    primary            = OceanDark,
    onPrimary          = OnOceanContainer,
    primaryContainer   = OnOceanContainer,
    onPrimaryContainer = OceanDark,
    background         = DarkCharcoalBg,
    onBackground       = OnDarkBg,
    surface            = SurfaceDark,
    onSurface          = OnDarkBg,
    surfaceVariant     = SurfaceVariantDark,
    onSurfaceVariant   = OnDarkBg
)

fun resolveColorScheme(
    colorTheme: String,
    darkTheme: Boolean,
    dynamicColor: Boolean,
    context: android.content.Context
): ColorScheme {
    if (dynamicColor && colorTheme == "Dynamic" && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        return if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
    }
    return when (colorTheme) {
        "Sakura"  -> if (darkTheme) SakuraDarkScheme  else SakuraLightScheme
        "Canyon"  -> if (darkTheme) CanyonDarkScheme  else CanyonLightScheme
        "Harvest" -> if (darkTheme) HarvestDarkScheme else HarvestLightScheme
        "Forest"  -> if (darkTheme) ForestDarkScheme  else ForestLightScheme
        "Ocean"   -> if (darkTheme) OceanDarkScheme   else OceanLightScheme
        else      -> if (darkTheme) DefaultDark       else DefaultLight
    }
}

fun resolveTypography(appFont: String) = when (appFont) {
    "Nunito"   -> buildTypography(NunitoFamily)
    "Poppins"  -> buildTypography(PoppinsFamily)
    "Jakarta"  -> buildTypography(JakartaFamily)
    "Grotesk"  -> buildTypography(SpaceGroteskFamily)
    "Playfair" -> buildTypography(PlayfairFamily)
    "Dancing"  -> buildTypography(DancingFamily)
    "Mono"     -> buildTypography(RobotoMonoFamily)
    else       -> Typography // Default = Nunito, local, no delay
}

@Composable
fun YogaAITheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    colorTheme: String = "Default",
    appFont: String = "Default",
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val colorScheme = resolveColorScheme(colorTheme, darkTheme, dynamicColor, context)
    val typography = resolveTypography(appFont)

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
        typography  = typography,
        shapes      = Shapes,
        content     = content
    )
}
