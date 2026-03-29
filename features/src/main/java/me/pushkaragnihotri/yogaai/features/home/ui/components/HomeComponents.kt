package me.pushkaragnihotri.yogaai.features.home.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.pushkaragnihotri.yogaai.features.home.data.model.RiskLevel
import me.pushkaragnihotri.yogaai.features.home.data.model.RiskPrediction
import me.pushkaragnihotri.yogaai.features.R
import me.pushkaragnihotri.yogaai.features.common.ui.DevicePreviews
import me.pushkaragnihotri.yogaai.features.home.data.model.WellnessUiModel
import me.pushkaragnihotri.yogaai.features.ui.theme.*
import me.pushkaragnihotri.yogaai.features.ui.theme.YogaAITheme
import me.pushkaragnihotri.yogaai.features.home.ui.HomeUiState
import me.pushkaragnihotri.yogaai.features.common.ui.MascotState
import me.pushkaragnihotri.yogaai.features.common.ui.ZenMascot
import java.time.LocalDate

@Composable
fun HomeScreenContent(
    uiState: HomeUiState,
    hasPermissions: Boolean = true,
    sdkAvailable: Boolean = true,
    onGrantPermissionClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
            .padding(top = 20.dp)
    ) {
        ProfileHeader()

        Spacer(modifier = Modifier.height(28.dp))

        val mascotState = uiState.riskPrediction?.let {
            if (it.riskLevel == RiskLevel.LOW) MascotState.HAPPY else MascotState.ENCOURAGING
        } ?: MascotState.HAPPY

        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            ZenMascot(state = mascotState)
        }

        Spacer(modifier = Modifier.height(20.dp))

        if (sdkAvailable && !hasPermissions) {
            HealthConnectPermissionBanner(onGrantPermissionClick = onGrantPermissionClick)
            Spacer(modifier = Modifier.height(20.dp))
        }

        Text(
            text = stringResource(R.string.daily_wellness),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(16.dp))

        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxWidth().height(200.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        } else {
            DailyWellnessGrid(items = uiState.wellnessItems)
        }

        Spacer(modifier = Modifier.height(28.dp))

        uiState.riskPrediction?.let {
            Text(
                text = stringResource(R.string.recommended_relief),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(16.dp))
            RiskCard(it)
        }
    }
}

@Composable
fun HealthConnectPermissionBanner(onGrantPermissionClick: () -> Unit) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        ),
        shape = MaterialTheme.shapes.large,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = stringResource(R.string.profile_connect_prompt),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
            Spacer(modifier = Modifier.height(12.dp))
            FilledTonalButton(
                onClick = onGrantPermissionClick,
                shape = MaterialTheme.shapes.extraLarge
            ) {
                Text(text = stringResource(R.string.profile_connect_button))
            }
        }
    }
}

@Composable
fun ProfileHeader() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            // Avatar with gradient ring
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .background(
                        Brush.linearGradient(
                            listOf(
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.colorScheme.tertiary
                            )
                        ),
                        CircleShape
                    )
                    .padding(3.dp)
                    .clip(CircleShape)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.profile_pic),
                    contentDescription = stringResource(R.string.profile_picture_content_desc),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column {
                Text(
                    text = stringResource(R.string.greeting_namaste),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    letterSpacing = 1.2.sp
                )
                Text(
                    text = stringResource(R.string.profile_default_name),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }

        // Notification button — tonal style
        FilledTonalIconButton(
            onClick = { /* TODO */ },
            modifier = Modifier.size(48.dp),
            shape = MaterialTheme.shapes.medium
        ) {
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = stringResource(R.string.notifications_content_desc),
                tint = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
    }
}

@Composable
fun DailyWellnessGrid(items: List<WellnessUiModel>) {
    val rows = items.chunked(2)
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        rows.forEachIndexed { index, rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (index == 0 && rowItems.size == 1) {
                    WellnessCard(
                        item = rowItems.first(),
                        modifier = Modifier.weight(1f).aspectRatio(2f)
                    )
                } else {
                    rowItems.forEach { item ->
                        WellnessCard(
                            item = item,
                            modifier = Modifier.weight(1f).aspectRatio(1f)
                        )
                    }
                    if (rowItems.size < 2) Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
fun WellnessCard(item: WellnessUiModel, modifier: Modifier = Modifier) {
    val animatedProgress = remember { Animatable(0f) }
    val animatedColor by animateColorAsState(
        targetValue = item.color,
        animationSpec = tween(durationMillis = 800)
    )

    LaunchedEffect(item.progress) {
        animatedProgress.animateTo(
            targetValue = item.progress,
            animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing)
        )
    }

    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.size(64.dp)) {
                CircularProgressIndicator(
                    progress = { 1f },
                    modifier = Modifier.fillMaxSize(),
                    color = animatedColor.copy(alpha = 0.15f),
                    strokeWidth = 7.dp,
                    trackColor = Color.Transparent
                )
                CircularProgressIndicator(
                    progress = { animatedProgress.value },
                    modifier = Modifier.fillMaxSize(),
                    color = animatedColor,
                    strokeWidth = 7.dp,
                    trackColor = Color.Transparent
                )
                if (item.score != null) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = item.score.toString(),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = animatedColor
                        )
                        Text(
                            text = stringResource(R.string.metric_score_label),
                            style = MaterialTheme.typography.labelSmall,
                            fontSize = 8.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = null,
                        tint = animatedColor,
                        modifier = Modifier.size(26.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                if (item.score == null) {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                }
                Text(
                    text = stringResource(item.titleRes),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (item.score == null) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = item.value,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
fun RiskCard(risk: RiskPrediction) {
    val isDark = isSystemInDarkTheme()

    val containerColor = when (risk.riskLevel) {
        RiskLevel.LOW    -> if (isDark) RiskLowDark else RiskLowLight
        RiskLevel.MEDIUM -> if (isDark) RiskMediumDark else RiskMediumLight
        RiskLevel.HIGH   -> MaterialTheme.colorScheme.errorContainer
    }
    val contentColor = when (risk.riskLevel) {
        RiskLevel.LOW    -> if (isDark) RiskLowTextDark else RiskLowTextLight
        RiskLevel.MEDIUM -> if (isDark) RiskMediumTextDark else RiskMediumTextLight
        RiskLevel.HIGH   -> MaterialTheme.colorScheme.onErrorContainer
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = containerColor),
        shape = MaterialTheme.shapes.large,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            val riskLevelString = when (risk.riskLevel) {
                RiskLevel.LOW    -> stringResource(R.string.risk_level_low)
                RiskLevel.MEDIUM -> stringResource(R.string.risk_level_medium)
                RiskLevel.HIGH   -> stringResource(R.string.risk_level_high)
            }
            Surface(
                color = contentColor.copy(alpha = 0.15f),
                shape = MaterialTheme.shapes.extraSmall
            ) {
                Text(
                    text = "$riskLevelString ${stringResource(R.string.risk_level_suffix)}",
                    style = MaterialTheme.typography.labelMedium,
                    color = contentColor,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = risk.explanation,
                style = MaterialTheme.typography.headlineSmall,
                color = if (isDark) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onBackground
            )
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
                    date = LocalDate.now(),
                    contributingSignals = emptyList()
                ),
                wellnessItems = listOf()
            )
        )
    }
}
