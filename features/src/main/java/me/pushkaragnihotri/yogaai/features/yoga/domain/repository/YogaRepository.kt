package me.pushkaragnihotri.yogaai.features.yoga.domain.repository

import android.content.Context
import com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarkerResult

interface YogaRepository {
    fun setup(context: Context, listener: YogaRepositoryListener)
    fun detectPose(image: android.graphics.Bitmap, rotationDegrees: Int, timestamp: Long)
    fun close()
}

interface YogaRepositoryListener {
    fun onResults(result: PoseLandmarkerResult)
    fun onError(error: String)
}
