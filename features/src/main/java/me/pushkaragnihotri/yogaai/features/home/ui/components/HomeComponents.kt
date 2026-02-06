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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.pushkaragnihotri.yogaai.core.model.RiskLevel
import me.pushkaragnihotri.yogaai.core.model.RiskPrediction
import me.pushkaragnihotri.yogaai.features.R
import me.pushkaragnihotri.yogaai.features.common.ui.DevicePreviews
import me.pushkaragnihotri.yogaai.features.home.model.WellnessUiModel
import me.pushkaragnihotri.yogaai.features.common.ui.theme.*
import me.pushkaragnihotri.yogaai.features.common.ui.theme.YogaAITheme
import me.pushkaragnihotri.yogaai.features.home.ui.HomeUiState
import me.pushkaragnihotri.yogaai.core.model.DailyMetric

@Composable
fun HomeScreenContent(
    uiState: HomeUiState
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp)
    ) {
        // 1. Top Section: Profile Header
        ProfileHeader()

        Spacer(modifier = Modifier.height(24.dp))

        // 2. Daily Wellness Section
        Text(
            text = stringResource(R.string.daily_wellness),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))

        if (uiState.isLoading) {
            Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            DailyWellnessGrid(items = uiState.wellnessItems)
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Recommended Relief
         uiState.riskPrediction?.let { 
             Text(
                text = stringResource(R.string.recommended_relief),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
             )
             Spacer(modifier = Modifier.height(16.dp))
             RiskCard(it) 
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
            
            // Real Profile Pic from Drawable
            Image(
                painter = painterResource(id = R.drawable.profile_pic),
                contentDescription = stringResource(R.string.profile_picture_content_desc),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            )
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column {
                Text(
                    text = stringResource(R.string.greeting_namaste),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    letterSpacing = 1.sp
                )
                Text(
                    text = stringResource(R.string.profile_default_name),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        
        // Notification Icon
        IconButton(
            onClick = { /* TODO */ },
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surfaceContainerHigh, CircleShape)
                .size(48.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = stringResource(R.string.notifications_content_desc),
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun DailyWellnessGrid(items: List<WellnessUiModel>) {
    val rows = items.chunked(2)
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        rows.forEach { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                rowItems.forEach { item ->
                    WellnessCard(
                        item = item,
                        modifier = Modifier.weight(1f)
                    )
                }
                if (rowItems.size < 2) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
fun WellnessCard(
    item: WellnessUiModel,
    modifier: Modifier = Modifier
) {
    // Animation State
    val animatedProgress = remember { Animatable(0f) }
    
    // Dynamic Color Animation
    val animatedColor by animateColorAsState(
        targetValue = item.color,
        animationSpec = tween(durationMillis = 1000)
    )
    
    LaunchedEffect(item.progress) {
        animatedProgress.animateTo(
            targetValue = item.progress,
            animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing)
        )
    }

    Card(
        modifier = modifier.aspectRatio(1f),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = CardBackgroundLight 
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(60.dp)
            ) {
                // Background track
                CircularProgressIndicator(
                    progress = 1f,
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White,
                    strokeWidth = 8.dp,
                    trackColor = Color.Transparent
                )
                
                // Animated Progress with Dynamic Color
                CircularProgressIndicator(
                    progress = animatedProgress.value, 
                    modifier = Modifier.fillMaxSize(),
                    color = animatedColor,
                    strokeWidth = 8.dp
                )
                
                if (item.score != null) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = item.score.toString(),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = item.color
                        )
                        Text(
                            text = stringResource(R.string.metric_score_label),
                            style = MaterialTheme.typography.labelSmall,
                            fontSize = 8.sp,
                            color = Color.Gray
                        )
                    }
                } else {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = null,
                        tint = item.color,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(verticalAlignment = Alignment.CenterVertically) {
               if (item.score == null) {
                   Icon(
                       imageVector = item.icon,
                       contentDescription = null,
                       modifier = Modifier.size(16.dp),
                       tint = Color.Gray 
                   )
                   Spacer(modifier = Modifier.width(4.dp))
               }
               
               Text(
                   text = stringResource(item.titleRes), 
                   style = MaterialTheme.typography.bodyMedium,
                   color = Color.DarkGray
               )
            }
            
            if (item.score == null) {
                 Spacer(modifier = Modifier.height(4.dp))
                 Text(
                     text = item.value,
                     style = MaterialTheme.typography.titleSmall,
                     fontWeight = FontWeight.Bold
                 )
            }
        }
    }
}

@Composable
fun RiskCard(risk: RiskPrediction) {
    val isDark = isSystemInDarkTheme()
    
    val containerColor = when (risk.riskLevel) {
        RiskLevel.LOW -> if (isDark) RiskLowDark else RiskLowLight
        RiskLevel.MEDIUM -> if (isDark) RiskMediumDark else RiskMediumLight
        RiskLevel.HIGH -> MaterialTheme.colorScheme.errorContainer
    }
    val contentColor = when (risk.riskLevel) {
        RiskLevel.LOW -> if (isDark) RiskLowTextDark else RiskLowTextLight
        RiskLevel.MEDIUM -> if (isDark) RiskMediumTextDark else RiskMediumTextLight
        RiskLevel.HIGH -> MaterialTheme.colorScheme.onErrorContainer
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = containerColor),
        shape = RoundedCornerShape(24.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            val riskLevelString = when (risk.riskLevel) {
                RiskLevel.LOW -> stringResource(R.string.risk_level_low)
                RiskLevel.MEDIUM -> stringResource(R.string.risk_level_medium)
                RiskLevel.HIGH -> stringResource(R.string.risk_level_high)
            }
            Text(
                text = "$riskLevelString ${stringResource(R.string.risk_level_suffix)}",
                style = MaterialTheme.typography.labelMedium,
                color = contentColor,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = risk.explanation,
                style = MaterialTheme.typography.headlineSmall,
                color = if (isDark && risk.riskLevel == RiskLevel.HIGH) MaterialTheme.colorScheme.onErrorContainer else if (isDark) Color.White else Color.Black
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
                    date = java.time.LocalDate.now(),
                    contributingSignals = emptyList()
                ),
                wellnessItems = listOf() // Empty for preview or mock it if needed
            )
        )
    }
}
