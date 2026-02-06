package me.pushkaragnihotri.yogaai.features.yoga.data.repository

import android.content.Context
import com.google.mediapipe.tasks.core.BaseOptions
import com.google.mediapipe.tasks.vision.core.RunningMode
import com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarker
import com.google.mediapipe.framework.image.BitmapImageBuilder
import me.pushkaragnihotri.yogaai.features.yoga.domain.repository.YogaRepository
import me.pushkaragnihotri.yogaai.features.yoga.domain.repository.YogaRepositoryListener
import timber.log.Timber

class YogaRepositoryImpl : YogaRepository {
    private var poseLandmarker: PoseLandmarker? = null

    override fun setup(context: Context, listener: YogaRepositoryListener) {
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
