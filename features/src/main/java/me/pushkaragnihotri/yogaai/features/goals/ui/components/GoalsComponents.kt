package me.pushkaragnihotri.yogaai.features.goals.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import me.pushkaragnihotri.yogaai.features.R
import me.pushkaragnihotri.yogaai.features.common.ui.DevicePreviews
import me.pushkaragnihotri.yogaai.features.common.ui.theme.YogaAITheme
import kotlin.math.roundToInt

@Composable
fun GoalsScreenContent(
    stepGoal: Int,
    sleepGoal: Int,
    onStepGoalChanged: (Int) -> Unit,
    onSleepGoalChanged: (Int) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState())) {
        Text(stringResource(R.string.goal_steps_format, stepGoal), style = MaterialTheme.typography.titleMedium)
        Slider(
            value = stepGoal.toFloat(),
            onValueChange = { onStepGoalChanged(it.roundToInt()) },
            valueRange = 1000f..20000f,
            steps = 19
        )
        
        Spacer(Modifier.height(24.dp))
        
        Text(stringResource(R.string.goal_sleep_format, sleepGoal), style = MaterialTheme.typography.titleMedium)
        Slider(
            value = sleepGoal.toFloat(),
            onValueChange = { onSleepGoalChanged(it.roundToInt()) },
            valueRange = 4f..12f,
            steps = 8
        )
        
        Spacer(Modifier.height(24.dp))
        Text(stringResource(R.string.goal_reminders_header), style = MaterialTheme.typography.titleMedium)
        // Placeholder for reminders
        Row(verticalAlignment = Alignment.CenterVertically) {
            Switch(checked = true, onCheckedChange = { /* TODO */ })
            Spacer(Modifier.width(8.dp))
            Text(stringResource(R.string.goal_checkin_reminder))
        }
    }
}

@DevicePreviews
@Composable
fun GoalsScreenPreview() {
    YogaAITheme {
        GoalsScreenContent(
            stepGoal = 6000,
            sleepGoal = 8,
            onStepGoalChanged = {},
            onSleepGoalChanged = {}
        )
    }
}
