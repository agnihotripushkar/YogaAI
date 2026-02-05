package me.pushkaragnihotri.yogaai.features.yoga.viewmodel

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
    val confidence: Float = 0f,
    val holdTimeSeconds: Int = 0,
    val isPoseCompleted: Boolean = false,
    val feedback: String = ""
)

class YogaDetectorViewModel(
    val poseDetectionManager: PoseDetectionManager,
    private val poseClassifier: PoseClassifier
) : ViewModel(), PoseDetectionListener {

    companion object {
        private const val TARGET_HOLD_TIME_SECONDS = 10
    }

    private val _uiState = MutableStateFlow(YogaDetectorUiState())
    val uiState: StateFlow<YogaDetectorUiState> = _uiState.asStateFlow()

    private var timerJob: Job? = null

    override fun onResults(result: PoseLandmarkerResult) {
        val classification = poseClassifier.classify(result)
        
        // Don't update state or reset timer if already completed (prevents flickering)
        if (_uiState.value.isPoseCompleted) return

        _uiState.update { 
            it.copy(
                poseResult = result,
                poseName = classification.poseName,
                isPoseCorrect = classification.isCorrect,
                confidence = classification.confidence,
                feedback = classification.feedback
            )
        }

        if (classification.isCorrect) {
            startTimer()
        } else {
            resetTimer()
        }
    }

    private fun startTimer() {
        if (timerJob == null && !_uiState.value.isPoseCompleted) {
            timerJob = viewModelScope.launch {
                while (true) {
                    delay(1000)
                    _uiState.update { 
                        val newTime = it.holdTimeSeconds + 1
                        if (newTime >= TARGET_HOLD_TIME_SECONDS) {
                            // Pose completed!
                            timerJob?.cancel() // Stop the timer
                            it.copy(
                                holdTimeSeconds = newTime,
                                isPoseCompleted = true,
                                // Keep the last feedback
                            )
                        } else {
                            it.copy(holdTimeSeconds = newTime)
                        }
                    }
                }
            }
        }
    }

    private fun resetTimer() {
        // Double check completion to be safe
        if (_uiState.value.isPoseCompleted) return

        timerJob?.cancel()
        timerJob = null
        _uiState.update { 
            it.copy(
                holdTimeSeconds = 0,
                isPoseCompleted = false 
            ) 
        }
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
