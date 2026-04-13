package me.pushkaragnihotri.yogaai.features.settings.ui.components

import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Feedback
import androidx.compose.material.icons.rounded.Palette
import androidx.compose.material.icons.rounded.Policy
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.pushkaragnihotri.yogaai.features.R
import me.pushkaragnihotri.yogaai.features.common.ui.DevicePreviews
import me.pushkaragnihotri.yogaai.features.settings.ui.SettingsAction
import me.pushkaragnihotri.yogaai.features.settings.ui.SettingsState
import me.pushkaragnihotri.yogaai.features.ui.theme.YogaAITheme

@Composable
fun SettingsScreenContent(
    state: SettingsState,
    onAction: (SettingsAction) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Profile
        SettingsSection(stringResource(R.string.settings_section_profile)) {
            ProfileCard(state = state, onEditClick = { onAction(SettingsAction.OnProfileEditorOpen) })
        }

        // Appearance
        SettingsSection(stringResource(R.string.settings_section_appearance)) {
            SettingsNavRow(
                title = "Appearance",
                subtitle = "Theme, font, and color",
                icon = Icons.Rounded.Palette,
                onClick = { onAction(SettingsAction.OnAppearanceClick) }
            )
        }

        // About
        SettingsSection(stringResource(R.string.settings_section_about)) {
            SettingsNavRow(
                title = stringResource(R.string.settings_feedback),
                subtitle = stringResource(R.string.settings_feedback_sub),
                icon = Icons.Rounded.Feedback,
                onClick = { onAction(SettingsAction.OnFeedbackClick) }
            )
            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
            SettingsNavRow(
                title = stringResource(R.string.settings_share_app),
                subtitle = stringResource(R.string.settings_share_app_sub),
                icon = Icons.Rounded.Share,
                onClick = { onAction(SettingsAction.OnShareAppClick) }
            )
            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
            SettingsNavRow(
                title = stringResource(R.string.settings_rate_app),
                subtitle = stringResource(R.string.settings_rate_app_sub),
                icon = Icons.Rounded.Star,
                onClick = { onAction(SettingsAction.OnRateAppClick) }
            )
            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
            SettingsNavRow(
                title = stringResource(R.string.settings_privacy_policy),
                subtitle = stringResource(R.string.settings_privacy_policy_sub),
                icon = Icons.Rounded.Policy,
                onClick = { onAction(SettingsAction.OnPrivacyPolicyClick) }
            )
            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
            ListItem(
                headlineContent = {
                    Text(
                        stringResource(R.string.settings_version_label),
                        style = MaterialTheme.typography.bodyLarge
                    )
                },
                supportingContent = {
                    Text(
                        stringResource(
                            R.string.settings_version_format,
                            state.appVersionName,
                            state.appVersionCode
                        ),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                colors = ListItemDefaults.colors(containerColor = Color.Transparent)
            )
        }
    }

    if (state.showProfileEditor) {
        ProfileEditorDialog(
            initialName = state.userName,
            initialAge = state.userAge,
            initialSex = state.userSex,
            initialHeight = state.userHeight,
            initialWeight = state.userWeight,
            initialTargetWeight = state.userTargetWeight,
            initialLevel = state.userLevel,
            onDismiss = { onAction(SettingsAction.OnProfileEditorDismiss) },
            onSave = { name, age, sex, height, weight, targetWeight, level ->
                onAction(SettingsAction.OnProfileSave(name, age, sex, height, weight, targetWeight, level))
            }
        )
    }
}

@Composable
private fun ProfileCard(state: SettingsState, onEditClick: () -> Unit) {
    val notSet = stringResource(R.string.settings_profile_not_set)
    val initials = state.userName.trim()
        .split(" ")
        .filter { it.isNotBlank() }
        .take(2)
        .joinToString("") { it.first().uppercaseChar().toString() }
        .ifBlank { "?" }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Avatar
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = initials,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
        Spacer(Modifier.height(12.dp))

        // Name
        Text(
            text = state.userName.ifBlank { notSet },
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(Modifier.height(2.dp))

        // Level + Age badge row
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (state.userLevel.isNotBlank()) {
                LevelBadge(state.userLevel)
            }
            if (state.userAge > 0) {
                Text(
                    text = "${state.userAge} yrs",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            if (state.userSex.isNotBlank()) {
                Text(
                    text = "·",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = state.userSex,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(Modifier.height(20.dp))
        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
        Spacer(Modifier.height(16.dp))

        // Body stats grid — 3 tiles in a row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            StatTile(
                label = "Height",
                value = if (state.userHeight > 0) "${state.userHeight}" else "—",
                unit = if (state.userHeight > 0) "cm" else "",
                modifier = Modifier.weight(1f)
            )
            StatTile(
                label = "Weight",
                value = if (state.userWeight > 0f) "%.1f".format(state.userWeight) else "—",
                unit = if (state.userWeight > 0f) "kg" else "",
                modifier = Modifier.weight(1f)
            )
            StatTile(
                label = "Target",
                value = if (state.userTargetWeight > 0f) "%.1f".format(state.userTargetWeight) else "—",
                unit = if (state.userTargetWeight > 0f) "kg" else "",
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(Modifier.height(16.dp))
        OutlinedButton(
            onClick = onEditClick,
            shape = MaterialTheme.shapes.extraLarge,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                Icons.Rounded.Edit,
                contentDescription = null,
                modifier = Modifier.size(16.dp)
            )
            Spacer(Modifier.width(6.dp))
            Text(stringResource(R.string.settings_profile_edit))
        }
    }
}

@Composable
private fun StatTile(label: String, value: String, unit: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.Center) {
                Text(
                    text = value,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                if (unit.isNotEmpty()) {
                    Spacer(Modifier.width(2.dp))
                    Text(
                        text = unit,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(bottom = 2.dp)
                    )
                }
            }
            Spacer(Modifier.height(2.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun LevelBadge(level: String) {
    val containerColor = when (level) {
        "Beginner" -> MaterialTheme.colorScheme.tertiaryContainer
        "Intermediate" -> MaterialTheme.colorScheme.secondaryContainer
        else -> MaterialTheme.colorScheme.primaryContainer
    }
    val textColor = when (level) {
        "Beginner" -> MaterialTheme.colorScheme.onTertiaryContainer
        "Intermediate" -> MaterialTheme.colorScheme.onSecondaryContainer
        else -> MaterialTheme.colorScheme.onPrimaryContainer
    }
    Box(
        modifier = Modifier
            .clip(MaterialTheme.shapes.extraLarge)
            .background(containerColor)
            .padding(horizontal = 10.dp, vertical = 3.dp)
    ) {
        Text(
            text = level,
            style = MaterialTheme.typography.labelMedium,
            color = textColor,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun SettingsNavRow(
    title: String,
    subtitle: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    ListItem(
        headlineContent = { Text(title, style = MaterialTheme.typography.bodyLarge) },
        supportingContent = { Text(subtitle, style = MaterialTheme.typography.bodySmall) },
        leadingContent = {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        },
        trailingContent = {
            Icon(
                Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        modifier = Modifier.clickable(onClick = onClick),
        colors = ListItemDefaults.colors(containerColor = Color.Transparent)
    )
}

@Composable
private fun ProfileEditorDialog(
    initialName: String,
    initialAge: Int,
    initialSex: String,
    initialHeight: Int,
    initialWeight: Float,
    initialTargetWeight: Float,
    initialLevel: String,
    onDismiss: () -> Unit,
    onSave: (String, Int, String, Int, Float, Float, String) -> Unit
) {
    val beginner = stringResource(R.string.profile_level_beginner)
    val intermediate = stringResource(R.string.profile_level_intermediate)
    val advanced = stringResource(R.string.profile_level_advanced)
    val levels = listOf(beginner, intermediate, advanced)

    var name by remember { mutableStateOf(initialName) }
    var ageText by remember { mutableStateOf(if (initialAge > 0) initialAge.toString() else "") }
    var sex by remember { mutableStateOf(initialSex) }
    var heightText by remember { mutableStateOf(if (initialHeight > 0) initialHeight.toString() else "") }
    var weightText by remember { mutableStateOf(if (initialWeight > 0f) "%.1f".format(initialWeight) else "") }
    var targetWeightText by remember { mutableStateOf(if (initialTargetWeight > 0f) "%.1f".format(initialTargetWeight) else "") }
    var level by remember { mutableStateOf(initialLevel.takeIf { it.isNotBlank() } ?: beginner) }

    LaunchedEffect(initialName, initialAge, initialSex, initialHeight, initialWeight, initialTargetWeight, initialLevel) {
        name = initialName
        ageText = if (initialAge > 0) initialAge.toString() else ""
        sex = initialSex
        heightText = if (initialHeight > 0) initialHeight.toString() else ""
        weightText = if (initialWeight > 0f) "%.1f".format(initialWeight) else ""
        targetWeightText = if (initialTargetWeight > 0f) "%.1f".format(initialTargetWeight) else ""
        level = initialLevel.takeIf { it.isNotBlank() } ?: beginner
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.settings_profile_dialog_title)) },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(stringResource(R.string.settings_profile_name_label)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = ageText,
                    onValueChange = { if (it.all { c -> c.isDigit() } && it.length <= 3) ageText = it },
                    label = { Text(stringResource(R.string.settings_profile_age_label)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    "Sex",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("Male", "Female", "Other").forEach { option ->
                        FilterChip(
                            selected = sex == option,
                            onClick = { sex = option },
                            label = { Text(option) }
                        )
                    }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = heightText,
                        onValueChange = { if (it.all { c -> c.isDigit() } && it.length <= 3) heightText = it },
                        label = { Text("Height (cm)") },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = weightText,
                        onValueChange = { if (it.all { c -> c.isDigit() || c == '.' } && it.length <= 6) weightText = it },
                        label = { Text("Weight (kg)") },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                }
                OutlinedTextField(
                    value = targetWeightText,
                    onValueChange = { if (it.all { c -> c.isDigit() || c == '.' } && it.length <= 6) targetWeightText = it },
                    label = { Text("Target weight (kg)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    stringResource(R.string.settings_profile_level_label),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    levels.forEach { option ->
                        FilterChip(
                            selected = level == option,
                            onClick = { level = option },
                            label = { Text(option) }
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onSave(
                        name.trim(),
                        ageText.toIntOrNull() ?: 0,
                        sex,
                        heightText.toIntOrNull() ?: 0,
                        weightText.toFloatOrNull() ?: 0f,
                        targetWeightText.toFloatOrNull() ?: 0f,
                        level
                    )
                }
            ) {
                Text(stringResource(R.string.settings_profile_save))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.settings_profile_cancel))
            }
        }
    )
}

@Composable
fun InfoRow(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.headlineSmall,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary
    )
}

@Composable
fun SettingsSection(title: String, content: @Composable ColumnScope.() -> Unit) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 4.dp)
        )
        Spacer(Modifier.height(10.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.large,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(
                modifier = Modifier.padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                content = content
            )
        }
    }
}

@Preview(name = "Info Row", showBackground = true)
@Composable
private fun InfoRowPreview() {
    YogaAITheme {
        Column(modifier = Modifier.padding(16.dp)) {
            InfoRow("8,432 steps today")
            InfoRow("420 kcal burned")
        }
    }
}

@Preview(name = "Settings Section", showBackground = true)
@Composable
private fun SettingsSectionPreview() {
    YogaAITheme {
        SettingsSection(title = "Appearance") {
            Text("Example setting content", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@DevicePreviews
@Composable
private fun SettingsScreenContentPreview() {
    YogaAITheme {
        SettingsScreenContent(
            state = SettingsState(
                themeMode = 0,
                language = "English",
                dynamicColor = false,
                userName = "Alex",
                userAge = 28,
                userSex = "Male",
                userHeight = 178,
                userWeight = 75.5f,
                userTargetWeight = 70.0f,
                userLevel = "Intermediate",
                appVersionName = "1.0",
                appVersionCode = 1L
            ),
            onAction = {}
        )
    }
}
