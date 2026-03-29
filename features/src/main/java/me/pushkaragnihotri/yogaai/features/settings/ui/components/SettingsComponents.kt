package me.pushkaragnihotri.yogaai.features.settings.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Health Data Section
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

        // Appearance Section
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
        }
    }
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
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
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
                language = "English"
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
                language = "English"
            ),
            onAction = {}
        )
    }
}
