package me.pushkaragnihotri.yogaai.features.settings.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.health.connect.client.HealthConnectClient
import me.pushkaragnihotri.yogaai.core.HealthConnectManager
import me.pushkaragnihotri.yogaai.features.R
import me.pushkaragnihotri.yogaai.features.common.ui.DevicePreviews
import me.pushkaragnihotri.yogaai.features.common.ui.theme.YogaAITheme

@Composable
fun SettingsScreenContent(
    themeMode: Int,
    sdkStatus: Int,
    hasPermissions: Boolean,
    steps: Long,
    calories: Double,
    onThemeChange: (Int) -> Unit,
    onDeleteData: () -> Unit,
    onConnectClick: () -> Unit,
    onDisconnectWearable: () -> Unit // Kept for backward compat, but onConnectClick handles permissions
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Health Dashboard Section
        SettingsSection(stringResource(R.string.profile_health_data_header)) {
             if (sdkStatus != HealthConnectManager.SDK_AVAILABLE) {
                 Text("Health Connect Status: $sdkStatus", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.error)
                 Spacer(Modifier.height(8.dp))
             }

             when (sdkStatus) {
                HealthConnectManager.SDK_AVAILABLE -> {
                    if (hasPermissions) {
                        InfoRow(stringResource(R.string.profile_steps_format, steps))
                        InfoRow(stringResource(R.string.profile_calories_format, calories))
                    } else {
                        Text(stringResource(R.string.profile_connect_prompt))
                        Button(onClick = onConnectClick) {
                            Text(stringResource(R.string.profile_connect_button))
                        }
                    }
                }
                else -> {
                    Text("Health Connect not available.")
                    // Simplified for brevity, could add the install logic from ProfileComponents here if needed
                }
             }
        }

        HorizontalDivider(Modifier.padding(vertical = 16.dp))

        SettingsSection(stringResource(R.string.settings_section_appearance)) {
            Text(stringResource(R.string.settings_theme), style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(
                    selected = themeMode == 0,
                    onClick = { onThemeChange(0) },
                    label = { Text(stringResource(R.string.settings_theme_system)) }
                )
                FilterChip(
                    selected = themeMode == 1,
                    onClick = { onThemeChange(1) },
                    label = { Text(stringResource(R.string.settings_theme_light)) }
                )
                FilterChip(
                    selected = themeMode == 2,
                    onClick = { onThemeChange(2) },
                    label = { Text(stringResource(R.string.settings_theme_dark)) }
                )
            }
        }

        HorizontalDivider(Modifier.padding(vertical = 16.dp))

        SettingsSection(stringResource(R.string.settings_section_account)) {
             Button(
                 onClick = onDeleteData,
                 colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
             ) {
                 Text(stringResource(R.string.settings_delete_data))
             }
             Text(
                 stringResource(R.string.settings_delete_data_desc),
                 style = MaterialTheme.typography.bodySmall,
                 modifier = Modifier.padding(top = 4.dp)
             )
        }

        HorizontalDivider(Modifier.padding(vertical = 16.dp))

        SettingsSection(stringResource(R.string.settings_section_about)) {
            Text(stringResource(R.string.settings_app_version))
            Text(stringResource(R.string.settings_pilot_build))
        }
    }
}

@Composable
fun InfoRow(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.headlineSmall,
        color = MaterialTheme.colorScheme.primary
    )
}

@Composable
fun SettingsSection(title: String, content: @Composable ColumnScope.() -> Unit) {
    Column {
        Text(title, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.secondary)
        Spacer(Modifier.height(8.dp))
        content()
    }
}

@DevicePreviews
@Composable
fun SettingsScreenPreview() {
    YogaAITheme {
        SettingsScreenContent(
            themeMode = 0,
            sdkStatus = HealthConnectManager.SDK_AVAILABLE,
            hasPermissions = true,
            steps = 5000,
            calories = 300.0,
            onThemeChange = {},
            onDeleteData = {},
            onConnectClick = {},
            onDisconnectWearable = {}
        )
    }
}
