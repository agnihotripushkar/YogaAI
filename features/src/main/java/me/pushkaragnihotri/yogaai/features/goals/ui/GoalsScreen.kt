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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoalsScreen(
    viewModel: GoalsViewModel = koinViewModel()
) {
    val stepGoal by viewModel.stepGoal.collectAsState()
    val sleepGoal by viewModel.sleepGoal.collectAsState()
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
                onStepGoalChanged = { viewModel.updateStepGoal(it) },
                onSleepGoalChanged = { viewModel.updateSleepGoal(it) }
            )
        }
    }
}
