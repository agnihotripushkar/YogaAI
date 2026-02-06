package me.pushkaragnihotri.yogaai.features.home.domain.repository

import kotlinx.coroutines.flow.StateFlow
import me.pushkaragnihotri.yogaai.core.model.DailyMetric
import me.pushkaragnihotri.yogaai.core.model.RiskPrediction
import java.time.LocalDate

interface HomeRepository {
    val todayMetrics: StateFlow<DailyMetric>
    suspend fun refreshMetrics()
    suspend fun getTodayRisk(): RiskPrediction
    suspend fun getRiskByDate(date: LocalDate): RiskPrediction?
    suspend fun getTodayMetrics(): DailyMetric
    suspend fun getRiskHistory(): List<RiskPrediction>
    suspend fun disconnect()
}
