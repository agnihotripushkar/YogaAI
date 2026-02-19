package me.pushkaragnihotri.yogaai.core

import android.content.Context
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.HeartRateRecord
import androidx.health.connect.client.records.SleepSessionRecord
import androidx.health.connect.client.records.StepsRecord
import androidx.health.connect.client.records.TotalCaloriesBurnedRecord
import androidx.health.connect.client.request.AggregateRequest
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter
import java.time.Instant
import timber.log.Timber

class HealthConnectManager(private val context: Context) {
    
    companion object {
        const val SDK_AVAILABLE = 1
        const val SDK_UNAVAILABLE = 2
        const val SDK_UPDATE_REQUIRED = 3
    }
    
    val healthConnectClient: HealthConnectClient? by lazy {
        try {
            if (HealthConnectClient.getSdkStatus(context) == HealthConnectClient.SDK_AVAILABLE) {
                HealthConnectClient.getOrCreate(context)
            } else {
                Timber.w("Health Connect SDK not available on this device")
                null
            }
        } catch (e: Exception) {
            Timber.e(e, "Failed to create HealthConnectClient")
            null
        }
    }

    val permissions = setOf(
        HealthPermission.getReadPermission(StepsRecord::class),
        HealthPermission.getReadPermission(HeartRateRecord::class),
        HealthPermission.getReadPermission(SleepSessionRecord::class),
        HealthPermission.getReadPermission(TotalCaloriesBurnedRecord::class)
    )

    suspend fun hasAllPermissions(): Boolean {
        val client = healthConnectClient ?: return false
        val granted = client.permissionController.getGrantedPermissions()
        val hasAll = granted.containsAll(permissions)
        Timber.d("hasAllPermissions: $hasAll (granted: $granted, required: $permissions)")
        return hasAll
    }

    fun checkAvailability(): Int {
        val status = HealthConnectClient.getSdkStatus(context)
        val mappedStatus = when (status) {
            HealthConnectClient.SDK_AVAILABLE -> SDK_AVAILABLE
            HealthConnectClient.SDK_UNAVAILABLE_PROVIDER_UPDATE_REQUIRED -> SDK_UPDATE_REQUIRED
            HealthConnectClient.SDK_UNAVAILABLE -> SDK_UNAVAILABLE
            else -> SDK_UNAVAILABLE
        }
        Timber.d("Health Connect SDK status: $status (mapped to: $mappedStatus)")
        return mappedStatus
    }

    suspend fun readSteps(startTime: Instant, endTime: Instant): Long {
        Timber.d("readSteps called from $startTime to $endTime")
        if (!hasAllPermissions()) {
            Timber.w("readSteps: Permissions not granted")
            return 0
        }
        
        // Try reading aggregated steps first as it's more efficient for totals
        return try {
             val client = healthConnectClient ?: return 0L
             val response = client.aggregate(
                AggregateRequest(
                    metrics = setOf(StepsRecord.COUNT_TOTAL),
                    timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
                )
            )
            val steps = response[StepsRecord.COUNT_TOTAL] ?: 0L
            Timber.d("readSteps (aggregated): $steps")
            steps
        } catch (e: Exception) {
            Timber.e(e, "readSteps (aggregation failed)")
            // Fallback to reading records if aggregation fails or is not supported in the way we expect
            try {
                val client2 = healthConnectClient ?: return 0L
                val response = client2.readRecords(
                    ReadRecordsRequest(
                        StepsRecord::class,
                        timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
                    )
                )
                val totalSteps = response.records.sumOf { it.count }
                Timber.d("readSteps (fallback records): $totalSteps")
                totalSteps
            } catch (e2: Exception) {
                Timber.e(e2, "readSteps (fallback failed)")
                0L
            }
        }
    }

    suspend fun readSleepSessions(startTime: Instant, endTime: Instant): List<SleepSessionRecord> {
        Timber.d("readSleepSessions called from $startTime to $endTime")
        if (!hasAllPermissions()) {
            Timber.w("readSleepSessions: Permissions not granted")
            return emptyList()
        }
        
        return try {
            val client = healthConnectClient ?: return emptyList()
            val response = client.readRecords(
                ReadRecordsRequest(
                    SleepSessionRecord::class,
                    timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
                )
            )
            Timber.d("readSleepSessions found ${response.records.size} records")
            response.records
        } catch (e: Exception) {
            Timber.e(e, "readSleepSessions failed")
            emptyList()
        }
    }

    suspend fun readHeartRate(startTime: Instant, endTime: Instant): List<HeartRateRecord> {
        Timber.d("readHeartRate called from $startTime to $endTime")
        if (!hasAllPermissions()) {
            Timber.w("readHeartRate: Permissions not granted")
            return emptyList()
        }
        
        return try {
            val client = healthConnectClient ?: return emptyList()
            val response = client.readRecords(
                ReadRecordsRequest(
                    HeartRateRecord::class,
                    timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
                )
            )
            Timber.d("readHeartRate found ${response.records.size} records")
            response.records
        } catch (e: Exception) {
            Timber.e(e, "readHeartRate failed")
            emptyList()
        }
    }

    suspend fun readCalories(startTime: Instant, endTime: Instant): Double {
        Timber.d("readCalories called from $startTime to $endTime")
        if (!hasAllPermissions()) {
            Timber.w("readCalories: Permissions not granted")
            return 0.0
        }
        return try {
             val client = healthConnectClient ?: return 0.0
             val response = client.aggregate(
                AggregateRequest(
                    metrics = setOf(TotalCaloriesBurnedRecord.ENERGY_TOTAL),
                    timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
                )
            )
            val calories = response[TotalCaloriesBurnedRecord.ENERGY_TOTAL]?.inKilocalories ?: 0.0
            Timber.d("readCalories: $calories")
            calories
        } catch (e: Exception) {
            Timber.e(e, "readCalories failed")
            0.0
        }
    }
}
