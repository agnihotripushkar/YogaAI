package me.pushkaragnihotri.yogaai.features.history.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.HistoryEdu
import androidx.compose.material.icons.outlined.TimerOff
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import me.pushkaragnihotri.yogaai.features.R
import me.pushkaragnihotri.yogaai.features.ui.theme.BrightGreen
import me.pushkaragnihotri.yogaai.features.ui.theme.YogaAITheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun PoseHistoryRoot(
    viewModel: PoseHistoryViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    PoseHistoryScreen(state = state, onAction = viewModel::onAction)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PoseHistoryScreen(
    state: PoseHistoryState,
    onAction: (PoseHistoryAction) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.history_title),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when {
                state.isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                state.sessions.isEmpty() -> {
                    EmptyHistoryContent(modifier = Modifier.align(Alignment.Center))
                }
                else -> {
                    LazyColumn(
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(state.sessions) { item ->
                            PoseHistoryItem(item = item)
                        }
                        item { Spacer(modifier = Modifier.height(16.dp)) }
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyHistoryContent(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            imageVector = Icons.Outlined.HistoryEdu,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
        )
        Text(
            text = stringResource(R.string.history_empty_title),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = stringResource(R.string.history_empty_desc),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun PoseHistoryItem(item: PoseHistoryUiItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Status icon box
            PoseStatusIcon(isCompleted = item.isCompleted)

            // Pose info
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.poseName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "${item.dateLabel}  ·  ${item.timeLabel}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    DurationChip(seconds = item.durationSeconds)
                    StatusChip(isCompleted = item.isCompleted)
                }
            }

            // Attempts badge
            AttemptsBadge(count = item.attemptCount)
        }
    }
}

@Composable
private fun PoseStatusIcon(isCompleted: Boolean) {
    val bgColor = if (isCompleted) BrightGreen.copy(alpha = 0.15f)
    else MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.4f)
    val iconColor = if (isCompleted) BrightGreen
    else MaterialTheme.colorScheme.error

    Box(
        modifier = Modifier
            .size(52.dp)
            .background(bgColor, RoundedCornerShape(14.dp)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = if (isCompleted) Icons.Outlined.CheckCircle else Icons.Outlined.TimerOff,
            contentDescription = null,
            tint = iconColor,
            modifier = Modifier.size(28.dp)
        )
    }
}

@Composable
private fun DurationChip(seconds: Int) {
    Surface(
        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f),
        shape = MaterialTheme.shapes.extraSmall
    ) {
        Text(
            text = "${seconds}s",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
        )
    }
}

@Composable
private fun StatusChip(isCompleted: Boolean) {
    val bgColor = if (isCompleted) BrightGreen.copy(alpha = 0.15f)
    else MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.5f)
    val textColor = if (isCompleted) BrightGreen
    else MaterialTheme.colorScheme.error

    Surface(color = bgColor, shape = MaterialTheme.shapes.extraSmall) {
        Text(
            text = stringResource(if (isCompleted) R.string.history_completed else R.string.history_stopped),
            style = MaterialTheme.typography.labelSmall,
            color = textColor,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
        )
    }
}

@Composable
private fun AttemptsBadge(count: Int) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "$count",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = if (count == 1) stringResource(R.string.history_attempt_single)
            else stringResource(R.string.history_attempts_format, count),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PoseHistoryScreenPreview() {
    YogaAITheme {
        PoseHistoryScreen(
            state = PoseHistoryState(
                isLoading = false,
                sessions = listOf(
                    PoseHistoryUiItem(
                        poseName = "Warrior II",
                        dateLabel = "Today",
                        timeLabel = "09:42 AM",
                        isCompleted = true,
                        attemptCount = 3,
                        durationSeconds = 30
                    ),
                    PoseHistoryUiItem(
                        poseName = "Tree Pose",
                        dateLabel = "Today",
                        timeLabel = "08:15 AM",
                        isCompleted = false,
                        attemptCount = 1,
                        durationSeconds = 12
                    ),
                    PoseHistoryUiItem(
                        poseName = "Plank",
                        dateLabel = "Yesterday",
                        timeLabel = "07:30 PM",
                        isCompleted = true,
                        attemptCount = 2,
                        durationSeconds = 30
                    )
                )
            ),
            onAction = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PoseHistoryEmptyPreview() {
    YogaAITheme {
        PoseHistoryScreen(
            state = PoseHistoryState(isLoading = false, sessions = emptyList()),
            onAction = {}
        )
    }
}
