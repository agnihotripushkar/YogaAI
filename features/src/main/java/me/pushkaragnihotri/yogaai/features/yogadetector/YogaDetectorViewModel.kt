package me.pushkaragnihotri.yogaai.features.yogadetector

import androidx.lifecycle.ViewModel
import com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarkerResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import me.pushkaragnihotri.yogaai.core.ai.PoseDetectionListener
import me.pushkaragnihotri.yogaai.core.ai.PoseDetectionManager

data class YogaDetectorUiState(
    val poseResult: PoseLandmarkerResult? = null,
    val errorMessage: String? = null
)

class YogaDetectorViewModel(
    val poseDetectionManager: PoseDetectionManager
) : ViewModel(), PoseDetectionListener {

    private val _uiState = MutableStateFlow(YogaDetectorUiState())
    val uiState: StateFlow<YogaDetectorUiState> = _uiState.asStateFlow()

    override fun onResults(result: PoseLandmarkerResult) {
        _uiState.update { it.copy(poseResult = result) }
    }

    override fun onError(error: String) {
        _uiState.update { it.copy(errorMessage = error) }
    }

    override fun onCleared() {
        super.onCleared()
        poseDetectionManager.close()
    }
}
