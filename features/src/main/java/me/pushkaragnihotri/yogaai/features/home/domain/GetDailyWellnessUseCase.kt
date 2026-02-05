package me.pushkaragnihotri.yogaai.features.home.domain

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DirectionsRun
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.LocalFireDepartment
import androidx.compose.material.icons.rounded.NightlightRound
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import me.pushkaragnihotri.yogaai.core.repository.WellnessRepository
import me.pushkaragnihotri.yogaai.features.R
import me.pushkaragnihotri.yogaai.features.home.model.WellnessUiModel
import me.pushkaragnihotri.yogaai.features.common.ui.theme.*

class GetDailyWellnessUseCase(
    private val repository: WellnessRepository
) {
    operator fun invoke(): Flow<List<WellnessUiModel>> {
        return repository.todayMetrics.map { metrics ->
            val stepGoal = 10000f
            val sleepGoal = 480f
            val caloriesGoal = 2500f

            fun getStepColor(steps: Long): Color = when {
                steps >= stepGoal -> StatusGreen
                steps >= stepGoal * 0.5 -> StatusOrange
                else -> StatusRed
            }

            listOf(
                WellnessUiModel(
                    titleRes = R.string.metric_sleep,
                    value = "${metrics.sleepDurationMinutes / 60}h",
                    icon = Icons.Rounded.NightlightRound,
                    color = WellnessSleep,
                    progress = (metrics.sleepDurationMinutes / sleepGoal).coerceIn(0f, 1f),
                    score = if (metrics.sleepDurationMinutes > 0) (metrics.sleepDurationMinutes / 60 * 10).toInt().coerceAtMost(100) else null
                ),
                WellnessUiModel(
                    titleRes = R.string.metric_steps,
                    value = metrics.steps.toString(),
                    icon = Icons.Rounded.DirectionsRun,
                    color = getStepColor(metrics.steps),
                    progress = (metrics.steps / stepGoal).coerceIn(0f, 1f)
                ),
                WellnessUiModel(
                    titleRes = R.string.metric_hr,
                    value = "${metrics.restingHeartRate} bpm",
                    icon = Icons.Rounded.Favorite,
                    color = if(metrics.restingHeartRate in 60..100) WellnessHR else WellnessCalories,
                    progress = if (metrics.restingHeartRate > 0) 1f else 0f
                ),
                WellnessUiModel(
                    titleRes = R.string.metric_calories,
                    value = String.format("%.0f", metrics.calories),
                    icon = Icons.Rounded.LocalFireDepartment,
                    color = WellnessCalories,
                    progress = (metrics.calories.toFloat() / caloriesGoal).coerceIn(0f, 1f)
                )
            )
        }
    }
}
