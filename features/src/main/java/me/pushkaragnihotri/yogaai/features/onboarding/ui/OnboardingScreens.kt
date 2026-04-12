package me.pushkaragnihotri.yogaai.features.onboarding.ui

import android.Manifest
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.CameraAlt
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.MonitorHeart
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.SelfImprovement
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.health.connect.client.PermissionController
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.pushkaragnihotri.yogaai.core.HealthConnectManager
import me.pushkaragnihotri.yogaai.features.R
import kotlin.math.abs
import kotlin.math.roundToInt

// ─── Constants ────────────────────────────────────────────────────────────────

private const val TOTAL_STEPS = 12

private val GOAL_OPTIONS = listOf(
    "🧘" to "Reduce stress and anxiety",
    "💪" to "Build strength and flexibility",
    "❤️" to "Improve my overall health",
    "😴" to "Sleep better at night",
    "🏃" to "Complement my fitness routine",
    "🌱" to "Start a healthy habit",
)

private val PAIN_OPTIONS = listOf(
    "😬" to "I don't know if I'm doing poses right",
    "🤕" to "I'm worried about injuring myself",
    "⏰" to "I don't know where to start",
    "📱" to "I get distracted and lose motivation",
    "🧩" to "Classes feel too advanced for me",
    "📊" to "I can't see my progress",
)

private val TINDER_CARDS = listOf(
    "I've watched yoga videos but still can't tell if my form is right.",
    "I start a routine, miss one day, and feel like I've already failed.",
    "Yoga classes feel designed for people who are already flexible.",
    "I track my steps but have no idea how my body is actually doing.",
)

private data class SolutionItem(val pain: String, val solution: String)

private val SOLUTION_ITEMS = listOf(
    SolutionItem(
        pain = "Not knowing if form is right",
        solution = "Real-time AI feedback on every pose — audio cues, instant correction.",
    ),
    SolutionItem(
        pain = "Risk of injury from wrong form",
        solution = "3 beginner-safe poses with alignment cues and contraindications built in.",
    ),
    SolutionItem(
        pain = "No way to track progress",
        solution = "Every session saved — see your streak and improvement over time.",
    ),
    SolutionItem(
        pain = "Health data scattered across apps",
        solution = "Steps, sleep, heart rate — all connected to your practice in one view.",
    ),
)

private data class Testimonial(val quote: String, val name: String, val tag: String)

private val TESTIMONIALS = listOf(
    Testimonial(
        quote = "I tried yoga apps before but never knew if my form was right. YogaAI actually tells me. Game changer.",
        name = "Priya, 28",
        tag = "Complete beginner",
    ),
    Testimonial(
        quote = "My sleep improved after 2 weeks. Seeing health data alongside my practice finally connected the dots.",
        name = "Marcus, 34",
        tag = "Health-focused",
    ),
    Testimonial(
        quote = "I quit three times before this. The instant feedback is what made it stick.",
        name = "Sarah, 41",
        tag = "Returning to fitness",
    ),
)

private data class OnboardingPose(
    val name: String,
    val sanskritName: String,
    val benefit: String,
    val emoji: String,
)

private val POSES = listOf(
    OnboardingPose("Tree Pose", "Vrikshasana", "Builds balance and focus", "🌳"),
    OnboardingPose("Warrior II", "Virabhadrasana II", "Strengthens legs and opens hips", "⚔️"),
    OnboardingPose("Plank", "Phalakasana", "Builds core strength and stability", "🏋️"),
)

// ─── Main Orchestrator ────────────────────────────────────────────────────────

@Composable
fun OnboardingScreen(
    viewModel: OnboardingViewModel,
    onOnboardingComplete: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        viewModel.onAction(OnboardingAction.CameraPermissionResult(granted))
    }

    val healthLauncher = rememberLauncherForActivityResult(
        PermissionController.createRequestPermissionResultContract()
    ) { granted ->
        viewModel.onAction(OnboardingAction.HealthPermissionsResult(granted))
    }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                OnboardingEvent.NavigateToHome -> onOnboardingComplete()
            }
        }
    }

    BackHandler(enabled = state.currentStep > 0) {
        viewModel.onAction(OnboardingAction.PreviousStep)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        LinearProgressIndicator(
            progress = { (state.currentStep + 1).toFloat() / TOTAL_STEPS },
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
        )

        AnimatedContent(
            targetState = state.currentStep,
            transitionSpec = {
                if (targetState > initialState) {
                    (slideInHorizontally { it } + fadeIn(tween(200))) togetherWith
                        (slideOutHorizontally { -it } + fadeOut(tween(200)))
                } else {
                    (slideInHorizontally { -it } + fadeIn(tween(200))) togetherWith
                        (slideOutHorizontally { it } + fadeOut(tween(200)))
                }
            },
            modifier = Modifier.fillMaxSize(),
            label = "onboarding_step",
        ) { step ->
            when (step) {
                0 -> WelcomeStep(
                    onNext = { viewModel.onAction(OnboardingAction.NextStep) },
                )
                1 -> GoalStep(
                    state = state,
                    onBack = { viewModel.onAction(OnboardingAction.PreviousStep) },
                    onAction = viewModel::onAction,
                )
                2 -> PainPointsStep(
                    state = state,
                    onBack = { viewModel.onAction(OnboardingAction.PreviousStep) },
                    onAction = viewModel::onAction,
                )
                3 -> SocialProofStep(
                    onBack = { viewModel.onAction(OnboardingAction.PreviousStep) },
                    onNext = { viewModel.onAction(OnboardingAction.NextStep) },
                )
                4 -> TinderCardsStep(
                    state = state,
                    onAction = viewModel::onAction,
                )
                5 -> PersonalizedSolutionStep(
                    state = state,
                    onBack = { viewModel.onAction(OnboardingAction.PreviousStep) },
                    onNext = { viewModel.onAction(OnboardingAction.NextStep) },
                )
                6 -> ProfileSetupStep(
                    state = state,
                    onBack = { viewModel.onAction(OnboardingAction.PreviousStep) },
                    onAction = viewModel::onAction,
                )
                7 -> CameraPermissionStep(
                    onBack = { viewModel.onAction(OnboardingAction.PreviousStep) },
                    onEnable = { cameraLauncher.launch(Manifest.permission.CAMERA) },
                    onSkip = { viewModel.onAction(OnboardingAction.CameraPermissionResult(false)) },
                )
                8 -> HealthConnectStep(
                    onBack = { viewModel.onAction(OnboardingAction.PreviousStep) },
                    onConnect = {
                        if (viewModel.getHealthConnectAvailability() == HealthConnectManager.SDK_AVAILABLE) {
                            healthLauncher.launch(viewModel.permissions)
                        } else {
                            viewModel.onAction(OnboardingAction.HealthPermissionsResult(emptySet()))
                        }
                    },
                    onSkip = { viewModel.onAction(OnboardingAction.HealthPermissionsResult(emptySet())) },
                )
                9 -> ProcessingStep(
                    onFinished = { viewModel.onAction(OnboardingAction.NextStep) },
                )
                10 -> PoseDemoStep(
                    state = state,
                    onBack = { viewModel.onAction(OnboardingAction.PreviousStep) },
                    onAction = viewModel::onAction,
                )
                11 -> ValueDeliveryStep(
                    state = state,
                    onComplete = { viewModel.onAction(OnboardingAction.CompleteOnboarding) },
                )
                else -> WelcomeStep(onNext = { viewModel.onAction(OnboardingAction.NextStep) })
            }
        }
    }
}

// ─── Shared Layout ────────────────────────────────────────────────────────────

@Composable
private fun StepScaffold(
    showBack: Boolean = true,
    onBack: () -> Unit = {},
    content: @Composable () -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        if (showBack) {
            IconButton(
                onClick = onBack,
                modifier = Modifier.padding(start = 8.dp, top = 8.dp),
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onBackground,
                )
            }
        } else {
            Spacer(modifier = Modifier.height(48.dp))
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 8.dp),
        ) {
            content()
        }
    }
}

@Composable
private fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    enabled: Boolean = true,
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = MaterialTheme.shapes.extraLarge,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
        ),
    ) {
        Text(text = text, style = MaterialTheme.typography.titleMedium)
    }
}

// ─── Step 0: Welcome ──────────────────────────────────────────────────────────

@Composable
private fun WelcomeStep(onNext: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.onboarding_1),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.75f)),
                        startY = 300f,
                    )
                )
        )
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = 28.dp, vertical = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Your yoga practice,\nguided by AI",
                style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
                color = Color.White,
                textAlign = TextAlign.Center,
            )
            Spacer(Modifier.height(12.dp))
            Text(
                text = "Real-time feedback. No guessing. Just progress.",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White.copy(alpha = 0.85f),
                textAlign = TextAlign.Center,
            )
            Spacer(Modifier.height(36.dp))
            Button(
                onClick = onNext,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = MaterialTheme.shapes.extraLarge,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                ),
            ) {
                Text("Get Started", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}

// ─── Step 1: Goal Question ────────────────────────────────────────────────────

@Composable
private fun GoalStep(
    state: OnboardingState,
    onBack: () -> Unit,
    onAction: (OnboardingAction) -> Unit,
) {
    StepScaffold(onBack = onBack) {
        Text(
            text = "Why are you starting yoga?",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground,
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = "We'll build your practice around your goal.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(24.dp))
        GOAL_OPTIONS.forEach { (emoji, label) ->
            val isSelected = state.selectedGoal == label
            val bgColor by animateColorAsState(
                if (isSelected) MaterialTheme.colorScheme.primaryContainer
                else MaterialTheme.colorScheme.surface,
                label = "goal_bg",
            )
            val borderColor by animateColorAsState(
                if (isSelected) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                label = "goal_border",
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp)
                    .clip(MaterialTheme.shapes.large)
                    .background(bgColor)
                    .border(1.dp, borderColor, MaterialTheme.shapes.large)
                    .clickable { onAction(OnboardingAction.GoalSelected(label)) }
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(text = emoji, fontSize = 22.sp)
                Spacer(Modifier.width(14.dp))
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                    color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer
                    else MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f),
                )
                if (isSelected) {
                    Icon(
                        imageVector = Icons.Rounded.CheckCircle,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp),
                    )
                }
            }
        }
        Spacer(Modifier.height(24.dp))
        AnimatedVisibility(visible = state.selectedGoal.isNotEmpty()) {
            PrimaryButton(text = "Continue", onClick = { onAction(OnboardingAction.NextStep) })
        }
        Spacer(Modifier.height(24.dp))
    }
}

// ─── Step 2: Pain Points ──────────────────────────────────────────────────────

@Composable
private fun PainPointsStep(
    state: OnboardingState,
    onBack: () -> Unit,
    onAction: (OnboardingAction) -> Unit,
) {
    StepScaffold(onBack = onBack) {
        Text(
            text = "What's held you back from yoga?",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground,
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = "Pick everything that applies.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(24.dp))
        PAIN_OPTIONS.forEach { (emoji, label) ->
            val isSelected = label in state.selectedPains
            val bgColor by animateColorAsState(
                if (isSelected) MaterialTheme.colorScheme.primaryContainer
                else MaterialTheme.colorScheme.surface,
                label = "pain_bg",
            )
            val borderColor by animateColorAsState(
                if (isSelected) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                label = "pain_border",
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp)
                    .clip(MaterialTheme.shapes.large)
                    .background(bgColor)
                    .border(1.dp, borderColor, MaterialTheme.shapes.large)
                    .clickable { onAction(OnboardingAction.PainToggled(label)) }
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(text = emoji, fontSize = 22.sp)
                Spacer(Modifier.width(14.dp))
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                    color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer
                    else MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f),
                )
                if (isSelected) {
                    Icon(
                        imageVector = Icons.Rounded.Check,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp),
                    )
                }
            }
        }
        Spacer(Modifier.height(24.dp))
        PrimaryButton(text = "Continue", onClick = { onAction(OnboardingAction.NextStep) })
        Spacer(Modifier.height(24.dp))
    }
}

// ─── Step 3: Social Proof ─────────────────────────────────────────────────────

@Composable
private fun SocialProofStep(onBack: () -> Unit, onNext: () -> Unit) {
    StepScaffold(onBack = onBack) {
        Text(
            text = "You're in good company",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground,
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = "Beginners just like you found their practice with YogaAI.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(24.dp))
        TESTIMONIALS.forEach { t ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = MaterialTheme.shapes.large,
                elevation = CardDefaults.cardElevation(0.dp),
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row {
                        repeat(5) {
                            Icon(
                                imageVector = Icons.Rounded.Star,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier.size(14.dp),
                            )
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "\"${t.quote}\"",
                        style = MaterialTheme.typography.bodyMedium.copy(fontStyle = FontStyle.Italic),
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    Spacer(Modifier.height(10.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primaryContainer),
                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Person,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.size(16.dp),
                            )
                        }
                        Spacer(Modifier.width(8.dp))
                        Column {
                            Text(
                                text = t.name,
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                                color = MaterialTheme.colorScheme.onSurface,
                            )
                            Text(
                                text = t.tag,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                }
            }
        }
        Spacer(Modifier.height(24.dp))
        PrimaryButton(text = "That's me →", onClick = onNext)
        Spacer(Modifier.height(24.dp))
    }
}

// ─── Step 4: Tinder Cards ─────────────────────────────────────────────────────

@Composable
private fun TinderCardsStep(
    state: OnboardingState,
    onAction: (OnboardingAction) -> Unit,
) {
    val cardIndex = state.tinderIndex.coerceAtMost(TINDER_CARDS.lastIndex)
    val statement = TINDER_CARDS[cardIndex]
    val offset = remember(cardIndex) { Animatable(0f) }
    val scope = rememberCoroutineScope()

    fun swipe(toRight: Boolean) {
        scope.launch {
            offset.animateTo(if (toRight) 1200f else -1200f, tween(280))
            onAction(OnboardingAction.TinderCardAdvanced)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.height(32.dp))
        Text(
            text = "Sound familiar?",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(6.dp))
        Text(
            text = "${cardIndex + 1} of ${TINDER_CARDS.size}",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(32.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp),
            contentAlignment = Alignment.Center,
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .offset { IntOffset(offset.value.roundToInt(), 0) }
                    .rotate(offset.value / 40f)
                    .pointerInput(cardIndex) {
                        detectHorizontalDragGestures(
                            onDragEnd = {
                                if (abs(offset.value) > 120f) {
                                    swipe(offset.value > 0)
                                } else {
                                    scope.launch { offset.animateTo(0f, tween(300)) }
                                }
                            },
                            onHorizontalDrag = { _, dragAmount ->
                                scope.launch { offset.snapTo(offset.value + dragAmount) }
                            },
                        )
                    },
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(28.dp),
                ) {
                    AnimatedVisibility(
                        visible = offset.value < -30f,
                        modifier = Modifier.align(Alignment.TopStart),
                    ) {
                        Text(
                            text = "SKIP",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier
                                .border(2.dp, MaterialTheme.colorScheme.error, RoundedCornerShape(6.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                        )
                    }
                    AnimatedVisibility(
                        visible = offset.value > 30f,
                        modifier = Modifier.align(Alignment.TopEnd),
                    ) {
                        Text(
                            text = "SAME",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .border(2.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(6.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                        )
                    }
                    Text(
                        text = "\"$statement\"",
                        style = MaterialTheme.typography.headlineSmall.copy(fontStyle = FontStyle.Italic),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.align(Alignment.Center),
                    )
                }
            }
        }

        Spacer(Modifier.height(28.dp))
        Text(
            text = "Swipe or tap a button",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.errorContainer)
                    .clickable { swipe(false) },
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Rounded.Close,
                    contentDescription = "Skip",
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(28.dp),
                )
            }
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .clickable { swipe(true) },
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Rounded.Check,
                    contentDescription = "Relatable",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(28.dp),
                )
            }
        }
    }
}

// ─── Step 5: Personalized Solution ───────────────────────────────────────────

@Composable
private fun PersonalizedSolutionStep(
    state: OnboardingState,
    onBack: () -> Unit,
    onNext: () -> Unit,
) {
    StepScaffold(onBack = onBack) {
        Text(
            text = "YogaAI was built for exactly this.",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground,
        )
        Spacer(Modifier.height(8.dp))
        val sub = if (state.selectedGoal.isNotEmpty())
            "Here's how we help you ${state.selectedGoal.lowercase()}."
        else "Here's how we solve each of these."
        Text(
            text = sub,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(24.dp))
        SOLUTION_ITEMS.forEach { item ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = MaterialTheme.shapes.large,
                elevation = CardDefaults.cardElevation(0.dp),
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.Top,
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.CheckCircle,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(18.dp),
                        )
                    }
                    Spacer(Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = item.pain,
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Spacer(Modifier.height(2.dp))
                        Text(
                            text = item.solution,
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                    }
                }
            }
        }
        Spacer(Modifier.height(24.dp))
        PrimaryButton(text = "I'm in →", onClick = onNext)
        Spacer(Modifier.height(24.dp))
    }
}

// ─── Step 6: Profile Setup ────────────────────────────────────────────────────

@Composable
private fun ProfileSetupStep(
    state: OnboardingState,
    onBack: () -> Unit,
    onAction: (OnboardingAction) -> Unit,
) {
    StepScaffold(onBack = onBack) {
        Text(
            text = "Let's set up your profile",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground,
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = "We'll personalise your experience around you.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(24.dp))
        OutlinedTextField(
            value = state.userName,
            onValueChange = { onAction(OnboardingAction.NameChanged(it)) },
            label = { Text("Your name") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.large,
        )
        Spacer(Modifier.height(14.dp))
        OutlinedTextField(
            value = state.userAge,
            onValueChange = { onAction(OnboardingAction.AgeChanged(it.filter { c -> c.isDigit() })) },
            label = { Text("Your age") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.large,
        )
        Spacer(Modifier.height(20.dp))
        Text(
            text = "Your yoga experience",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Spacer(Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("Beginner", "Intermediate", "Advanced").forEach { level ->
                FilterChip(
                    selected = state.selectedLevel == level,
                    onClick = { onAction(OnboardingAction.LevelSelected(level)) },
                    label = { Text(level, style = MaterialTheme.typography.bodySmall) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    ),
                )
            }
        }
        Spacer(Modifier.height(20.dp))
        Text(
            text = "Daily step goal: ${"%,d".format(state.stepGoal)} steps",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Slider(
            value = state.stepGoal.toFloat(),
            onValueChange = { onAction(OnboardingAction.StepGoalChanged(it.roundToInt())) },
            valueRange = 1000f..20000f,
            steps = 18,
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(Modifier.height(12.dp))
        Text(
            text = "Daily sleep goal: ${state.sleepGoal} hours",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Slider(
            value = state.sleepGoal.toFloat(),
            onValueChange = { onAction(OnboardingAction.SleepGoalChanged(it.roundToInt())) },
            valueRange = 4f..12f,
            steps = 7,
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(Modifier.height(24.dp))
        PrimaryButton(text = "Save my profile", onClick = { onAction(OnboardingAction.NextStep) })
        Spacer(Modifier.height(24.dp))
    }
}

// ─── Step 7: Camera Permission ────────────────────────────────────────────────

@Composable
private fun CameraPermissionStep(
    onBack: () -> Unit,
    onEnable: () -> Unit,
    onSkip: () -> Unit,
) {
    PermissionStep(
        onBack = onBack,
        icon = Icons.Rounded.CameraAlt,
        headline = "See your form in real time",
        description = "YogaAI uses your camera to detect your pose and give instant feedback — all processed on your device, never uploaded.",
        bullets = listOf(
            "See your skeleton overlay as you move",
            "Get audio cues when your form needs adjusting",
            "Works completely offline — your camera feed never leaves your phone",
        ),
        primaryLabel = "Enable Camera",
        onPrimary = onEnable,
        onSkip = onSkip,
    )
}

// ─── Step 8: Health Connect ───────────────────────────────────────────────────

@Composable
private fun HealthConnectStep(
    onBack: () -> Unit,
    onConnect: () -> Unit,
    onSkip: () -> Unit,
) {
    PermissionStep(
        onBack = onBack,
        icon = Icons.Rounded.MonitorHeart,
        headline = "Track your wellness automatically",
        description = "Connect Health Connect to see how your sleep, heart rate, and activity link to your yoga practice.",
        bullets = listOf(
            "Daily steps, calories, and heart rate in one dashboard",
            "Wellness risk alerts based on your real health data",
            "Read-only access — your data stays private and is never shared",
        ),
        primaryLabel = "Connect Health Data",
        onPrimary = onConnect,
        onSkip = onSkip,
    )
}

@Composable
private fun PermissionStep(
    onBack: () -> Unit,
    icon: ImageVector,
    headline: String,
    description: String,
    bullets: List<String>,
    primaryLabel: String,
    onPrimary: () -> Unit,
    onSkip: () -> Unit,
) {
    StepScaffold(onBack = onBack) {
        Spacer(Modifier.height(16.dp))
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(40.dp),
            )
        }
        Spacer(Modifier.height(24.dp))
        Text(
            text = headline,
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground,
        )
        Spacer(Modifier.height(10.dp))
        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(24.dp))
        bullets.forEach { bullet ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Check,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(14.dp),
                    )
                }
                Spacer(Modifier.width(12.dp))
                Text(
                    text = bullet,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f),
                )
            }
        }
        Spacer(Modifier.height(32.dp))
        PrimaryButton(text = primaryLabel, onClick = onPrimary)
        Spacer(Modifier.height(12.dp))
        TextButton(
            onClick = onSkip,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text = "Not now",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        Spacer(Modifier.height(24.dp))
    }
}

// ─── Step 9: Processing ───────────────────────────────────────────────────────

@Composable
private fun ProcessingStep(onFinished: () -> Unit) {
    LaunchedEffect(Unit) {
        delay(2200)
        onFinished()
    }

    val pulseAlpha = remember { Animatable(0.4f) }
    LaunchedEffect(Unit) {
        while (true) {
            pulseAlpha.animateTo(1f, tween(700))
            pulseAlpha.animateTo(0.4f, tween(700))
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .clip(CircleShape)
                    .background(
                        MaterialTheme.colorScheme.primaryContainer.copy(alpha = pulseAlpha.value)
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Rounded.SelfImprovement,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(52.dp),
                )
            }
            Spacer(Modifier.height(28.dp))
            Text(
                text = "Building your practice plan…",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
            )
            Spacer(Modifier.height(10.dp))
            Text(
                text = "Tailoring everything to your goals",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
        }
    }
}

// ─── Step 10: Pose Demo ───────────────────────────────────────────────────────

@Composable
private fun PoseDemoStep(
    state: OnboardingState,
    onBack: () -> Unit,
    onAction: (OnboardingAction) -> Unit,
) {
    StepScaffold(onBack = onBack) {
        Text(
            text = "Pick your first pose",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground,
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = "YogaAI will guide you through it in real time.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(24.dp))
        POSES.forEach { pose ->
            val isSelected = state.selectedPose == pose.name
            val bgColor by animateColorAsState(
                if (isSelected) MaterialTheme.colorScheme.primaryContainer
                else MaterialTheme.colorScheme.surface,
                label = "pose_bg",
            )
            val borderColor by animateColorAsState(
                if (isSelected) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                label = "pose_border",
            )
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .border(
                        width = if (isSelected) 2.dp else 1.dp,
                        color = borderColor,
                        shape = MaterialTheme.shapes.large,
                    )
                    .clickable { onAction(OnboardingAction.PoseSelected(pose.name)) },
                colors = CardDefaults.cardColors(containerColor = bgColor),
                shape = MaterialTheme.shapes.large,
                elevation = CardDefaults.cardElevation(0.dp),
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 18.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(text = pose.emoji, fontSize = 32.sp)
                    Spacer(Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = pose.name,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer
                                else MaterialTheme.colorScheme.onSurface,
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(
                                text = "Beginner",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f))
                                    .padding(horizontal = 6.dp, vertical = 2.dp),
                            )
                        }
                        Text(
                            text = pose.sanskritName,
                            style = MaterialTheme.typography.bodySmall.copy(fontStyle = FontStyle.Italic),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Spacer(Modifier.height(2.dp))
                        Text(
                            text = pose.benefit,
                            style = MaterialTheme.typography.bodySmall,
                            color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer
                            else MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    if (isSelected) {
                        Icon(
                            imageVector = Icons.Rounded.CheckCircle,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(22.dp),
                        )
                    }
                }
            }
        }
        Spacer(Modifier.height(24.dp))
        AnimatedVisibility(visible = state.selectedPose.isNotEmpty()) {
            PrimaryButton(
                text = "Start with ${state.selectedPose}",
                onClick = { onAction(OnboardingAction.NextStep) },
            )
        }
        Spacer(Modifier.height(24.dp))
    }
}

// ─── Step 11: Value Delivery ──────────────────────────────────────────────────

@Composable
private fun ValueDeliveryStep(
    state: OnboardingState,
    onComplete: () -> Unit,
) {
    val selectedPoseData = POSES.firstOrNull { it.name == state.selectedPose } ?: POSES.first()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
                        MaterialTheme.colorScheme.background,
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Rounded.CheckCircle,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(48.dp),
                )
            }
            Spacer(Modifier.height(20.dp))
            val greeting = if (state.userName.isNotBlank())
                "You're ready, ${state.userName}."
            else
                "You're ready to begin."
            Text(
                text = greeting,
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Your first session is queued up.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
            Spacer(Modifier.height(32.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = MaterialTheme.shapes.extraLarge,
                elevation = CardDefaults.cardElevation(0.dp),
            ) {
                Row(
                    modifier = Modifier.padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(text = selectedPoseData.emoji, fontSize = 36.sp)
                    Spacer(Modifier.width(16.dp))
                    Column {
                        Text(
                            text = "First up: ${selectedPoseData.name}",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                        Text(
                            text = selectedPoseData.sanskritName,
                            style = MaterialTheme.typography.bodySmall.copy(fontStyle = FontStyle.Italic),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = selectedPoseData.benefit,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }
            Spacer(Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = MaterialTheme.shapes.extraLarge,
                elevation = CardDefaults.cardElevation(0.dp),
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Your daily goals",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Spacer(Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround,
                    ) {
                        GoalSummaryItem(label = "Steps", value = "%,d".format(state.stepGoal))
                        GoalSummaryItem(label = "Sleep", value = "${state.sleepGoal}h")
                        GoalSummaryItem(label = "Level", value = state.selectedLevel)
                    }
                }
            }
            Spacer(Modifier.height(40.dp))
            PrimaryButton(text = "Start My First Session", onClick = onComplete)
        }
    }
}

@Composable
private fun GoalSummaryItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.primary,
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}
