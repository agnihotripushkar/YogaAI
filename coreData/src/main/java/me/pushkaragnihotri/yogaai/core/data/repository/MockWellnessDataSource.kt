package me.pushkaragnihotri.yogaai.core.data.repository

import me.pushkaragnihotri.yogaai.core.data.model.DailyMetric
import me.pushkaragnihotri.yogaai.core.data.model.RiskLevel
import me.pushkaragnihotri.yogaai.core.data.model.RiskPrediction
import java.time.LocalDate

object MockWellnessDataSource {
    fun getTodayRisk(): RiskPrediction {
        return RiskPrediction(
            date = LocalDate.now(),
            riskLevel = RiskLevel.MEDIUM,
            explanation = "Your sleep was slightly fragmented last night.",
            contributingSignals = listOf("Sleep Efficiency: 85%", "Resting HR: +5 bpm")
        )
    }

    fun getTodayMetrics(): DailyMetric {
        return DailyMetric(
            steps = 4520,
            sleepDurationMinutes = 410, // 6h 50m
            restingHeartRate = 72
        )
    }
    
    fun getHistory(): List<RiskPrediction> {
        return listOf(
            RiskPrediction(LocalDate.now().minusDays(1), RiskLevel.LOW, "Great recovery.", emptyList()),
            RiskPrediction(LocalDate.now().minusDays(2), RiskLevel.HIGH, "High fatigue detected.", listOf("Low Sleep", "High Steps")),
            RiskPrediction(LocalDate.now().minusDays(3), RiskLevel.LOW, "Stable baseline.", emptyList())
        )
    }
}
