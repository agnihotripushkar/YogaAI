package me.pushkaragnihotri.yogaai.features.yoga.ui

import android.Manifest
import android.graphics.Bitmap
import android.graphics.Matrix
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview as CameraXPreview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarkerResult
import me.pushkaragnihotri.yogaai.features.R
import me.pushkaragnihotri.yogaai.features.common.audio.TextToSpeechManager
import me.pushkaragnihotri.yogaai.features.common.ui.MascotState
import me.pushkaragnihotri.yogaai.features.common.ui.ZenMascot
import me.pushkaragnihotri.yogaai.features.ui.theme.*
import me.pushkaragnihotri.yogaai.core.presentation.ObserveAsEvents
import org.koin.androidx.compose.koinViewModel
import java.util.concurrent.Executors

@Composable
fun YogaDetectorRoot(
    onNavigateToResult: (String, String, String) -> Unit = { _, _, _ -> },
    viewModel: YogaDetectorViewModel = koinViewModel()
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsState()
    var isMuted by remember { mutableStateOf(false) }
    var cameraSelector by remember { mutableStateOf(CameraSelector.DEFAULT_BACK_CAMERA) }

    val ttsManager = remember { TextToSpeechManager(context) }
    DisposableEffect(Unit) {
        onDispose { ttsManager.shutdown() }
    }

    LaunchedEffect(state.feedback, isMuted) {
        if (!isMuted && state.feedback.isNotEmpty()) {
            ttsManager.speak(state.feedback)
        }
    }

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is YogaDetectorEvent.NavigateToResult -> {
                ttsManager.stop()
                onNavigateToResult(event.poseName, event.duration, event.feedback)
            }
            is YogaDetectorEvent.ShowError -> { /* error shown via state.errorMessage */ }
        }
    }

    // Navigate on pose completion (state-driven)
    LaunchedEffect(state.isPoseCompleted) {
        if (state.isPoseCompleted) {
            ttsManager.stop()
            onNavigateToResult(
                state.poseName,
                state.holdTimeSeconds.toString(),
                state.feedback.ifEmpty { "Great job!" }
            )
        }
    }

    LaunchedEffect(Unit) {
        viewModel.yogaRepository.setup(context, viewModel)
    }

    YogaDetectorScreen(
        state = state,
        isMuted = isMuted,
        cameraSelector = cameraSelector,
        onAction = { action ->
            when (action) {
                YogaDetectorAction.OnToggleMute -> isMuted = !isMuted
                YogaDetectorAction.OnSwitchCamera -> {
                    cameraSelector = if (cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
                        CameraSelector.DEFAULT_FRONT_CAMERA
                    } else {
                        CameraSelector.DEFAULT_BACK_CAMERA
                    }
                }
                else -> viewModel.onAction(action)
            }
        },
        onImageAnalyzed = { imageProxy ->
            val bitmap = imageProxy.toRotatedBitmap()
            if (bitmap != null) {
                viewModel.yogaRepository.detectPose(
                    bitmap,
                    imageProxy.imageInfo.rotationDegrees,
                    System.currentTimeMillis()
                )
            }
            imageProxy.close()
        }
    )
}

@Composable
fun YogaDetectorScreen(
    state: YogaDetectorState,
    isMuted: Boolean,
    cameraSelector: CameraSelector,
    onAction: (YogaDetectorAction) -> Unit,
    onImageAnalyzed: (androidx.camera.core.ImageProxy) -> Unit
) {
    val context = LocalContext.current
    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context, Manifest.permission.CAMERA
            ) == android.content.pm.PackageManager.PERMISSION_GRANTED
        )
    }

    val launcher = androidx.activity.compose.rememberLauncherForActivityResult(
        contract = androidx.activity.result.contract.ActivityResultContracts.RequestPermission(),
        onResult = { isGranted -> hasCameraPermission = isGranted }
    )

    LaunchedEffect(Unit) {
        if (!hasCameraPermission) launcher.launch(Manifest.permission.CAMERA)
    }

    Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {
        if (hasCameraPermission) {
            CameraPreview(
                cameraSelector = cameraSelector,
                onImageAnalyzed = onImageAnalyzed
            )

            val poseResult = state.poseResult
            if (poseResult != null) {
                PoseOverlay(result = poseResult, isCorrect = state.isPoseCorrect)
            }

            TopHud(
                poseName = state.poseName,
                timeSeconds = state.holdTimeSeconds,
                confidence = state.confidence,
                holdTargetSeconds = state.holdTargetSeconds,
                isCorrect = state.isPoseCorrect
            )

            BottomSheetControls(
                modifier = Modifier.align(Alignment.BottomCenter),
                isCorrect = state.isPoseCorrect,
                feedback = state.feedback,
                isMuted = isMuted,
                onToggleMute = { onAction(YogaDetectorAction.OnToggleMute) },
                onStop = { onAction(YogaDetectorAction.OnStopClick) },
                onSwitchCamera = { onAction(YogaDetectorAction.OnSwitchCamera) }
            )
        } else {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = stringResource(R.string.camera_permission_needed),
                        color = Color.White
                    )
                    Spacer(Modifier.height(8.dp))
                    Button(onClick = { launcher.launch(Manifest.permission.CAMERA) }) {
                        Text(stringResource(R.string.camera_permission_grant))
                    }
                }
            }
        }

        val errorMessage = state.errorMessage
        if (errorMessage != null) {
            Snackbar(
                modifier = Modifier.align(Alignment.BottomCenter).padding(16.dp)
            ) { Text(text = errorMessage.asString(context)) }
        }
    }
}

@Composable
fun BottomSheetControls(
    modifier: Modifier = Modifier,
    isCorrect: Boolean,
    feedback: String,
    isMuted: Boolean,
    onToggleMute: () -> Unit,
    onStop: () -> Unit,
    onSwitchCamera: () -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Gunmetal.copy(alpha = 0.97f)),
        shape = MaterialTheme.shapes.large.copy(
            bottomStart = androidx.compose.foundation.shape.CornerSize(0.dp),
            bottomEnd = androidx.compose.foundation.shape.CornerSize(0.dp)
        )
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .width(40.dp).height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(Color.Gray.copy(alpha = 0.3f))
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (isCorrect) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Correct",
                    tint = BrightGreen,
                    modifier = Modifier.size(48.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(R.string.feedback_great_balance),
                    style = MaterialTheme.typography.displaySmall,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                if (feedback.isNotEmpty()) {
                    Text(
                        text = feedback,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.LightGray,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            } else {
                Text(
                    text = stringResource(R.string.feedback_adjust_pose),
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                if (feedback.isNotEmpty()) {
                    Text(
                        text = feedback,
                        style = MaterialTheme.typography.titleMedium,
                        color = AccentTeal,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                } else {
                    Text(
                        text = stringResource(R.string.feedback_aligning),
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onSwitchCamera,
                    modifier = Modifier.size(56.dp).background(Color.White.copy(alpha = 0.1f), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Cameraswitch,
                        contentDescription = "Switch Camera",
                        tint = Color.White
                    )
                }

                Button(
                    onClick = onStop,
                    shape = MaterialTheme.shapes.extraLarge,
                    colors = ButtonDefaults.buttonColors(containerColor = RedButton),
                    modifier = Modifier.size(72.dp),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Box(modifier = Modifier.size(32.dp).background(Color.White, RoundedCornerShape(4.dp)))
                }

                IconButton(
                    onClick = onToggleMute,
                    modifier = Modifier.size(56.dp).background(Color.White.copy(alpha = 0.1f), CircleShape)
                ) {
                    Icon(
                        imageVector = if (isMuted) Icons.Default.VolumeOff else Icons.Default.VolumeUp,
                        contentDescription = "Mute",
                        tint = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun TopHud(
    poseName: String,
    timeSeconds: Int,
    confidence: Float,
    holdTargetSeconds: Int = 30,
    isCorrect: Boolean = false
) {
    Box(
        modifier = Modifier.fillMaxWidth().padding(top = 48.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            color = DarkOverlay.copy(alpha = 0.8f),
            shape = MaterialTheme.shapes.extraLarge,
            modifier = Modifier.wrapContentHeight()
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                AnimatedContent(
                    targetState = poseName,
                    transitionSpec = {
                        (fadeIn(tween(220)) + slideInVertically(initialOffsetY = { it / 4 }))
                            .togetherWith(fadeOut(tween(180)))
                    },
                    label = "poseName"
                ) { name ->
                    Text(
                        text = name,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        modifier = Modifier.widthIn(max = 120.dp)
                    )
                }

                Box(modifier = Modifier.width(1.dp).height(20.dp).background(Color.Gray.copy(alpha = 0.5f)))

                ZenMascot(state = MascotState.MEDITATIVE, modifier = Modifier.size(40.dp))

                val showHoldRing = isCorrect && holdTargetSeconds > 0
                Box(contentAlignment = Alignment.Center, modifier = Modifier.size(44.dp)) {
                    if (showHoldRing) {
                        val progress = (timeSeconds.coerceAtMost(holdTargetSeconds).toFloat() / holdTargetSeconds)
                            .coerceIn(0f, 1f)
                        CircularProgressIndicator(
                            progress = { 1f },
                            modifier = Modifier.fillMaxSize(),
                            color = Color.White.copy(alpha = 0.12f),
                            strokeWidth = 3.dp,
                            trackColor = Color.Transparent
                        )
                        CircularProgressIndicator(
                            progress = { progress },
                            modifier = Modifier.fillMaxSize(),
                            color = BrightGreen,
                            strokeWidth = 3.dp,
                            trackColor = Color.Transparent
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Timer,
                            contentDescription = null,
                            tint = BrightGreen,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = String.format("%02d:%02d", timeSeconds / 60, timeSeconds % 60),
                            color = Color.White,
                            fontWeight = FontWeight.Medium,
                            fontSize = 16.sp
                        )
                    }
                }

                Surface(color = ConfidenceBg, shape = RoundedCornerShape(50)) {
                    Text(
                        text = "${(confidence * 100).toInt()}%",
                        color = AccentTeal,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun CameraPreview(
    cameraSelector: CameraSelector,
    onImageAnalyzed: (androidx.camera.core.ImageProxy) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    val executor = remember { Executors.newSingleThreadExecutor() }

    AndroidView(
        factory = { ctx ->
            PreviewView(ctx).apply {
                implementationMode = PreviewView.ImplementationMode.COMPATIBLE
            }
        },
        modifier = Modifier.fillMaxSize(),
        update = { previewView ->
            cameraProviderFuture.addListener({
                val cameraProvider = cameraProviderFuture.get()
                val preview = CameraXPreview.Builder().build().also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }

                val imageAnalysis = ImageAnalysis.Builder()
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build()
                    .also {
                        it.setAnalyzer(executor) { imageProxy -> onImageAnalyzed(imageProxy) }
                    }

                try {
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(lifecycleOwner, cameraSelector, preview, imageAnalysis)
                } catch (e: Exception) {
                    // Handle error
                }
            }, ContextCompat.getMainExecutor(context))
        }
    )
}

@Composable
fun PoseOverlay(result: PoseLandmarkerResult, isCorrect: Boolean) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val landmarks = result.landmarks()
        if (landmarks.isNotEmpty()) {
            for (landmark in landmarks[0]) {
                drawCircle(
                    color = Color.White.copy(alpha = 0.5f),
                    radius = 8f,
                    center = Offset(landmark.x() * size.width, landmark.y() * size.height)
                )
                drawCircle(
                    color = if (isCorrect) BrightGreen else AccentTeal,
                    radius = 4f,
                    center = Offset(landmark.x() * size.width, landmark.y() * size.height)
                )
            }
        }
    }
}

fun androidx.camera.core.ImageProxy.toRotatedBitmap(): Bitmap? {
    val bitmap = this.toBitmap() ?: return null
    val matrix = Matrix().apply {
        postRotate(this@toRotatedBitmap.imageInfo.rotationDegrees.toFloat())
    }
    return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
}

@Preview(name = "Top HUD", showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun TopHudPreview() {
    YogaAITheme {
        TopHud(
            poseName = "Warrior II",
            timeSeconds = 18,
            confidence = 0.95f,
            holdTargetSeconds = 30,
            isCorrect = true
        )
    }
}

@Preview(name = "Bottom Controls — Correct Pose", showBackground = true, backgroundColor = 0xFF1A1A2E)
@Composable
private fun BottomSheetControlsCorrectPreview() {
    YogaAITheme {
        BottomSheetControls(
            isCorrect = true,
            feedback = "Keep your back straight and arms extended",
            isMuted = false,
            onToggleMute = {},
            onStop = {},
            onSwitchCamera = {}
        )
    }
}

@Preview(name = "Bottom Controls — Incorrect Pose", showBackground = true, backgroundColor = 0xFF1A1A2E)
@Composable
private fun BottomSheetControlsIncorrectPreview() {
    YogaAITheme {
        BottomSheetControls(
            isCorrect = false,
            feedback = "Lower your hips and bend your front knee more",
            isMuted = true,
            onToggleMute = {},
            onStop = {},
            onSwitchCamera = {}
        )
    }
}

@Preview(name = "Bottom Controls — Aligning (no feedback)", showBackground = true, backgroundColor = 0xFF1A1A2E)
@Composable
private fun BottomSheetControlsAligningPreview() {
    YogaAITheme {
        BottomSheetControls(
            isCorrect = false,
            feedback = "",
            isMuted = false,
            onToggleMute = {},
            onStop = {},
            onSwitchCamera = {}
        )
    }
}

@Preview(name = "Top HUD — High Hold Time", showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun TopHudHighTimePreview() {
    YogaAITheme {
        TopHud(poseName = "Tree Pose", timeSeconds = 125, confidence = 0.88f)
    }
}
