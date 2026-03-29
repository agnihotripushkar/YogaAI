package me.pushkaragnihotri.yogaai.features.settings.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import me.pushkaragnihotri.yogaai.core.HealthConnectManager
import me.pushkaragnihotri.yogaai.features.R
import me.pushkaragnihotri.yogaai.features.common.ui.DevicePreviews
import me.pushkaragnihotri.yogaai.features.ui.theme.YogaAITheme

@Composable
fun SettingsScreenContent(
    themeMode: Int,
    sdkStatus: Int,
    hasPermissions: Boolean,
    steps: Long,
    calories: Double,
    language: String,
    onThemeChange: (Int) -> Unit,
    onLanguageChange: (String) -> Unit,
    onDeleteData: () -> Unit,
    onConnectClick: () -> Unit,
    onDisconnectWearable: () -> Unit,
    onPrivacyPolicyClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Health Data Section
        SettingsSection(stringResource(R.string.profile_health_data_header)) {
            if (sdkStatus != HealthConnectManager.SDK_AVAILABLE) {
                Text(
                    "Health Connect Status: $sdkStatus",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
                Spacer(Modifier.height(8.dp))
            }
            when (sdkStatus) {
                HealthConnectManager.SDK_AVAILABLE -> {
                    if (hasPermissions) {
                        InfoRow(stringResource(R.string.profile_steps_format, steps))
                        InfoRow(stringResource(R.string.profile_calories_format, calories))
                    } else {
                        Text(
                            stringResource(R.string.profile_connect_prompt),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(Modifier.height(12.dp))
                        FilledTonalButton(
                            onClick = onConnectClick,
                            shape = MaterialTheme.shapes.extraLarge
                        ) {
                            Text(stringResource(R.string.profile_connect_button))
                        }
                    }
                }
                else -> {
                    Text(
                        "Health Connect not available.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
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
                        selected = themeMode == mode,
                        onClick = { onThemeChange(mode) },
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
            language = "English",
            onThemeChange = {},
            onLanguageChange = {},
            onDeleteData = {},
            onConnectClick = {},
            onDisconnectWearable = {},
            onPrivacyPolicyClick = {}
        )
    }
}
