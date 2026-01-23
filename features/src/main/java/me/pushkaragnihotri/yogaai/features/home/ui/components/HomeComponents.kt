package me.pushkaragnihotri.yogaai.features.home.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import me.pushkaragnihotri.yogaai.core.model.DailyMetric
import me.pushkaragnihotri.yogaai.core.model.RiskLevel
import me.pushkaragnihotri.yogaai.core.model.RiskPrediction
import me.pushkaragnihotri.yogaai.features.R
import me.pushkaragnihotri.yogaai.features.common.ui.DevicePreviews
import me.pushkaragnihotri.yogaai.features.common.ui.theme.YogaAITheme
import me.pushkaragnihotri.yogaai.features.home.ui.HomeUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenContent(
    uiState: HomeUiState
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = stringResource(R.string.greeting),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = stringResource(R.string.wellness_check),
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(24.dp))

        if (uiState.isLoading) {
            Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                val isWide = maxWidth > 600.dp
                
                if (isWide) {
                     Row {
                         Column(modifier = Modifier.weight(1f)) {
                             uiState.riskPrediction?.let { RiskCard(it) }
                         }
                         Spacer(Modifier.width(16.dp))
                         Column(modifier = Modifier.weight(1f)) {
                             MetricsGrid(uiState.metrics)
                         }
                     }
                } else {
                    Column {
                        uiState.riskPrediction?.let { risk ->
                            RiskCard(risk)
                        }
                        Spacer(modifier = Modifier.height(24.dp))
                        MetricsRow(uiState.metrics)
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun RiskCard(risk: RiskPrediction) {
    val isDark = androidx.compose.foundation.isSystemInDarkTheme()
    
    val containerColor = when (risk.riskLevel) {
        RiskLevel.LOW -> if (isDark) Color(0xFF0F522E) else Color(0xFFE6F4EA)
        RiskLevel.MEDIUM -> if (isDark) Color(0xFF5C4200) else Color(0xFFFEF7E0)
        RiskLevel.HIGH -> MaterialTheme.colorScheme.errorContainer
    }
    val contentColor = when (risk.riskLevel) {
        RiskLevel.LOW -> if (isDark) Color(0xFFC3EED0) else Color(0xFF137333)
        RiskLevel.MEDIUM -> if (isDark) Color(0xFFFFDFA6) else Color(0xFFEA8600)
        RiskLevel.HIGH -> MaterialTheme.colorScheme.onErrorContainer
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = containerColor),
        shape = RoundedCornerShape(28.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            val riskLevelString = when (risk.riskLevel) {
                RiskLevel.LOW -> stringResource(R.string.risk_level_low)
                RiskLevel.MEDIUM -> stringResource(R.string.risk_level_medium)
                RiskLevel.HIGH -> stringResource(R.string.risk_level_high)
            }
            Text(
                text = "$riskLevelString ${stringResource(R.string.risk_level_suffix)}",
                style = MaterialTheme.typography.labelMedium,
                color = contentColor,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = risk.explanation,
                style = MaterialTheme.typography.headlineSmall,
                color = if (isDark && risk.riskLevel == RiskLevel.HIGH) MaterialTheme.colorScheme.onErrorContainer else if (isDark) Color.White else Color.Black
            )
        }
    }
}

@Composable
fun MetricsRow(metrics: DailyMetric) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            MetricCard(
                title = stringResource(R.string.metric_steps),
                value = metrics.steps.toString(),
                modifier = Modifier.weight(1f)
            )
            MetricCard(
                title = stringResource(R.string.metric_sleep),
                value = "${metrics.sleepDurationMinutes / 60}h",
                modifier = Modifier.weight(1f)
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            MetricCard(
                title = stringResource(R.string.metric_hr),
                value = "${metrics.restingHeartRate}",
                modifier = Modifier.weight(1f)
            )
            MetricCard(
                title = stringResource(R.string.metric_calories),
                value = String.format("%.0f", metrics.calories),
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun MetricsGrid(metrics: DailyMetric) {
     Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        MetricCard(stringResource(R.string.metric_steps), metrics.steps.toString(), Modifier.fillMaxWidth())
        MetricCard(stringResource(R.string.metric_sleep), "${metrics.sleepDurationMinutes / 60}h", Modifier.fillMaxWidth())
        MetricCard(stringResource(R.string.metric_hr), "${metrics.restingHeartRate}", Modifier.fillMaxWidth())
        MetricCard(stringResource(R.string.metric_calories), String.format("%.0f", metrics.calories), Modifier.fillMaxWidth())
    }
}

@Composable
fun MetricCard(title: String, value: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        ),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = title, style = MaterialTheme.typography.labelMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }
    }
}

@DevicePreviews
@Composable
fun HomeScreenPreview() {
    YogaAITheme {
        HomeScreenContent(
            uiState = HomeUiState(
                isLoading = false,
                riskPrediction = RiskPrediction(
                    riskLevel = RiskLevel.LOW,
                    explanation = "Your stress levels are low and recovery is high. Great day for a workout!",
                    date = java.time.LocalDate.now(),
                    contributingSignals = emptyList()
                ),
                metrics = DailyMetric(
                    steps = 8500,
                    sleepDurationMinutes = 480,
                    restingHeartRate = 62
                )
            )
        )
    }
}
