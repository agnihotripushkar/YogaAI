package me.pushkaragnihotri.yogaai.core.repository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import me.pushkaragnihotri.yogaai.core.HealthConnectManager
import me.pushkaragnihotri.yogaai.core.model.DailyMetric
import me.pushkaragnihotri.yogaai.core.model.RiskLevel
import me.pushkaragnihotri.yogaai.core.model.RiskPrediction
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

interface WellnessRepository {
    val todayMetrics: StateFlow<DailyMetric>
    suspend fun refreshMetrics()
    suspend fun getTodayRisk(): RiskPrediction
    suspend fun getRiskByDate(date: LocalDate): RiskPrediction?
    suspend fun getTodayMetrics(): DailyMetric
    suspend fun getRiskHistory(): List<RiskPrediction>
    suspend fun disconnect()
}

class WellnessRepositoryImpl(
    private val healthConnectManager: HealthConnectManager,
    private val isDemoMode: Boolean = false
) : WellnessRepository {

    private val _todayMetrics = MutableStateFlow(DailyMetric())
    override val todayMetrics: StateFlow<DailyMetric> = _todayMetrics.asStateFlow()

    override suspend fun refreshMetrics() {
        _todayMetrics.value = getTodayMetrics()
    }

    override suspend fun getTodayRisk(): RiskPrediction {
        if (isDemoMode) return MockWellnessDataSource.getTodayRisk()
        
        // TODO: Real logic using ML model. For now returning dummy based on simple heuristic.
        val metrics = getTodayMetrics()
        return if (metrics.sleepDurationMinutes < 300) {
           RiskPrediction(
               LocalDate.now(), 
               RiskLevel.HIGH, 
               "Short sleep detected.", 
               listOf("Sleep < 5h")
           )
        } else {
            RiskPrediction(
                LocalDate.now(), 
                RiskLevel.LOW, 
                "Wellness looks good.", 
                emptyList()
            )
        }
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
