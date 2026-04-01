package me.pushkaragnihotri.yogaai.features.settings.ui.components

import android.os.Build
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Policy
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import me.pushkaragnihotri.yogaai.core.HealthConnectManager
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
    val sdkStatus = if (state.sdkAvailable) HealthConnectManager.SDK_AVAILABLE else HealthConnectManager.SDK_UNAVAILABLE

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Profile
        SettingsSection(stringResource(R.string.settings_section_profile)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    Icons.Rounded.Person,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(4.dp)
                )
                Column(Modifier.weight(1f)) {
                    ProfileLine(
                        label = stringResource(R.string.settings_profile_name_label),
                        value = state.userName.ifBlank { stringResource(R.string.settings_profile_not_set) }
                    )
                    Spacer(Modifier.height(8.dp))
                    ProfileLine(
                        label = stringResource(R.string.settings_profile_age_label),
                        value = if (state.userAge > 0) state.userAge.toString()
                        else stringResource(R.string.settings_profile_not_set)
                    )
                    Spacer(Modifier.height(8.dp))
                    ProfileLine(
                        label = stringResource(R.string.settings_profile_level_label),
                        value = state.userLevel.ifBlank { stringResource(R.string.settings_profile_not_set) }
                    )
                }
            }
            Spacer(Modifier.height(12.dp))
            FilledTonalButton(
                onClick = { onAction(SettingsAction.OnProfileEditorOpen) },
                shape = MaterialTheme.shapes.extraLarge,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.settings_profile_edit))
            }
        }

        // Appearance
        SettingsSection(stringResource(R.string.settings_section_appearance)) {
            Text(
                stringResource(R.string.settings_theme),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(10.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf(
                    0 to stringResource(R.string.settings_theme_system),
                    1 to stringResource(R.string.settings_theme_light),
                    2 to stringResource(R.string.settings_theme_dark)
                ).forEach { (mode, label) ->
                    FilterChip(
                        selected = state.themeMode == mode,
                        onClick = { onAction(SettingsAction.OnThemeChange(mode)) },
                        label = { Text(label) },
                        shape = MaterialTheme.shapes.extraLarge
                    )
                }
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                Spacer(Modifier.height(20.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            stringResource(R.string.settings_dynamic_color),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            stringResource(R.string.settings_dynamic_color_desc),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Switch(
                        checked = state.dynamicColor,
                        onCheckedChange = { onAction(SettingsAction.OnDynamicColorChange(it)) }
                    )
                }
            }
        }

        // Health data
        SettingsSection(stringResource(R.string.profile_health_data_header)) {
            if (!state.sdkAvailable) {
                Text(
                    "Health Connect Status: $sdkStatus",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
                Spacer(Modifier.height(8.dp))
            }
            if (state.sdkAvailable) {
                if (state.hasPermissions) {
                    InfoRow(stringResource(R.string.profile_steps_format, state.steps))
                    InfoRow(stringResource(R.string.profile_calories_format, state.calories))
                } else {
                    Text(
                        stringResource(R.string.profile_connect_prompt),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.height(12.dp))
                    FilledTonalButton(
                        onClick = { onAction(SettingsAction.OnConnectClick) },
                        shape = MaterialTheme.shapes.extraLarge
                    ) {
                        Text(stringResource(R.string.profile_connect_button))
                    }
                }
            } else {
                Text(
                    "Health Connect not available.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // About: rate, share, privacy, version
        SettingsSection(stringResource(R.string.settings_section_about)) {
            SettingsNavRow(
                title = stringResource(R.string.settings_rate_app),
                subtitle = stringResource(R.string.settings_rate_app_sub),
                icon = Icons.Rounded.Star,
                onClick = { onAction(SettingsAction.OnRateAppClick) }
            )
            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
            SettingsNavRow(
                title = stringResource(R.string.settings_share_app),
                subtitle = stringResource(R.string.settings_share_app_sub),
                icon = Icons.Rounded.Share,
                onClick = { onAction(SettingsAction.OnShareAppClick) }
            )
            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
            SettingsNavRow(
                title = stringResource(R.string.settings_privacy_policy),
                subtitle = stringResource(R.string.settings_privacy_policy_sub),
                icon = Icons.Rounded.Policy,
                onClick = { onAction(SettingsAction.OnPrivacyPolicyClick) }
            )
            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
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
                colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f))
            )
        }
    }

    if (state.showProfileEditor) {
        ProfileEditorDialog(
            initialName = state.userName,
            initialAge = state.userAge,
            initialLevel = state.userLevel,
            onDismiss = { onAction(SettingsAction.OnProfileEditorDismiss) },
            onSave = { name, age, level ->
                onAction(SettingsAction.OnProfileSave(name, age, level))
            }
        )
    }
}

@Composable
private fun ProfileLine(label: String, value: String) {
    Text(
        text = label,
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
    Text(
        text = value,
        style = MaterialTheme.typography.bodyLarge,
        fontWeight = FontWeight.Medium,
        color = MaterialTheme.colorScheme.onSurface
    )
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
        colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f))
    )
}

@Composable
private fun ProfileEditorDialog(
    initialName: String,
    initialAge: Int,
    initialLevel: String,
    onDismiss: () -> Unit,
    onSave: (String, Int, String) -> Unit
) {
    val beginner = stringResource(R.string.profile_level_beginner)
    val intermediate = stringResource(R.string.profile_level_intermediate)
    val advanced = stringResource(R.string.profile_level_advanced)
    val levels = listOf(beginner, intermediate, advanced)

    var name by remember { mutableStateOf(initialName) }
    var ageText by remember { mutableStateOf(if (initialAge > 0) initialAge.toString() else "") }
    var level by remember {
        mutableStateOf(
            initialLevel.takeIf { it.isNotBlank() } ?: beginner
        )
    }

    LaunchedEffect(initialName, initialAge, initialLevel) {
        name = initialName
        ageText = if (initialAge > 0) initialAge.toString() else ""
        level = when {
            initialLevel.isNotBlank() -> initialLevel
            else -> beginner
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.settings_profile_dialog_title)) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
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
                    val age = ageText.toIntOrNull() ?: 0
                    onSave(name.trim(), age, level)
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
private fun SettingsScreenContentWithPermissionsPreview() {
    YogaAITheme {
        SettingsScreenContent(
            state = SettingsState(
                themeMode = 0,
                sdkAvailable = true,
                hasPermissions = true,
                steps = 8432,
                calories = 420.0,
                language = "English",
                dynamicColor = false,
                userName = "Alex",
                userAge = 28,
                userLevel = "Intermediate",
                appVersionName = "1.0",
                appVersionCode = 1L
            ),
            onAction = {}
        )
    }
}

@DevicePreviews
@Composable
private fun SettingsScreenContentNoPermissionsPreview() {
    YogaAITheme {
        SettingsScreenContent(
            state = SettingsState(
                themeMode = 2,
                sdkAvailable = true,
                hasPermissions = false,
                language = "English",
                dynamicColor = false,
                appVersionName = "1.0",
                appVersionCode = 1L
            ),
            onAction = {}
        )
    }
}
