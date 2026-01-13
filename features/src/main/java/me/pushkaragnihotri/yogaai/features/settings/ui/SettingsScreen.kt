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
import me.pushkaragnihotri.yogaai.features.common.ui.ResponsiveContainer
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = koinViewModel(),
    onNavigateUp: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(Icons.AutoMirrored.Rounded.ArrowBack, "Back")
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
                val themeMode by viewModel.themeMode.collectAsState(initial = 0)
                
                SettingsSection("Appearance") {
                    Text("Theme", style = MaterialTheme.typography.bodyMedium)
                    Spacer(Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        FilterChip(
                            selected = themeMode == 0,
                            onClick = { viewModel.setTheme(0) },
                            label = { Text("System") }
                        )
                        FilterChip(
                            selected = themeMode == 1,
                            onClick = { viewModel.setTheme(1) },
                            label = { Text("Light") }
                        )
                        FilterChip(
                            selected = themeMode == 2,
                            onClick = { viewModel.setTheme(2) },
                            label = { Text("Dark") }
                        )
                    }
                }

                HorizontalDivider(Modifier.padding(vertical = 16.dp))

                SettingsSection("Account & Data") {
                     Button(
                         onClick = { viewModel.deleteData() },
                         colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                     ) {
                         Text("Delete My Data")
                     }
                     Text(
                         "Deletes all local data and resets app.",
                         style = MaterialTheme.typography.bodySmall,
                         modifier = Modifier.padding(top = 4.dp)
                     )
                }
                
                HorizontalDivider(Modifier.padding(vertical = 16.dp))
                
                SettingsSection("Data Source") {
                    Text("Source: Health Connect")
                    Spacer(Modifier.height(8.dp))
                    OutlinedButton(onClick = { viewModel.disconnectWearable() }) {
                        Text("Disconnect")
                    }
                }
                
                HorizontalDivider(Modifier.padding(vertical = 16.dp))
                
                SettingsSection("About") {
                    Text("Wellness Risk App v1.0")
                    Text("Pilot Study Build")
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
