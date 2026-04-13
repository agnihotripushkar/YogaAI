package me.pushkaragnihotri.yogaai.features.history.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import me.pushkaragnihotri.yogaai.core.database.YogaSessionDao
import me.pushkaragnihotri.yogaai.core.database.YogaSessionEntity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class PoseHistoryViewModel(
    private val yogaSessionDao: YogaSessionDao
) : ViewModel() {

    private val _state = MutableStateFlow(PoseHistoryState())
    val state = _state.asStateFlow()

    init {
        yogaSessionDao.getAllSessions()
            .onEach { sessions -> _state.update { it.copy(sessions = sessions.toUiItems(), isLoading = false) } }
            .launchIn(viewModelScope)
    }

    fun onAction(action: PoseHistoryAction) {
        when (action) {
            PoseHistoryAction.OnClearHistory -> viewModelScope.launch {
                yogaSessionDao.deleteAll()
            }
        }
    }

    private fun List<YogaSessionEntity>.toUiItems(): List<PoseHistoryUiItem> {
        val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
        val today = Calendar.getInstance()
        val yesterday = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -1) }
        val dateFormat = SimpleDateFormat("EEE, d MMM", Locale.getDefault())

        return map { record ->
            val date = Date(record.completedAtEpochMs)
            val recordCal = Calendar.getInstance().apply { time = date }

            val dateLabel = when {
                isSameDay(recordCal, today) -> "Today"
                isSameDay(recordCal, yesterday) -> "Yesterday"
                else -> dateFormat.format(date)
            }

            PoseHistoryUiItem(
                poseName = record.poseName,
                dateLabel = dateLabel,
                timeLabel = timeFormat.format(date),
                isCompleted = record.isCompleted,
                attemptCount = record.attemptCount,
                durationSeconds = record.durationSeconds
            )
        }
    }

    private fun isSameDay(a: Calendar, b: Calendar): Boolean =
        a.get(Calendar.YEAR) == b.get(Calendar.YEAR) &&
            a.get(Calendar.DAY_OF_YEAR) == b.get(Calendar.DAY_OF_YEAR)
}
