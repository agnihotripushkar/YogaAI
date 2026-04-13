package me.pushkaragnihotri.yogaai.features.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import me.pushkaragnihotri.yogaai.features.R

// ── Local font families (no network, instant) ────────────────────────────

val NunitoFamily = FontFamily(
    Font(R.font.nunito_regular, FontWeight.Normal),
    Font(R.font.nunito_bold,    FontWeight.Bold),
)

val PoppinsFamily = FontFamily(
    Font(R.font.poppins_regular, FontWeight.Normal),
    Font(R.font.poppins_bold,    FontWeight.Bold),
)

val JakartaFamily = FontFamily(
    Font(R.font.plus_jakarta_sans_regular, FontWeight.Normal),
    Font(R.font.plus_jakarta_sans_bold,    FontWeight.Bold),
)

val SpaceGroteskFamily = FontFamily(
    Font(R.font.space_grotesk_regular, FontWeight.Normal),
    Font(R.font.space_grotesk_bold,    FontWeight.Bold),
)

val PlayfairFamily = FontFamily(
    Font(R.font.playfair_display_regular, FontWeight.Normal),
    Font(R.font.playfair_display_bold,    FontWeight.Bold),
)

val DancingFamily = FontFamily(
    Font(R.font.dancing_script_regular, FontWeight.Normal),
    Font(R.font.dancing_script_bold,    FontWeight.Bold),
)

val RobotoMonoFamily = FontFamily(
    Font(R.font.roboto_mono_regular, FontWeight.Normal),
    Font(R.font.roboto_mono_bold,    FontWeight.Bold),
)

/** Builds a full Typography with a single font family applied uniformly. */
fun buildTypography(family: FontFamily) = Typography(
    displayLarge   = TextStyle(fontFamily = family, fontWeight = FontWeight.ExtraBold, fontSize = 57.sp, lineHeight = 64.sp, letterSpacing = (-0.25).sp),
    displayMedium  = TextStyle(fontFamily = family, fontWeight = FontWeight.ExtraBold, fontSize = 45.sp, lineHeight = 52.sp, letterSpacing = 0.sp),
    displaySmall   = TextStyle(fontFamily = family, fontWeight = FontWeight.Bold,      fontSize = 36.sp, lineHeight = 44.sp, letterSpacing = 0.sp),
    headlineLarge  = TextStyle(fontFamily = family, fontWeight = FontWeight.Bold,      fontSize = 32.sp, lineHeight = 40.sp, letterSpacing = 0.sp),
    headlineMedium = TextStyle(fontFamily = family, fontWeight = FontWeight.Bold,      fontSize = 28.sp, lineHeight = 36.sp, letterSpacing = 0.sp),
    headlineSmall  = TextStyle(fontFamily = family, fontWeight = FontWeight.SemiBold,  fontSize = 24.sp, lineHeight = 32.sp, letterSpacing = 0.sp),
    titleLarge     = TextStyle(fontFamily = family, fontWeight = FontWeight.SemiBold,  fontSize = 22.sp, lineHeight = 28.sp, letterSpacing = 0.sp),
    titleMedium    = TextStyle(fontFamily = family, fontWeight = FontWeight.SemiBold,  fontSize = 16.sp, lineHeight = 24.sp, letterSpacing = 0.15.sp),
    titleSmall     = TextStyle(fontFamily = family, fontWeight = FontWeight.Medium,    fontSize = 14.sp, lineHeight = 20.sp, letterSpacing = 0.1.sp),
    bodyLarge      = TextStyle(fontFamily = family, fontWeight = FontWeight.Normal,    fontSize = 16.sp, lineHeight = 24.sp, letterSpacing = 0.5.sp),
    bodyMedium     = TextStyle(fontFamily = family, fontWeight = FontWeight.Normal,    fontSize = 14.sp, lineHeight = 20.sp, letterSpacing = 0.25.sp),
    bodySmall      = TextStyle(fontFamily = family, fontWeight = FontWeight.Normal,    fontSize = 12.sp, lineHeight = 16.sp, letterSpacing = 0.4.sp),
    labelLarge     = TextStyle(fontFamily = family, fontWeight = FontWeight.Bold,      fontSize = 14.sp, lineHeight = 20.sp, letterSpacing = 0.1.sp),
    labelMedium    = TextStyle(fontFamily = family, fontWeight = FontWeight.Bold,      fontSize = 12.sp, lineHeight = 16.sp, letterSpacing = 0.5.sp),
    labelSmall     = TextStyle(fontFamily = family, fontWeight = FontWeight.Medium,    fontSize = 11.sp, lineHeight = 16.sp, letterSpacing = 0.5.sp),
)

/** Use for short celebratory or editorial titles (not full app body). */
val ExpressiveHeroStyle = TextStyle(
    fontFamily = PlayfairFamily,
    fontWeight = FontWeight.Bold,
    fontSize   = 34.sp,
    lineHeight = 40.sp,
    letterSpacing = (-0.5).sp
)

// Default typography (Nunito headlines, Nunito body — all local, no delay)
val Typography = buildTypography(NunitoFamily)
