package me.pushkaragnihotri.yogaai.features.home.data.repository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import me.pushkaragnihotri.yogaai.core.HealthConnectManager
import me.pushkaragnihotri.yogaai.core.model.DailyMetric
import me.pushkaragnihotri.yogaai.core.model.RiskLevel
import me.pushkaragnihotri.yogaai.core.model.RiskPrediction
import me.pushkaragnihotri.yogaai.features.home.domain.repository.HomeRepository
import me.pushkaragnihotri.yogaai.features.home.wellness.WellnessExplanationGenerator
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

class HomeRepositoryImpl(
    private val healthConnectManager: HealthConnectManager,
    private val explanationGenerator: WellnessExplanationGenerator? = null,
    private val isDemoMode: Boolean = false
) : HomeRepository {

    private val _todayMetrics = MutableStateFlow(DailyMetric())
    override val todayMetrics: StateFlow<DailyMetric> = _todayMetrics.asStateFlow()

    override suspend fun refreshMetrics() {
        _todayMetrics.value = getTodayMetrics()
    }

    override suspend fun getTodayRisk(): RiskPrediction {
        if (isDemoMode) return MockWellnessDataSource.getTodayRisk()
        
        val metrics = getTodayMetrics()
        
        // Simple heuristic for risk level
        val riskLevel = if (metrics.sleepDurationMinutes < 300) RiskLevel.HIGH else RiskLevel.LOW
        val signals = if (riskLevel == RiskLevel.HIGH) listOf("Sleep < 5h") else emptyList()
        
        val explanation = if (explanationGenerator?.isAvailable() == true) {
             explanationGenerator.generateExplanation(
                riskLevel = riskLevel,
                contributingSignals = signals,
                metricsSummary = "Steps: ${metrics.steps}, Sleep: ${metrics.sleepDurationMinutes}m, HR: ${metrics.restingHeartRate}"
            )
        } else {
             if (riskLevel == RiskLevel.HIGH) "Short sleep detected." else "Wellness looks good."
        }

        return RiskPrediction(
            riskLevel = riskLevel,
            explanation = explanation,
            contributingSignals = signals,
            date = LocalDate.now()
        )
    }

    override suspend fun getRiskByDate(date: LocalDate): RiskPrediction? {
        if (isDemoMode) {
            // Simple mock return
             return MockWellnessDataSource.getHistory().find { it.date == date } 
                    ?: MockWellnessDataSource.getTodayRisk().takeIf { it.date == date }
        }
        return null // TODO real
    }

    override suspend fun getTodayMetrics(): DailyMetric {
        if (isDemoMode) return MockWellnessDataSource.getTodayMetrics()

        val now = Instant.now()
        val startOfDay = LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()
        
        val steps = healthConnectManager.readSteps(startOfDay, now)
        val sleep = healthConnectManager.readSleepSessions(startOfDay.minusSeconds(86400), now) // Look back 24h
            .sumOf { it.endTime.epochSecond - it.startTime.epochSecond } / 60
            
        // Simplified HR logic (avg)
        val hrRecords = healthConnectManager.readHeartRate(startOfDay, now)
        val avgHr = if (hrRecords.isNotEmpty()) {
            hrRecords.map { it.samples.map { s -> s.beatsPerMinute }.average() }.average().toInt()
        } else 0
        
        val calories = healthConnectManager.readCalories(startOfDay, now)

        return DailyMetric(steps, sleep, avgHr, calories)
    }

    override suspend fun getRiskHistory(): List<RiskPrediction> {
        if (isDemoMode) return MockWellnessDataSource.getHistory()
        return emptyList() // Implement real history storage/retrieval later
    }

    override suspend fun disconnect() {
        // Clear local data if needed
    }
}
