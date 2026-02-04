package me.pushkaragnihotri.yogaai.core.wellness

import me.pushkaragnihotri.yogaai.core.model.RiskLevel

interface WellnessExplanationGenerator {
    suspend fun generateExplanation(
        riskLevel: RiskLevel,
        contributingSignals: List<String>,
        metricsSummary: String
    ): String
}
