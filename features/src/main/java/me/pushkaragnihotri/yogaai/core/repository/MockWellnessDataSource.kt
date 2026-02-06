package me.pushkaragnihotri.yogaai.core.repository

import me.pushkaragnihotri.yogaai.core.model.DailyMetric
import me.pushkaragnihotri.yogaai.core.model.RiskLevel
import me.pushkaragnihotri.yogaai.core.model.RiskPrediction
import java.time.LocalDate

object MockWellnessDataSource {
    fun getTodayRisk(): RiskPrediction {
        return RiskPrediction(
            riskLevel = RiskLevel.MEDIUM,
            explanation = "Your sleep was slightly fragmented last night.",
            contributingSignals = listOf("Sleep Efficiency: 85%", "Resting HR: +5 bpm"),
            date = LocalDate.now()
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
            RiskPrediction(riskLevel = RiskLevel.LOW, explanation = "Great recovery.", contributingSignals = emptyList(), date = LocalDate.now().minusDays(1)),
            RiskPrediction(riskLevel = RiskLevel.HIGH, explanation = "High fatigue detected.", contributingSignals = listOf("Low Sleep", "High Steps"), date = LocalDate.now().minusDays(2)),
            RiskPrediction(riskLevel = RiskLevel.LOW, explanation = "Stable baseline.", contributingSignals = emptyList(), date = LocalDate.now().minusDays(3))
        )
    }
}
