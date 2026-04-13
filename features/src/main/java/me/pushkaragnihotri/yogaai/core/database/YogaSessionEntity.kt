package me.pushkaragnihotri.yogaai.core.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import me.pushkaragnihotri.yogaai.core.YogaSessionRecord

@Entity(tableName = "yoga_sessions")
data class YogaSessionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val poseName: String,
    val durationSeconds: Int,
    val completedAtEpochMs: Long,
    val isCompleted: Boolean,
    val attemptCount: Int
)

fun YogaSessionEntity.toRecord() = YogaSessionRecord(
    poseName = poseName,
    durationSeconds = durationSeconds,
    completedAtEpochMs = completedAtEpochMs,
    isCompleted = isCompleted,
    attemptCount = attemptCount
)
