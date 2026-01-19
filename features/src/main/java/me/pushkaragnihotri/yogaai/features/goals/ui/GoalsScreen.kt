package me.pushkaragnihotri.yogaai.features.goals.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import me.pushkaragnihotri.yogaai.features.common.ui.DevicePreviews
import me.pushkaragnihotri.yogaai.features.common.ui.ResponsiveContainer
import org.koin.androidx.compose.koinViewModel
import androidx.compose.ui.res.stringResource
import me.pushkaragnihotri.yogaai.features.R
import kotlin.math.roundToInt

@Composable
fun GoalsScreen(
    viewModel: GoalsViewModel = koinViewModel()
) {
    val stepGoal by viewModel.stepGoal.collectAsState()
    val sleepGoal by viewModel.sleepGoal.collectAsState()
    
    GoalsScreenContent(
        stepGoal = stepGoal,
        sleepGoal = sleepGoal,
        onStepGoalChanged = { viewModel.updateStepGoal(it) },
        onSleepGoalChanged = { viewModel.updateSleepGoal(it) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoalsScreenContent(
    stepGoal: Int,
    sleepGoal: Int,
    onStepGoalChanged: (Int) -> Unit,
    onSleepGoalChanged: (Int) -> Unit
) {
     val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
         modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
         topBar = {
             TopAppBar(
                 title = { Text(stringResource(R.string.title_goals)) },
                 scrollBehavior = scrollBehavior
             )
         }
    ) { padding ->
        ResponsiveContainer(modifier = Modifier.padding(padding)) {
            Column(modifier = Modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState())) {
                // Goals & Reminders Title removed as it's now in AppBar
            
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
    }
}

@DevicePreviews
@Composable
fun GoalsScreenPreview() {
    MaterialTheme {
        GoalsScreenContent(
            stepGoal = 6000,
            sleepGoal = 8,
            onStepGoalChanged = {},
            onSleepGoalChanged = {}
        )
    }
}
