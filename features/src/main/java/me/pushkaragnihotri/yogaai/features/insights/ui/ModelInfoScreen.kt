package me.pushkaragnihotri.yogaai.features.insights.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModelInfoScreen(onNavigateUp: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                 title = { Text("Model Info") },
                 navigationIcon = {
                     IconButton(onClick = onNavigateUp) {
                         Icon(androidx.compose.material.icons.Icons.AutoMirrored.Rounded.ArrowBack, "Back")
                     }
                 }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text("About the Algorithm", style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(24.dp))
            
            InfoSection("Type", "Lightweight heuristic & timeseries analysis.")
            InfoSection("Input", "Steps, Sleep Duration, Resting Heart Rate.")
            InfoSection("Limitations", "Pilot study build. Predictions are estimates, not diagnoses.")
        }
    }
}

@Composable
fun InfoSection(title: String, body: String) {
    Column(modifier = Modifier.padding(bottom = 16.dp)) {
        Text(title, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
        Text(body, style = MaterialTheme.typography.bodyLarge)
    }
}

@me.pushkaragnihotri.yogaai.features.common.ui.DevicePreviews
@Composable
fun ModelInfoScreenPreview() {
    me.pushkaragnihotri.yogaai.ui.theme.YogaAITheme {
        ModelInfoScreen(onNavigateUp = {})
    }
}
