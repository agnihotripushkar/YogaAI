package me.pushkaragnihotri.yogaai.features.home.data.repository

import me.pushkaragnihotri.yogaai.features.home.data.model.DailyMetric
import me.pushkaragnihotri.yogaai.features.home.data.model.RiskLevel
import me.pushkaragnihotri.yogaai.features.home.data.model.RiskPrediction
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
            explanation = "Your activity levels are good and sleep is sufficient.",
            contributingSignals = emptyList(),
            date = LocalDate.now()
        )
    }

    fun getHistory(): List<RiskPrediction> {
        val today = LocalDate.now()
        return listOf(
            getTodayRisk(),
            RiskPrediction(RiskLevel.MEDIUM, "Slightly elevated heart rate.", listOf("Resting HR > 70"), today.minusDays(1)),
            RiskPrediction(RiskLevel.LOW, "Great recovery day.", emptyList(), today.minusDays(2))
        )
    }
}