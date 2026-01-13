package me.pushkaragnihotri.yogaai.features.insights.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import me.pushkaragnihotri.yogaai.core.data.repository.WellnessRepository
import org.koin.compose.koinInject
import java.time.LocalDate

@Composable
fun DailyDetailScreen(
    date: String,
    onNavigateUp: () -> Unit,
    onNavigateToModelInfo: () -> Unit,
    repository: WellnessRepository = koinInject()
) {
    var risk by remember { mutableStateOf<me.pushkaragnihotri.yogaai.core.data.model.RiskPrediction?>(null) }
    
    LaunchedEffect(date) {
        try {
            val localDate = LocalDate.parse(date)
            risk = repository.getRiskByDate(localDate)
        } catch (e: Exception) {
            // handle parse error
        }
    }
    
    DailyDetailScreenContent(
        date = date,
        risk = risk,
        onNavigateUp = onNavigateUp,
        onNavigateToModelInfo = onNavigateToModelInfo
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailyDetailScreenContent(
    date: String,
    risk: me.pushkaragnihotri.yogaai.core.data.model.RiskPrediction?,
    onNavigateUp: () -> Unit,
    onNavigateToModelInfo: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(date) },
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
        ) {
            risk?.let {
                me.pushkaragnihotri.yogaai.features.home.ui.RiskCard(it)
                Spacer(Modifier.height(24.dp))
                Text("Key Signals", style = MaterialTheme.typography.headlineSmall)
                Spacer(Modifier.height(8.dp))
                it.contributingSignals.forEach { signal ->
                    ListItem(
                        headlineContent = { Text(signal) },
                        leadingContent = { Icon(androidx.compose.material.icons.Icons.Rounded.Warning, null) }
                    )
                }
            } ?: Text("Loading or Not Found...")
            
            Spacer(Modifier.weight(1f))
            Button(onClick = onNavigateToModelInfo, modifier = Modifier.fillMaxWidth()) {
                Text("How this model works")
            }
        }
    }
}

@me.pushkaragnihotri.yogaai.features.common.ui.DevicePreviews
@Composable
fun DailyDetailScreenPreview() {
    val dummyRisk = me.pushkaragnihotri.yogaai.core.data.model.RiskPrediction(
        date = java.time.LocalDate.now(),
        riskLevel = me.pushkaragnihotri.yogaai.core.data.model.RiskLevel.MEDIUM,
        explanation = "Moderate Risk",
        contributingSignals = listOf("High Step Count but Low Sleep")
    )
    me.pushkaragnihotri.yogaai.ui.theme.YogaAITheme {
        DailyDetailScreenContent(
            date = "2024-01-01",
            risk = dummyRisk,
            onNavigateUp = {},
            onNavigateToModelInfo = {}
        )
    }
}
