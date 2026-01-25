package me.pushkaragnihotri.yogaai.features.yogadetector

import android.Manifest
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import org.koin.androidx.compose.koinViewModel
import java.util.concurrent.Executors
import android.graphics.Bitmap
import android.graphics.Matrix
import androidx.camera.core.ImageProxy

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun YogaDetectorScreen(
    viewModel: YogaDetectorViewModel = koinViewModel()
) {
    val context = LocalContext.current
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.poseDetectionManager.setup(context, viewModel)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (cameraPermissionState.status.isGranted) {
            CameraPreview(
                onImageAnalyzed = { imageProxy ->
                    val bitmap = imageProxy.toBitmap()
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
            
            // Draw landmarks overlay
            uiState.poseResult?.let { result ->
                PoseOverlay(result)
            }
        } else {
            Text(
                text = "Camera permission is required for pose detection.",
                modifier = Modifier.align(Alignment.Center).padding(16.dp),
                style = MaterialTheme.typography.bodyLarge
            )
            LaunchedEffect(Unit) {
                cameraPermissionState.launchPermissionRequest()
            }
        }

        uiState.errorMessage?.let { error ->
            Text(
                text = error,
                color = Color.Red,
                modifier = Modifier.align(Alignment.BottomCenter).padding(16.dp)
            )
        }
    }
}

@Composable
fun CameraPreview(
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
                val preview = Preview.Builder().build().also {
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

                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

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
fun PoseOverlay(result: com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarkerResult) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val landmarks = result.landmarks()
        if (landmarks.isNotEmpty()) {
            // Very simplified drawing of landmarks for visualization
            landmarks[0].forEach { landmark ->
                drawCircle(
                    color = Color.Cyan,
                    radius = 8f,
                    center = Offset(landmark.x() * size.width, landmark.y() * size.height)
                )
            }
        }
    }
}

fun ImageProxy.toBitmap(): Bitmap? {
    val bitmap = this.toBitmap() ?: return null
    val matrix = Matrix().apply {
        postRotate(this@toBitmap.imageInfo.rotationDegrees.toFloat())
    }
    return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
}
