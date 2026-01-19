package me.pushkaragnihotri.yogaai.features.settings.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import me.pushkaragnihotri.yogaai.features.common.ui.DevicePreviews
import me.pushkaragnihotri.yogaai.features.common.ui.ResponsiveContainer
import me.pushkaragnihotri.yogaai.features.common.ui.theme.YogaAITheme
import org.koin.androidx.compose.koinViewModel
import androidx.compose.ui.res.stringResource
import me.pushkaragnihotri.yogaai.features.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = koinViewModel(),
    onNavigateUp: () -> Unit
) {
    val themeMode by viewModel.themeMode.collectAsState(initial = 0)
    SettingsScreen(
        themeMode = themeMode,
        onThemeChange = viewModel::setTheme,
        onDeleteData = viewModel::deleteData,
        onDisconnectWearable = viewModel::disconnectWearable,
        onNavigateUp = onNavigateUp
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    themeMode: Int,
    onThemeChange: (Int) -> Unit,
    onDeleteData: () -> Unit,
    onDisconnectWearable: () -> Unit,
    onNavigateUp: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.title_settings)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(Icons.AutoMirrored.Rounded.ArrowBack, stringResource(R.string.content_description_back))
                    }
                }
            )
        }
    ) { padding ->
        ResponsiveContainer(
            modifier = Modifier.padding(padding)
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
        SettingsScreen(
            themeMode = 0,
            onThemeChange = {},
            onDeleteData = {},
            onDisconnectWearable = {},
            onNavigateUp = {}
        )
    }
}
