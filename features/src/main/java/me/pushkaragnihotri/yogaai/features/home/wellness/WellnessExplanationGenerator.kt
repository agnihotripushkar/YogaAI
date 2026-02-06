package me.pushkaragnihotri.yogaai.features.home.wellness

import me.pushkaragnihotri.yogaai.core.model.RiskLevel

interface WellnessExplanationGenerator {
    suspend fun generateExplanation(
        riskLevel: RiskLevel,
        contributingSignals: List<String>,
        metricsSummary: String
    ): String

    suspend fun isAvailable(): Boolean
}
