package me.pushkaragnihotri.yogaai.features.insights.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import me.pushkaragnihotri.yogaai.features.home.ui.RiskCard

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
    history: List<me.pushkaragnihotri.yogaai.core.data.model.RiskPrediction>,
    onNavigateToDetail: (String) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    
    Scaffold(
        modifier = Modifier.androidx.compose.ui.input.nestedscroll.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                title = { Text("Insights") },
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
                    androidx.compose.foundation.lazy.grid.LazyVerticalGrid(
                        columns = androidx.compose.foundation.lazy.grid.GridCells.Adaptive(minSize = 300.dp),
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
fun RiskCardItem(item: me.pushkaragnihotri.yogaai.core.data.model.RiskPrediction, onClick: () -> Unit) {
    androidx.compose.foundation.layout.Box(
        modifier = Modifier.androidx.compose.foundation.clickable(onClick = onClick)
    ) {
        me.pushkaragnihotri.yogaai.features.home.ui.RiskCard(item)
    }
}

@me.pushkaragnihotri.yogaai.features.common.ui.DevicePreviews
@Composable
fun InsightsScreenPreview() {
    val dummyHistory = listOf(
        me.pushkaragnihotri.yogaai.core.data.model.RiskPrediction(
            date = java.time.LocalDate.now(),
            riskLevel = me.pushkaragnihotri.yogaai.core.data.model.RiskLevel.LOW,
            explanation = "Good",
            contributingSignals = emptyList()
        ),
        me.pushkaragnihotri.yogaai.core.data.model.RiskPrediction(
            date = java.time.LocalDate.now().minusDays(1),
            riskLevel = me.pushkaragnihotri.yogaai.core.data.model.RiskLevel.HIGH,
            explanation = "Bad sleep",
            contributingSignals = listOf("Sleep < 5h")
        )
    )
    me.pushkaragnihotri.yogaai.ui.theme.YogaAITheme {
        InsightsScreenContent(history = dummyHistory, onNavigateToDetail = {})
    }
}
