package me.pushkaragnihotri.yogaai.features.yoga.ui

import android.net.Uri
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.res.stringResource
import me.pushkaragnihotri.yogaai.core.UserPreferences
import me.pushkaragnihotri.yogaai.features.ui.theme.ExpressiveHeroStyle
import me.pushkaragnihotri.yogaai.features.ui.theme.YogaAITheme
import org.koin.core.context.GlobalContext
import me.pushkaragnihotri.yogaai.features.yoga.domain.model.PoseRepository
import me.pushkaragnihotri.yogaai.features.R
import me.pushkaragnihotri.yogaai.features.common.ui.MascotState
import me.pushkaragnihotri.yogaai.features.common.ui.ZenMascot

@Composable
fun PoseResultScreen(
    poseName: String,
    duration: String,
    feedback: String,
    onHomeClick: () -> Unit
) {
    val decodedName = Uri.decode(poseName)
    val decodedFeedback = Uri.decode(feedback)
    val poseDetail = PoseRepository.getPoseDetail(decodedName)
    val isGreat = decodedFeedback.contains("Great", ignoreCase = true)

    LaunchedEffect(decodedName, duration) {
        val dur = duration.toIntOrNull() ?: 0
        if (decodedName.isBlank() ||
            decodedName == "Detecting..." ||
            decodedName == "No Pose Detected"
        ) {
            return@LaunchedEffect
        }
        val prefs = runCatching { GlobalContext.get().get<UserPreferences>() }.getOrNull()
            ?: return@LaunchedEffect
        prefs.appendYogaSession(decodedName, dur)
    }

    val titleAlpha    = remember { Animatable(0f) }
    val mascotScale   = remember { Animatable(0.6f) }
    val mascotAlpha   = remember { Animatable(0f) }
    val cardAlpha     = remember { Animatable(0f) }
    val cardOffset    = remember { Animatable(28f) }
    val sectionsAlpha = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        launch { titleAlpha.animateTo(1f, tween(300)) }
        delay(140)
        launch { mascotAlpha.animateTo(1f, tween(350)) }
        launch {
            mascotScale.animateTo(
                targetValue = 1f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessMediumLow
                )
            )
        }
        delay(120)
        launch { cardAlpha.animateTo(1f, tween(380)) }
        launch { cardOffset.animateTo(0f, tween(380, easing = FastOutSlowInEasing)) }
        delay(260)
        sectionsAlpha.animateTo(1f, tween(400))
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
                        MaterialTheme.colorScheme.background
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(top = 24.dp, bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.result_title),
                style = ExpressiveHeroStyle,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                modifier = Modifier.alpha(titleAlpha.value)
            )

            Spacer(modifier = Modifier.height(20.dp))

            ZenMascot(
                state = if (isGreat) MascotState.HAPPY else MascotState.ENCOURAGING,
                modifier = Modifier
                    .size(110.dp)
                    .alpha(mascotAlpha.value)
                    .scale(mascotScale.value)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Result hero card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .alpha(cardAlpha.value)
                    .offset(y = cardOffset.value.dp),
                shape = MaterialTheme.shapes.large,
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(
                    modifier = Modifier.padding(28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = decodedName,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    if (poseDetail.sanskritName.isNotEmpty()) {
                        Text(
                            text = poseDetail.sanskritName,
                            style = MaterialTheme.typography.titleMedium,
                            fontStyle = FontStyle.Italic,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Duration chip
                    Surface(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = MaterialTheme.shapes.extraLarge
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.result_label_duration),
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Text(
                                text = "$duration s",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = decodedFeedback,
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        color = if (isGreat) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.error
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Column(modifier = Modifier.alpha(sectionsAlpha.value)) {
                if (poseDetail.benefits.isNotEmpty()) {
                    DetailSection(title = stringResource(R.string.result_section_benefits), items = poseDetail.benefits)
                }
                if (poseDetail.alignmentCues.isNotEmpty()) {
                    DetailSection(title = stringResource(R.string.result_section_alignment), items = poseDetail.alignmentCues)
                }
                if (poseDetail.instructions.isNotEmpty()) {
                    DetailSection(title = stringResource(R.string.result_section_instructions), items = poseDetail.instructions, isOrdered = true)
                }
                if (poseDetail.contraindications.isNotEmpty()) {
                    DetailSection(
                        title = stringResource(R.string.result_section_contraindications),
                        items = poseDetail.contraindications,
                        bulletColor = MaterialTheme.colorScheme.error
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = onHomeClick,
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = MaterialTheme.shapes.extraLarge
                ) {
                    Text(stringResource(R.string.result_back_home), style = MaterialTheme.typography.titleMedium)
                }
            }
        }
    }
}

@Composable
fun DetailSection(
    title: String,
    items: List<String>,
    isOrdered: Boolean = false,
    bulletColor: Color = MaterialTheme.colorScheme.primary
) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(8.dp))
        items.forEachIndexed { index, item ->
            Row(modifier = Modifier.padding(vertical = 4.dp)) {
                Text(
                    text = if (isOrdered) "${index + 1}." else "•",
                    style = MaterialTheme.typography.bodyLarge,
                    color = bulletColor,
                    modifier = Modifier.width(24.dp)
                )
                Text(text = item, style = MaterialTheme.typography.bodyLarge)
            }
        }
        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
    }
}

@Preview(name = "Pose Result — Great Performance")
@Composable
private fun PoseResultScreenGreatPreview() {
    YogaAITheme {
        PoseResultScreen(
            poseName = "Warrior II",
            duration = "60",
            feedback = "Great job! Your form was perfect.",
            onHomeClick = {}
        )
    }
}

@Preview(name = "Pose Result — Session Stopped")
@Composable
private fun PoseResultScreenStoppedPreview() {
    YogaAITheme {
        PoseResultScreen(
            poseName = "Tree Pose",
            duration = "12",
            feedback = "Session Stopped",
            onHomeClick = {}
        )
    }
}

@Preview(name = "Detail Section — Ordered", showBackground = true)
@Composable
private fun DetailSectionOrderedPreview() {
    YogaAITheme {
        DetailSection(
            title = "Instructions",
            items = listOf(
                "Stand with feet hip-width apart",
                "Shift weight onto left foot",
                "Place right foot on inner left thigh",
                "Bring palms together at chest"
            ),
            isOrdered = true
        )
    }
}

@Preview(name = "Detail Section — Bullets", showBackground = true)
@Composable
private fun DetailSectionBulletsPreview() {
    YogaAITheme {
        DetailSection(
            title = "Benefits",
            items = listOf(
                "Improves balance and stability",
                "Strengthens thighs, ankles, and spine",
                "Stretches the groins and inner thighs"
            )
        )
    }
}
