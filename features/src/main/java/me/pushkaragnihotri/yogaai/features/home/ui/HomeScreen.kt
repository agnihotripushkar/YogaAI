package me.pushkaragnihotri.yogaai.features.home.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import me.pushkaragnihotri.yogaai.core.model.DailyMetric
import me.pushkaragnihotri.yogaai.core.model.RiskLevel
import me.pushkaragnihotri.yogaai.core.model.RiskPrediction
import me.pushkaragnihotri.yogaai.features.R
import me.pushkaragnihotri.yogaai.features.common.ui.DevicePreviews
import me.pushkaragnihotri.yogaai.features.common.ui.theme.YogaAITheme
import me.pushkaragnihotri.yogaai.features.home.ui.components.HomeScreenContent
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    StatelessHomeScreen(uiState)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatelessHomeScreen(
    uiState: HomeUiState
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.home_title)) },
                scrollBehavior = scrollBehavior
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            HomeScreenContent(
                uiState = uiState
            )
        }
    }
}

@DevicePreviews
@Composable
fun HomeScreenPreview() {
    YogaAITheme {
        StatelessHomeScreen(
            uiState = HomeUiState(
                isLoading = false,
                riskPrediction = RiskPrediction(
                    riskLevel = RiskLevel.LOW,
                    explanation = "Your stress levels are low and recovery is high. Great day for a workout!",
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
