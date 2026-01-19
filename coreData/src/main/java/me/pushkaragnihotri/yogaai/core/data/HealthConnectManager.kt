package me.pushkaragnihotri.yogaai.core.data

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
import android.util.Log

private const val TAG = "HealthConnectManager"

class HealthConnectManager(private val context: Context) {
    
    val healthConnectClient by lazy { HealthConnectClient.getOrCreate(context) }

    val permissions = setOf(
        HealthPermission.getReadPermission(StepsRecord::class),
        HealthPermission.getReadPermission(HeartRateRecord::class),
        HealthPermission.getReadPermission(SleepSessionRecord::class),
        HealthPermission.getReadPermission(TotalCaloriesBurnedRecord::class)
    )

    suspend fun hasAllPermissions(): Boolean {
        val granted = healthConnectClient.permissionController.getGrantedPermissions()
        val hasAll = granted.containsAll(permissions)
        Log.d(TAG, "hasAllPermissions: $hasAll (granted: $granted, required: $permissions)")
        return hasAll
    }

    fun checkAvailability(): Int {
        val status = HealthConnectClient.getSdkStatus(context)
        Log.d(TAG, "Health Connect SDK status: $status")
        return status
    }

    suspend fun readSteps(startTime: Instant, endTime: Instant): Long {
        Log.d(TAG, "readSteps called from $startTime to $endTime")
        if (!hasAllPermissions()) {
            Log.w(TAG, "readSteps: Permissions not granted")
            return 0
        }
        
        // Try reading aggregated steps first as it's more efficient for totals
        return try {
             val response = healthConnectClient.aggregate(
                AggregateRequest(
                    metrics = setOf(StepsRecord.COUNT_TOTAL),
                    timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
                )
            )
            val steps = response[StepsRecord.COUNT_TOTAL] ?: 0L
            Log.d(TAG, "readSteps (aggregated): $steps")
            steps
        } catch (e: Exception) {
            Log.e(TAG, "readSteps (aggregation failed)", e)
            // Fallback to reading records if aggregation fails or is not supported in the way we expect
            try {
                val response = healthConnectClient.readRecords(
                    ReadRecordsRequest(
                        StepsRecord::class,
                        timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
                    )
                )
                val totalSteps = response.records.sumOf { it.count }
                Log.d(TAG, "readSteps (fallback records): $totalSteps")
                totalSteps
            } catch (e2: Exception) {
                Log.e(TAG, "readSteps (fallback failed)", e2)
                0L
            }
        }
    }

    suspend fun readSleepSessions(startTime: Instant, endTime: Instant): List<SleepSessionRecord> {
        Log.d(TAG, "readSleepSessions called from $startTime to $endTime")
        if (!hasAllPermissions()) {
            Log.w(TAG, "readSleepSessions: Permissions not granted")
            return emptyList()
        }
        
        return try {
            val response = healthConnectClient.readRecords(
                ReadRecordsRequest(
                    SleepSessionRecord::class,
                    timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
                )
            )
            Log.d(TAG, "readSleepSessions found ${response.records.size} records")
            response.records
        } catch (e: Exception) {
            Log.e(TAG, "readSleepSessions failed", e)
            emptyList()
        }
    }

    suspend fun readHeartRate(startTime: Instant, endTime: Instant): List<HeartRateRecord> {
        Log.d(TAG, "readHeartRate called from $startTime to $endTime")
        if (!hasAllPermissions()) {
            Log.w(TAG, "readHeartRate: Permissions not granted")
            return emptyList()
        }
        
        return try {
            val response = healthConnectClient.readRecords(
                ReadRecordsRequest(
                    HeartRateRecord::class,
                    timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
                )
            )
            Log.d(TAG, "readHeartRate found ${response.records.size} records")
            response.records
        } catch (e: Exception) {
            Log.e(TAG, "readHeartRate failed", e)
            emptyList()
        }
    }

    suspend fun readCalories(startTime: Instant, endTime: Instant): Double {
        Log.d(TAG, "readCalories called from $startTime to $endTime")
        if (!hasAllPermissions()) {
            Log.w(TAG, "readCalories: Permissions not granted")
            return 0.0
        }
        return try {
             val response = healthConnectClient.aggregate(
                AggregateRequest(
                    metrics = setOf(TotalCaloriesBurnedRecord.ENERGY_TOTAL),
                    timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
                )
            )
            val calories = response[TotalCaloriesBurnedRecord.ENERGY_TOTAL]?.inKilocalories ?: 0.0
            Log.d(TAG, "readCalories: $calories")
            calories
        } catch (e: Exception) {
            Log.e(TAG, "readCalories failed", e)
            0.0
        }
    }
}
