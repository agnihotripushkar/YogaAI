package me.pushkaragnihotri.yogaai.features.insights.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import me.pushkaragnihotri.yogaai.core.data.model.RiskLevel
import me.pushkaragnihotri.yogaai.core.data.model.RiskPrediction
import me.pushkaragnihotri.yogaai.features.common.ui.DevicePreviews
import me.pushkaragnihotri.yogaai.features.home.ui.RiskCard
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDate
import androidx.compose.ui.res.stringResource
import me.pushkaragnihotri.yogaai.features.R

@Composable
fun InsightsScreen(
    viewModel: InsightsViewModel = koinViewModel(),
    onNavigateToDetail: (String) -> Unit
) {
    val history by viewModel.history.collectAsState()
    InsightsScreenContent(history = history, onNavigateToDetail = onNavigateToDetail)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsightsScreenContent(
    history: List<RiskPrediction>,
    onNavigateToDetail: (String) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                title = { Text(stringResource(R.string.title_insights)) },
                scrollBehavior = scrollBehavior
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(horizontal = 16.dp)) {
           // Title removed
           Spacer(Modifier.height(16.dp))
            
            BoxWithConstraints {
                val isWide = maxWidth > 600.dp
                
                if (isWide) {
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(minSize = 300.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                       items(history.size) { index ->
                            val item = history[index]
                            RiskCardItem(item) { onNavigateToDetail(item.date.toString()) }
                       }
                    }
                } else {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        items(history) { item ->
                             RiskCardItem(item) { onNavigateToDetail(item.date.toString()) }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RiskCardItem(item: RiskPrediction, onClick: () -> Unit) {
    Box(
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        RiskCard(item)
    }
}

@DevicePreviews
@Composable
fun InsightsScreenPreview() {
    val dummyHistory = listOf(
        RiskPrediction(
            date = LocalDate.now(),
            riskLevel = RiskLevel.LOW,
            explanation = "Good",
            contributingSignals = emptyList()
        ),
        RiskPrediction(
            date = LocalDate.now().minusDays(1),
            riskLevel = RiskLevel.HIGH,
            explanation = "Bad sleep",
            contributingSignals = listOf("Sleep < 5h")
        )
    )
    MaterialTheme {
        InsightsScreenContent(history = dummyHistory, onNavigateToDetail = {})
    }
}
