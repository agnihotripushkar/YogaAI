package me.pushkaragnihotri.yogaai.features.yogadetector

import android.Manifest
import android.graphics.Bitmap
import android.graphics.Matrix
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview as CameraXPreview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarkerResult
import org.koin.androidx.compose.koinViewModel
import java.util.concurrent.Executors
import androidx.compose.ui.tooling.preview.Preview
import me.pushkaragnihotri.yogaai.features.common.ui.theme.YogaAITheme

@Composable
fun YogaDetectorScreen(
    viewModel: YogaDetectorViewModel = koinViewModel(),
    onNavigateToResult: (String, String, String) -> Unit = { _, _, _ -> }
) {
    val context = LocalContext.current
    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == android.content.pm.PackageManager.PERMISSION_GRANTED
        )
    }

    val launcher = androidx.activity.compose.rememberLauncherForActivityResult(
        contract = androidx.activity.result.contract.ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            hasCameraPermission = isGranted
        }
    )

    LaunchedEffect(Unit) {
        if (!hasCameraPermission) {
            launcher.launch(Manifest.permission.CAMERA)
        }
        viewModel.poseDetectionManager.setup(context, viewModel)
    }

    val uiState by viewModel.uiState.collectAsState()
    var isMuted by remember { mutableStateOf(false) }
    var cameraSelector by remember { mutableStateOf(CameraSelector.DEFAULT_BACK_CAMERA) }

    LaunchedEffect(uiState.isPoseCompleted) {
        if (uiState.isPoseCompleted) {
            onNavigateToResult(
                uiState.poseName,
                uiState.holdTimeSeconds.toString(),
                uiState.feedback.ifEmpty { "Great job!" }
            )
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {
        if (hasCameraPermission) {
            CameraPreview(
                cameraSelector = cameraSelector,
                onImageAnalyzed = { imageProxy ->
                    val bitmap = imageProxy.toRotatedBitmap()
                    if (bitmap != null) {
                        viewModel.poseDetectionManager.detectPose(
                            bitmap,
                            imageProxy.imageInfo.rotationDegrees,
                            System.currentTimeMillis()
                        )
                    }
                    imageProxy.close()
                }
            )
            
            // Draw skeleton overlay
            val poseResult = uiState.poseResult
            if (poseResult != null) {
                PoseOverlay(
                    result = poseResult,
                    isCorrect = uiState.isPoseCorrect
                )
            }

            // --- Top HUD ---
            TopHud(
                poseName = uiState.poseName,
                timeSeconds = uiState.holdTimeSeconds,
                confidence = uiState.confidence
            )

            // --- Bottom Sheet & Controls ---
            BottomSheetControls(
                modifier = Modifier.align(Alignment.BottomCenter),
                isCorrect = uiState.isPoseCorrect,
                feedback = uiState.feedback,
                isMuted = isMuted,
                onToggleMute = { isMuted = !isMuted },
                onStop = { 
                     onNavigateToResult(uiState.poseName, uiState.holdTimeSeconds.toString(), "Session Stopped")
                },
                onSwitchCamera = { 
                    cameraSelector = if (cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
                        CameraSelector.DEFAULT_FRONT_CAMERA
                    } else {
                        CameraSelector.DEFAULT_BACK_CAMERA
                    }
                }
            )
            
        } else {
            // Permission Request State
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Camera permission needed",
                        color = Color.White
                    )
                    Spacer(Modifier.height(8.dp))
                    Button(onClick = { launcher.launch(Manifest.permission.CAMERA) }) {
                        Text("Grant Permission")
                    }
                }
            }
        }

        val errorMessage = uiState.errorMessage
        if (errorMessage != null) {
             Snackbar(
                modifier = Modifier.align(Alignment.BottomCenter).padding(16.dp)
            ) { Text(text = errorMessage) }
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
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1E2124).copy(alpha = 0.95f) // Dark Gunmetal
        ),
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
             // Handle bar
            Box(
                modifier = Modifier
                    .width(40.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(Color.Gray.copy(alpha = 0.3f))
            )
            
            Spacer(modifier = Modifier.height(24.dp))

            // Feedback Section
            if (isCorrect) {
                 Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Correct",
                    tint = Color(0xFF00E676),
                    modifier = Modifier.size(48.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Great balance!",
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
                    text = "Adjust your pose",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                 if (feedback.isNotEmpty()) {
                     Text(
                        text = feedback,
                        style = MaterialTheme.typography.titleMedium,
                        color = Color(0xFF1DE9B6), // Accent color for instructions
                        textAlign = TextAlign.Center,
                         modifier = Modifier.padding(top = 8.dp)
                    )
                } else {
                     Text(
                        text = "Aligning...",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.Gray
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Integrated Key Controls
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Switch Cam
                IconButton(
                    onClick = onSwitchCamera,
                    modifier = Modifier
                        .size(56.dp)
                        .background(Color.White.copy(alpha = 0.1f), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Cameraswitch,
                        contentDescription = "Switch Camera",
                        tint = Color.White
                    )
                }
        
                // Stop Button (Big Red)
                Button(
                    onClick = onStop,
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)),
                    modifier = Modifier.size(72.dp),
                    contentPadding = PaddingValues(0.dp)
                ) {
                     Box(
                        modifier = Modifier
                            .size(32.dp)
                            .background(Color.White, RoundedCornerShape(4.dp))
                    )
                }
        
                // Audio
                IconButton(
                    onClick = onToggleMute,
                    modifier = Modifier
                        .size(56.dp)
                        .background(Color.White.copy(alpha = 0.1f), CircleShape)
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
fun TopHud(poseName: String, timeSeconds: Int, confidence: Float) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 48.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            color = Color(0xFF2C2C2C).copy(alpha = 0.8f),
            shape = RoundedCornerShape(50),
            modifier = Modifier.height(48.dp)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 24.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Pose Name
                Text(
                    text = poseName,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )

                // Divider
                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .height(20.dp)
                        .background(Color.Gray.copy(alpha = 0.5f))
                )

                // Timer
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Icon(
                        imageVector = Icons.Default.Timer,
                        contentDescription = "Timer",
                        tint = Color(0xFF00E676), // Bright Green
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = String.format("%02d:%02d", timeSeconds / 60, timeSeconds % 60),
                        color = Color.White,
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp
                    )
                }
                
                // Confidence Pill
                Surface(
                    color = Color(0xFF004D40), // Dark Teal
                    shape = RoundedCornerShape(50),
                    modifier = Modifier
                ) {
                   Text(
                        text = "${(confidence * 100).toInt()}%",
                        color = Color(0xFF1DE9B6), // Accent Teal
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
                        it.setAnalyzer(executor) { imageProxy ->
                            onImageAnalyzed(imageProxy)
                        }
                    }

                try {
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        cameraSelector,
                        preview,
                        imageAnalysis
                    )
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
            val firstPersonLandmarks = landmarks[0]
            
            // Draw connections could be added here for full stick figure
            
            for (landmark in firstPersonLandmarks) {
                drawCircle(
                    color = Color.White.copy(alpha = 0.5f),
                    radius = 8f,
                    center = Offset(landmark.x() * size.width, landmark.y() * size.height)
                )
                drawCircle(
                    color = if (isCorrect) Color(0xFF00E676) else Color.White,
                    radius = 4f,
                    center = Offset(landmark.x() * size.width, landmark.y() * size.height)
                )
            }
        }
    }
}

fun ImageProxy.toRotatedBitmap(): Bitmap? {
    val bitmap = this.toBitmap() ?: return null
    val matrix = Matrix().apply {
        postRotate(this@toRotatedBitmap.imageInfo.rotationDegrees.toFloat())
    }
    return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
}

@Preview
@Composable
fun TopHudPreview() {
    YogaAITheme {
        TopHud(poseName = "Warrior II", timeSeconds = 30, confidence = 0.95f)
    }
}

@Preview
@Composable
fun BottomSheetControlsCorrectPreview() {
    YogaAITheme {
        BottomSheetControls(
            isCorrect = true,
            feedback = "Keep your back straight",
            isMuted = false,
            onToggleMute = {},
            onStop = {},
            onSwitchCamera = {}
        )
    }
}

@Preview
@Composable
fun BottomSheetControlsIncorrectPreview() {
    YogaAITheme {
        BottomSheetControls(
            isCorrect = false,
            feedback = "Lower your hips",
            isMuted = true,
            onToggleMute = {},
            onStop = {},
            onSwitchCamera = {}
        )
    }
}
