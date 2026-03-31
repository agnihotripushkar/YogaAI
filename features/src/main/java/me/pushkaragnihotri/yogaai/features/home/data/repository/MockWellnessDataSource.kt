package me.pushkaragnihotri.yogaai.features.home.data.repository

import me.pushkaragnihotri.yogaai.core.presentation.UiText
import me.pushkaragnihotri.yogaai.features.home.data.model.DailyMetric
import me.pushkaragnihotri.yogaai.features.home.data.model.RiskLevel
import me.pushkaragnihotri.yogaai.features.home.data.model.RiskPrediction
import me.pushkaragnihotri.yogaai.features.R
import java.time.LocalDate

object MockWellnessDataSource {

    fun getTodayMetrics(): DailyMetric {
        return DailyMetric(
            steps = 8500,
            sleepDurationMinutes = 420, // 7 hours
            restingHeartRate = 65,
            calories = 2100.0
        )
    }

    fun getTodayRisk(): RiskPrediction {
        return RiskPrediction(
            riskLevel = RiskLevel.LOW,
            explanation = UiText.StringResource(R.string.wellness_explanation_good_sleep),
            contributingSignals = emptyList(),
            date = LocalDate.now()
        )
    }

    fun getHistory(): List<RiskPrediction> {
        val today = LocalDate.now()
        return listOf(
            getTodayRisk(),
            RiskPrediction(
                riskLevel = RiskLevel.MEDIUM,
                explanation = UiText.StringResource(R.string.wellness_explanation_elevated_hr),
                contributingSignals = listOf(UiText.StringResource(R.string.wellness_signal_hr_70)),
                date = today.minusDays(1)
            ),
            RiskPrediction(
                riskLevel = RiskLevel.LOW,
                explanation = UiText.StringResource(R.string.wellness_explanation_recovery_day),
                contributingSignals = emptyList(),
                date = today.minusDays(2)
            )
        )
    }
}