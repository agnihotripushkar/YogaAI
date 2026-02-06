package me.pushkaragnihotri.yogaai.features.home.domain

import kotlinx.coroutines.flow.StateFlow
import me.pushkaragnihotri.yogaai.features.home.data.model.DailyMetric
import me.pushkaragnihotri.yogaai.features.home.data.model.RiskLevel
import me.pushkaragnihotri.yogaai.features.home.data.model.RiskPrediction
import java.time.LocalDate

interface HomeRepository {
    val todayMetrics: StateFlow<DailyMetric>
    suspend fun refreshMetrics()
    suspend fun getTodayRisk(): RiskPrediction
    suspend fun getRiskByDate(date: LocalDate): RiskPrediction?
    suspend fun getTodayMetrics(): DailyMetric
    suspend fun getRiskHistory(): List<RiskPrediction>
    suspend fun disconnect()

    suspend fun generateExplanation(
        riskLevel: RiskLevel,
        contributingSignals: List<String>,
        metricsSummary: String
    ): String

    suspend fun isAvailable(): Boolean
}