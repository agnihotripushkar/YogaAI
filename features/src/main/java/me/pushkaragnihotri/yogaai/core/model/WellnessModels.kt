package me.pushkaragnihotri.yogaai.core.model

import java.time.LocalDate

enum class RiskLevel {
    LOW, MEDIUM, HIGH
}

data class DailyMetric(
    val steps: Long = 0,
    val sleepDurationMinutes: Long = 0,
    val restingHeartRate: Int = 0,
    val calories: Double = 0.0
)

data class RiskPrediction(
    val riskLevel: RiskLevel,
    val explanation: String,
    val contributingSignals: List<String>,
    val date: LocalDate? = null
)
