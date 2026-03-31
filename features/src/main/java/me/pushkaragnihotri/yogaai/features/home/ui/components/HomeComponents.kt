package me.pushkaragnihotri.yogaai.features.home.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.rounded.DirectionsRun
import androidx.compose.material.icons.rounded.EmojiEvents
import androidx.compose.material.icons.rounded.LocalFireDepartment
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import me.pushkaragnihotri.yogaai.features.home.data.model.RiskLevel
import me.pushkaragnihotri.yogaai.features.home.data.model.RiskPrediction
import me.pushkaragnihotri.yogaai.features.R
import me.pushkaragnihotri.yogaai.core.presentation.UiText
import me.pushkaragnihotri.yogaai.features.common.ui.DevicePreviews
import me.pushkaragnihotri.yogaai.features.home.ui.WellnessUiModel
import me.pushkaragnihotri.yogaai.features.ui.theme.*
import me.pushkaragnihotri.yogaai.features.ui.theme.YogaAITheme
import me.pushkaragnihotri.yogaai.features.home.ui.HomeState
import me.pushkaragnihotri.yogaai.features.common.ui.MascotState
import me.pushkaragnihotri.yogaai.features.common.ui.ZenMascot
import java.time.LocalDate

@Composable
fun HomeScreenContent(
    state: HomeState,
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

        val mascotState = state.riskPrediction?.let {
            if (it.riskLevel == RiskLevel.LOW) MascotState.HAPPY else MascotState.ENCOURAGING
        } ?: MascotState.HAPPY

        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            ZenMascot(state = mascotState)
        }

        Spacer(modifier = Modifier.height(20.dp))

        if (state.sdkAvailable && !state.hasPermissions) {
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

        if (state.isLoading) {
            Box(
                modifier = Modifier.fillMaxWidth().height(200.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        } else {
            DailyWellnessGrid(items = state.wellnessItems)
        }

        Spacer(modifier = Modifier.height(28.dp))

        state.riskPrediction?.let {
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
        rows.forEachIndexed { rowIndex, rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (rowIndex == 0 && rowItems.size == 1) {
                    WellnessCard(
                        item = rowItems.first(),
                        modifier = Modifier.weight(1f).aspectRatio(2f),
                        animationDelayMs = 0
                    )
                } else {
                    rowItems.forEachIndexed { colIndex, item ->
                        WellnessCard(
                            item = item,
                            modifier = Modifier.weight(1f).aspectRatio(1f),
                            animationDelayMs = (rowIndex * 2 + colIndex) * 80
                        )
                    }
                    if (rowItems.size < 2) Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
fun WellnessCard(item: WellnessUiModel, modifier: Modifier = Modifier, animationDelayMs: Int = 0) {
    val animatedProgress = remember { Animatable(0f) }
    val animatedColor by animateColorAsState(
        targetValue = item.color,
        animationSpec = tween(durationMillis = 800)
    )
    val cardAlpha = remember { Animatable(0f) }
    val cardScale = remember { Animatable(0.88f) }

    LaunchedEffect(item.progress) {
        animatedProgress.animateTo(
            targetValue = item.progress,
            animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing)
        )
    }
    LaunchedEffect(Unit) {
        delay(animationDelayMs.toLong())
        launch { cardAlpha.animateTo(1f, tween(350)) }
        cardScale.animateTo(1f, tween(350, easing = FastOutSlowInEasing))
    }

    Card(
        modifier = modifier
            .alpha(cardAlpha.value)
            .scale(cardScale.value),
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
    val context = LocalContext.current
    val riskAlpha  = remember { Animatable(0f) }
    val riskOffset = remember { Animatable(24f) }
    LaunchedEffect(Unit) {
        launch { riskAlpha.animateTo(1f, tween(400)) }
        riskOffset.animateTo(0f, tween(400, easing = FastOutSlowInEasing))
    }

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
        modifier = Modifier
            .fillMaxWidth()
            .alpha(riskAlpha.value)
            .offset(y = riskOffset.value.dp)
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
                text = risk.explanation.asString(context),
                style = MaterialTheme.typography.headlineSmall,
                color = if (isDark) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@DevicePreviews
@Composable
private fun HomeScreenContentPreview() {
    YogaAITheme {
        HomeScreenContent(
            state = HomeState(
                isLoading = false,
                hasPermissions = true,
                sdkAvailable = true,
                riskPrediction = RiskPrediction(
                    riskLevel = RiskLevel.LOW,
                    explanation = UiText.DynamicString("Your stress levels are low and recovery is high. Great day for a workout!"),
                    date = LocalDate.now(),
                    contributingSignals = emptyList()
                ),
                wellnessItems = listOf()
            )
        )
    }
}

@Preview(name = "Permission Banner", showBackground = true)
@Composable
private fun HealthConnectPermissionBannerPreview() {
    YogaAITheme {
        HealthConnectPermissionBanner(onGrantPermissionClick = {})
    }
}

@Preview(name = "Profile Header", showBackground = true)
@Composable
private fun ProfileHeaderPreview() {
    YogaAITheme {
        ProfileHeader()
    }
}

@Preview(name = "Wellness Card — Steps", showBackground = true, widthDp = 180, heightDp = 180)
@Composable
private fun WellnessCardPreview() {
    YogaAITheme {
        WellnessCard(
            item = WellnessUiModel(
                titleRes = R.string.metric_steps,
                value = "8,432",
                icon = Icons.Rounded.DirectionsRun,
                color = Color(0xFF42A5F5),
                progress = 0.72f
            ),
            modifier = Modifier.size(160.dp)
        )
    }
}

@Preview(name = "Wellness Card — Streak", showBackground = true, widthDp = 180, heightDp = 180)
@Composable
private fun WellnessCardWithScorePreview() {
    YogaAITheme {
        WellnessCard(
            item = WellnessUiModel(
                titleRes = R.string.metric_streak,
                value = "5 Days",
                icon = Icons.Rounded.EmojiEvents,
                color = Color(0xFFFFB300),
                progress = 0.5f,
                score = 85
            ),
            modifier = Modifier.size(160.dp)
        )
    }
}

@Preview(name = "Wellness Grid", showBackground = true)
@Composable
private fun DailyWellnessGridPreview() {
    YogaAITheme {
        DailyWellnessGrid(
            items = listOf(
                WellnessUiModel(R.string.metric_streak, "5 Days", Icons.Rounded.EmojiEvents, Color(0xFFFFB300), 0.5f, 85),
                WellnessUiModel(R.string.metric_calories, "420 kcal", Icons.Rounded.LocalFireDepartment, Color(0xFFEF5350), 0.6f),
                WellnessUiModel(R.string.metric_steps, "8,432", Icons.Rounded.DirectionsRun, Color(0xFF42A5F5), 0.72f)
            )
        )
    }
}

@Preview(name = "Risk Card — Low", showBackground = true)
@Composable
private fun RiskCardLowPreview() {
    YogaAITheme {
        RiskCard(
            risk = RiskPrediction(
                riskLevel = RiskLevel.LOW,
                explanation = UiText.DynamicString("Your recovery is excellent. Great conditions for an intense session."),
                date = LocalDate.now(),
                contributingSignals = emptyList()
            )
        )
    }
}

@Preview(name = "Risk Card — Medium", showBackground = true)
@Composable
private fun RiskCardMediumPreview() {
    YogaAITheme {
        RiskCard(
            risk = RiskPrediction(
                riskLevel = RiskLevel.MEDIUM,
                explanation = UiText.DynamicString("Moderate stress detected. Consider a lighter yoga flow today."),
                date = LocalDate.now(),
                contributingSignals = emptyList()
            )
        )
    }
}

@Preview(name = "Risk Card — High", showBackground = true)
@Composable
private fun RiskCardHighPreview() {
    YogaAITheme {
        RiskCard(
            risk = RiskPrediction(
                riskLevel = RiskLevel.HIGH,
                explanation = UiText.DynamicString("High stress and low sleep detected. Restorative poses recommended."),
                date = LocalDate.now(),
                contributingSignals = emptyList()
            )
        )
    }
}
