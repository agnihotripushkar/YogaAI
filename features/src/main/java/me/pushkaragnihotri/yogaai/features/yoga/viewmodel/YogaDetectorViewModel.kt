package me.pushkaragnihotri.yogaai.features.yoga.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarkerResult
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import me.pushkaragnihotri.yogaai.core.presentation.UiText
import me.pushkaragnihotri.yogaai.features.yoga.data.source.PoseClassifier
import me.pushkaragnihotri.yogaai.features.yoga.domain.repository.YogaRepository
import me.pushkaragnihotri.yogaai.features.yoga.domain.repository.YogaRepositoryListener

class YogaDetectorViewModel(
    val yogaRepository: YogaRepository,
    private val poseClassifier: PoseClassifier
) : ViewModel(), YogaRepositoryListener {

    private val _state = MutableStateFlow(YogaDetectorState())
    val state = _state.asStateFlow()

    private val _events = Channel<YogaDetectorEvent>()
    val events = _events.receiveAsFlow()

    private var timerJob: Job? = null

    fun onAction(action: YogaDetectorAction) {
        when (action) {
            YogaDetectorAction.OnStopClick -> {
                viewModelScope.launch {
                    val s = _state.value
                    _events.send(
                        YogaDetectorEvent.NavigateToResult(
                            poseName = s.poseName,
                            duration = s.holdTimeSeconds.toString(),
                            feedback = "Session Stopped"
                        )
                    )
                }
            }
            YogaDetectorAction.OnToggleMute -> { /* mute state stays in UI layer */ }
            YogaDetectorAction.OnSwitchCamera -> { /* camera state stays in UI layer */ }
        }
    }

    override fun onResults(result: PoseLandmarkerResult) {
        val classification = poseClassifier.classify(result)

        if (_state.value.isPoseCompleted) return

        _state.update {
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
        if (timerJob == null && !_state.value.isPoseCompleted) {
            timerJob = viewModelScope.launch {
                while (true) {
                    delay(1000)
                    _state.update { it.copy(holdTimeSeconds = it.holdTimeSeconds + 1) }
                }
            }
        }
    }

    private fun resetTimer() {
        if (_state.value.isPoseCompleted) return
        timerJob?.cancel()
        timerJob = null
        _state.update { it.copy(holdTimeSeconds = 0, isPoseCompleted = false) }
    }

    override fun onError(error: String) {
        val errorText = UiText.DynamicString(error)
        _state.update { it.copy(errorMessage = errorText) }
        viewModelScope.launch {
            _events.send(YogaDetectorEvent.ShowError(errorText))
        }
    }

    override fun onCleared() {
        super.onCleared()
        yogaRepository.close()
        timerJob?.cancel()
    }
}
