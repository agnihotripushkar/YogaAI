package me.pushkaragnihotri.yogaai.features.appearance.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.LightMode
import androidx.compose.material.icons.rounded.SettingsBrightness
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import me.pushkaragnihotri.yogaai.features.ui.theme.CanyonSwatch
import me.pushkaragnihotri.yogaai.features.ui.theme.DancingFamily
import me.pushkaragnihotri.yogaai.features.ui.theme.ForestSwatch
import me.pushkaragnihotri.yogaai.features.ui.theme.HarvestSwatch
import me.pushkaragnihotri.yogaai.features.ui.theme.JakartaFamily
import me.pushkaragnihotri.yogaai.features.ui.theme.NunitoFamily
import me.pushkaragnihotri.yogaai.features.ui.theme.OceanSwatch
import me.pushkaragnihotri.yogaai.features.ui.theme.PlayfairFamily
import me.pushkaragnihotri.yogaai.features.ui.theme.PoppinsFamily
import me.pushkaragnihotri.yogaai.features.ui.theme.RobotoMonoFamily
import me.pushkaragnihotri.yogaai.features.ui.theme.SakuraSwatch
import me.pushkaragnihotri.yogaai.features.ui.theme.SpaceGroteskFamily
import me.pushkaragnihotri.yogaai.features.ui.theme.VioletLight
import org.koin.androidx.compose.koinViewModel

@Composable
fun AppearanceRoot(
    onNavigateUp: () -> Unit,
    viewModel: AppearanceViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    AppearanceScreen(
        state = state,
        onNavigateUp = onNavigateUp,
        onAction = viewModel::onAction
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppearanceScreen(
    state: AppearanceState,
    onNavigateUp: () -> Unit = {},
    onAction: (AppearanceAction) -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Appearance", style = MaterialTheme.typography.titleLarge)
                        Text(
                            "Choose your preferred look",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(padding)
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(28.dp)
        ) {
            // ── App Mode ──────────────────────────────────────────────────
            AppearanceSection(
                icon = Icons.Rounded.LightMode,
                title = "App Mode"
            ) {
                AppModeOption(
                    icon = Icons.Rounded.LightMode,
                    label = "Light",
                    description = "Always use light appearance",
                    selected = state.themeMode == 1,
                    onClick = { onAction(AppearanceAction.OnThemeModeChange(1)) }
                )
                Spacer(Modifier.height(8.dp))
                AppModeOption(
                    icon = Icons.Rounded.DarkMode,
                    label = "Dark",
                    description = "Always use dark appearance",
                    selected = state.themeMode == 2,
                    onClick = { onAction(AppearanceAction.OnThemeModeChange(2)) }
                )
                Spacer(Modifier.height(8.dp))
                AppModeOption(
                    icon = Icons.Rounded.SettingsBrightness,
                    label = "System",
                    description = "Match system appearance",
                    selected = state.themeMode == 0,
                    onClick = { onAction(AppearanceAction.OnThemeModeChange(0)) }
                )
            }

            // ── App Font ──────────────────────────────────────────────────
            AppearanceSection(
                icon = Icons.Rounded.LightMode,
                title = "App Font"
            ) {
                val fontOptions = listOf(
                    "Default"  to FontFamily.Default,
                    "Nunito"   to NunitoFamily,
                    "Poppins"  to PoppinsFamily,
                    "Jakarta"  to JakartaFamily,
                    "Grotesk"  to SpaceGroteskFamily,
                    "Playfair" to PlayfairFamily,
                    "Dancing"  to DancingFamily,
                    "Mono"     to RobotoMonoFamily,
                )
                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    fontOptions.forEach { (key, family) ->
                        FontChip(
                            label = key,
                            fontFamily = family,
                            selected = state.appFont == key,
                            onClick = { onAction(AppearanceAction.OnFontChange(key)) }
                        )
                    }
                }
            }

            // ── Color Theme ───────────────────────────────────────────────
            AppearanceSection(
                icon = Icons.Rounded.LightMode,
                title = "Color Theme"
            ) {
                val themes = listOf(
                    ColorThemeOption("Dynamic",  listOf(Color(0xFF78909C), Color(0xFF90A4AE))),
                    ColorThemeOption("Sakura",   listOf(SakuraSwatch, Color(0xFFF48FB1))),
                    ColorThemeOption("Canyon",   listOf(CanyonSwatch, Color(0xFFFFB68F))),
                    ColorThemeOption("Harvest",  listOf(HarvestSwatch, Color(0xFFD2CA67))),
                    ColorThemeOption("Forest",   listOf(ForestSwatch, Color(0xFF81C784))),
                    ColorThemeOption("Ocean",    listOf(OceanSwatch, Color(0xFF5BD8F5))),
                    ColorThemeOption("Default",  listOf(VioletLight, Color(0xFFB39DDB))),
                )

                val rows = themes.chunked(2)
                rows.forEach { rowItems ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        rowItems.forEach { theme ->
                            ColorThemeTile(
                                option = theme,
                                selected = state.colorTheme == theme.name,
                                onClick = { onAction(AppearanceAction.OnColorThemeChange(theme.name)) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                        // Fill empty slot in last odd row
                        if (rowItems.size == 1) Spacer(Modifier.weight(1f))
                    }
                    Spacer(Modifier.height(12.dp))
                }
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}

// ── Subcomponents ─────────────────────────────────────────────────────────────

@Composable
private fun AppearanceSection(
    icon: ImageVector,
    title: String,
    content: @Composable () -> Unit
) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(bottom = 12.dp)
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
        content()
    }
}

@Composable
private fun AppModeOption(
    icon: ImageVector,
    label: String,
    description: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val borderColor = if (selected)
        MaterialTheme.colorScheme.primary
    else
        MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)

    val bgColor = if (selected)
        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
    else
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.large)
            .background(bgColor)
            .border(
                width = if (selected) 1.5.dp else 1.dp,
                color = borderColor,
                shape = MaterialTheme.shapes.large
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = if (selected) MaterialTheme.colorScheme.primary
                   else MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(22.dp)
        )
        Column(Modifier.weight(1f)) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                color = if (selected) MaterialTheme.colorScheme.onSurface
                        else MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        RadioButton(selected = selected, onClick = onClick)
    }
}

@Composable
private fun FontChip(
    label: String,
    fontFamily: FontFamily,
    selected: Boolean,
    onClick: () -> Unit
) {
    val bgColor = if (selected)
        MaterialTheme.colorScheme.primaryContainer
    else
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
    val textColor = if (selected)
        MaterialTheme.colorScheme.onPrimaryContainer
    else
        MaterialTheme.colorScheme.onSurfaceVariant

    Box(
        modifier = Modifier
            .clip(MaterialTheme.shapes.extraLarge)
            .background(bgColor)
            .border(
                width = if (selected) 1.5.dp else 0.dp,
                color = if (selected) MaterialTheme.colorScheme.primary else Color.Transparent,
                shape = MaterialTheme.shapes.extraLarge
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            fontFamily = fontFamily,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
            style = MaterialTheme.typography.labelLarge,
            color = textColor
        )
    }
}

private data class ColorThemeOption(val name: String, val colors: List<Color>)

@Composable
private fun ColorThemeTile(
    option: ColorThemeOption,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val borderColor = if (selected) MaterialTheme.colorScheme.primary else Color.Transparent
    val bgColor = if (selected)
        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
    else
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)

    Card(
        modifier = modifier
            .aspectRatio(1f)
            .border(
                width = if (selected) 2.dp else 1.dp,
                color = if (selected) borderColor else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f),
                shape = MaterialTheme.shapes.large
            )
            .clickable(onClick = onClick),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = bgColor),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            // Color sphere with radial gradient
            val swatch = option.colors.first()
            val swatchLight = option.colors.getOrElse(1) { swatch }
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(swatchLight, swatch),
                            radius = 80f
                        )
                    )
            ) {
                if (selected) {
                    Icon(
                        Icons.Rounded.Check,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(24.dp)
                    )
                }
            }

            // Name label at bottom
            Text(
                text = option.name,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                color = if (selected) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 10.dp)
            )
        }
    }
}
