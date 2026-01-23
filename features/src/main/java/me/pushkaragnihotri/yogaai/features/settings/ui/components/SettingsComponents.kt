package me.pushkaragnihotri.yogaai.features.settings.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import me.pushkaragnihotri.yogaai.features.R
import me.pushkaragnihotri.yogaai.features.common.ui.DevicePreviews
import me.pushkaragnihotri.yogaai.features.common.ui.theme.YogaAITheme

@Composable
fun SettingsScreenContent(
    themeMode: Int,
    onThemeChange: (Int) -> Unit,
    onDeleteData: () -> Unit,
    onDisconnectWearable: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
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

        SettingsSection(stringResource(R.string.settings_section_source)) {
            Text(stringResource(R.string.settings_source_health_connect))
            Spacer(Modifier.height(8.dp))
            OutlinedButton(onClick = onDisconnectWearable) {
                Text(stringResource(R.string.settings_disconnect))
            }
        }

        HorizontalDivider(Modifier.padding(vertical = 16.dp))

        SettingsSection(stringResource(R.string.settings_section_about)) {
            Text(stringResource(R.string.settings_app_version))
            Text(stringResource(R.string.settings_pilot_build))
        }
    }
}

@Composable
fun SettingsSection(title: String, content: @Composable ColumnScope.() -> Unit) {
    Column {
        Text(title, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
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
            onThemeChange = {},
            onDeleteData = {},
            onDisconnectWearable = {}
        )
    }
}
