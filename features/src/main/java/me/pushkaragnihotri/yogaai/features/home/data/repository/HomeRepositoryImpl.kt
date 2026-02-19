package me.pushkaragnihotri.yogaai.features.home.data.repository

import android.os.Build
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import me.pushkaragnihotri.yogaai.core.HealthConnectManager
import me.pushkaragnihotri.yogaai.features.home.data.model.DailyMetric
import me.pushkaragnihotri.yogaai.features.home.data.model.RiskLevel
import me.pushkaragnihotri.yogaai.features.home.data.model.RiskPrediction
import me.pushkaragnihotri.yogaai.features.home.domain.HomeRepository
import timber.log.Timber
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

class HomeRepositoryImpl(
    private val healthConnectManager: HealthConnectManager
) : HomeRepository {

    private val _todayMetrics = MutableStateFlow(DailyMetric())
    override val todayMetrics: StateFlow<DailyMetric> = _todayMetrics.asStateFlow()

    /**
     * Returns true if Health Connect is available AND permissions are granted.
     * If not, we fall back to demo/mock data.
     */
    private suspend fun shouldUseLiveData(): Boolean {
        if (healthConnectManager.healthConnectClient == null) return false
        return healthConnectManager.hasAllPermissions()
    }

    override suspend fun refreshMetrics() {
        _todayMetrics.value = getTodayMetrics()
    }

    override suspend fun getTodayRisk(): RiskPrediction {
        if (!shouldUseLiveData()) {
            Timber.d("Using demo data for getTodayRisk (no Health Connect permissions)")
            return MockWellnessDataSource.getTodayRisk()
        }

        val metrics = getTodayMetrics()

        // Simple heuristic for risk level
        val riskLevel = if (metrics.sleepDurationMinutes < 300) RiskLevel.HIGH else RiskLevel.LOW
        val signals = if (riskLevel == RiskLevel.HIGH) listOf("Sleep < 5h") else emptyList()

        val explanation = if (riskLevel == RiskLevel.HIGH) "Short sleep detected." else "Wellness looks good."

        return RiskPrediction(
            riskLevel = riskLevel,
            explanation = explanation,
            contributingSignals = signals,
            date = LocalDate.now()
        )
    }

    override suspend fun getRiskByDate(date: LocalDate): RiskPrediction? {
        if (!shouldUseLiveData()) {
            return MockWellnessDataSource.getHistory().find { it.date == date }
                    ?: MockWellnessDataSource.getTodayRisk().takeIf { it.date == date }
        }
        return null // TODO real
    }

    override suspend fun getTodayMetrics(): DailyMetric {
        if (!shouldUseLiveData()) {
            Timber.d("Using demo data for getTodayMetrics (no Health Connect permissions)")
            return MockWellnessDataSource.getTodayMetrics()
        }

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
        if (!shouldUseLiveData()) return MockWellnessDataSource.getHistory()
        return emptyList() // Implement real history storage/retrieval later
    }

    override suspend fun disconnect() {
        // Clear local data if needed
    }

    override suspend fun generateExplanation(
        riskLevel: RiskLevel,
        contributingSignals: List<String>,
        metricsSummary: String
    ): String {
        return "Your wellness looks ${riskLevel.name.lowercase()} based on your activity and sleep."
    }

    override suspend fun isAvailable(): Boolean {
        // Gemini Nano / AICore requires Android 14 (API 34) or higher
        return Build.VERSION.SDK_INT >= 34
    }
}