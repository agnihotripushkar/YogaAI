package me.pushkaragnihotri.yogaai.features.yogadetector

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarkerResult
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import me.pushkaragnihotri.yogaai.core.ai.PoseClassifier
import me.pushkaragnihotri.yogaai.core.ai.PoseDetectionListener
import me.pushkaragnihotri.yogaai.core.ai.PoseDetectionManager

data class YogaDetectorUiState(
    val poseResult: PoseLandmarkerResult? = null,
    val errorMessage: String? = null,
    val poseName: String = "Detecting...",
    val isPoseCorrect: Boolean = false,
    val holdTimeSeconds: Int = 0
)

class YogaDetectorViewModel(
    val poseDetectionManager: PoseDetectionManager,
    private val poseClassifier: PoseClassifier
) : ViewModel(), PoseDetectionListener {

    private val _uiState = MutableStateFlow(YogaDetectorUiState())
    val uiState: StateFlow<YogaDetectorUiState> = _uiState.asStateFlow()

    private var timerJob: Job? = null

    override fun onResults(result: PoseLandmarkerResult) {
        val classification = poseClassifier.classify(result)
        
        _uiState.update { 
            it.copy(
                poseResult = result,
                poseName = classification.poseName,
                isPoseCorrect = classification.isCorrect
            )
        }

        if (classification.isCorrect) {
            startTimer()
        } else {
            resetTimer()
        }
    }

    private fun startTimer() {
        if (timerJob == null) {
            timerJob = viewModelScope.launch {
                while (true) {
                    delay(1000)
                    _uiState.update { it.copy(holdTimeSeconds = it.holdTimeSeconds + 1) }
                }
            }
        }
    }

    private fun resetTimer() {
        timerJob?.cancel()
        timerJob = null
        _uiState.update { it.copy(holdTimeSeconds = 0) }
    }

    override fun onError(error: String) {
        _uiState.update { it.copy(errorMessage = error) }
    }

    override fun onCleared() {
        super.onCleared()
        poseDetectionManager.close()
        timerJob?.cancel()
    }
}
