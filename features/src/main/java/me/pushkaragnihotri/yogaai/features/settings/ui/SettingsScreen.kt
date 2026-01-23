package me.pushkaragnihotri.yogaai.features.settings.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import me.pushkaragnihotri.yogaai.features.R
import me.pushkaragnihotri.yogaai.features.common.ui.ResponsiveContainer
import me.pushkaragnihotri.yogaai.features.settings.ui.components.SettingsScreenContent
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = koinViewModel(),
    onNavigateUp: () -> Unit
) {
    val themeMode by viewModel.themeMode.collectAsState(initial = 0)

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
            SettingsScreenContent(
                themeMode = themeMode,
                onThemeChange = viewModel::setTheme,
                onDeleteData = viewModel::deleteData,
                onDisconnectWearable = viewModel::disconnectWearable
            )
        }
    }
}
