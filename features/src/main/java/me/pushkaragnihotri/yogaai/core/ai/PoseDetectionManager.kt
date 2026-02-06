package me.pushkaragnihotri.yogaai.core.ai

import android.content.Context
import com.google.mediapipe.tasks.core.BaseOptions
import com.google.mediapipe.tasks.vision.core.RunningMode
import com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarker
import com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarkerResult
import com.google.mediapipe.framework.image.BitmapImageBuilder
import com.google.mediapipe.framework.image.MPImage
import timber.log.Timber

interface PoseDetectionManager {
    fun setup(context: Context, listener: PoseDetectionListener)
    fun detectPose(image: android.graphics.Bitmap, rotationDegrees: Int, timestamp: Long)
    fun close()
}

interface PoseDetectionListener {
    fun onResults(result: PoseLandmarkerResult)
    fun onError(error: String)
}

class PoseDetectionManagerImpl : PoseDetectionManager {
    private var poseLandmarker: PoseLandmarker? = null

    override fun setup(context: Context, listener: PoseDetectionListener) {
        try {
            val baseOptions = BaseOptions.builder()
                .setModelAssetPath("pose_landmarker.task")
                .build()

            val optionsBuilder = PoseLandmarker.PoseLandmarkerOptions.builder()
                .setBaseOptions(baseOptions)
                .setRunningMode(RunningMode.LIVE_STREAM)
                .setResultListener { result, _ ->
                    listener.onResults(result)
                }
                .setErrorListener { error ->
                    listener.onError(error.message ?: "Unknown error")
                }

            poseLandmarker = PoseLandmarker.createFromOptions(context, optionsBuilder.build())
        } catch (e: Exception) {
            Timber.e(e, "Error setting up PoseLandmarker")
            listener.onError("Failed to initialize PoseLandmarker: ${e.message}")
        }
    }

    override fun detectPose(image: android.graphics.Bitmap, rotationDegrees: Int, timestamp: Long) {
        val mpImage = BitmapImageBuilder(image).build()
        poseLandmarker?.detectAsync(mpImage, timestamp)
    }

    override fun close() {
        poseLandmarker?.close()
        poseLandmarker = null
    }
}
