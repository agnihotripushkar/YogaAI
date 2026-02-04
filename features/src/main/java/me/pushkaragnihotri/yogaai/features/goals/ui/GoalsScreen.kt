package me.pushkaragnihotri.yogaai.features.goals.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import me.pushkaragnihotri.yogaai.features.R
import me.pushkaragnihotri.yogaai.features.common.ui.ResponsiveContainer
import me.pushkaragnihotri.yogaai.features.goals.ui.components.GoalsScreenContent
import org.koin.androidx.compose.koinViewModel
import androidx.compose.ui.tooling.preview.Preview
import me.pushkaragnihotri.yogaai.features.common.ui.theme.YogaAITheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoalsScreen(
    viewModel: GoalsViewModel = koinViewModel()
) {
    val stepGoal by viewModel.stepGoal.collectAsState()
    val sleepGoal by viewModel.sleepGoal.collectAsState()
    
    GoalsScreenScaffold(
        stepGoal = stepGoal,
        sleepGoal = sleepGoal,
        onStepGoalChanged = { viewModel.updateStepGoal(it) },
        onSleepGoalChanged = { viewModel.updateSleepGoal(it) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoalsScreenScaffold(
    stepGoal: Int,
    sleepGoal: Int,
    onStepGoalChanged: (Int) -> Unit,
    onSleepGoalChanged: (Int) -> Unit,
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
            GoalsScreenContent(
                stepGoal = stepGoal,
                sleepGoal = sleepGoal,
                onStepGoalChanged = onStepGoalChanged,
                onSleepGoalChanged = onSleepGoalChanged
            )
        }
    }
}

@Preview
@Composable
fun GoalsScreenPreview() {
    YogaAITheme {
        GoalsScreenScaffold(
            stepGoal = 5000,
            sleepGoal = 8,
            onStepGoalChanged = {},
            onSleepGoalChanged = {}
        )
    }
}
