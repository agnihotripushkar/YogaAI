package me.pushkaragnihotri.yogaai.features.home.data

import me.pushkaragnihotri.yogaai.core.YogaSessionRecord
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

object YogaPracticeStats {
    fun streakDays(sessions: List<YogaSessionRecord>): Int {
        if (sessions.isEmpty()) return 0
        val zone = ZoneId.systemDefault()
        val daysWithSession = sessions.map {
            Instant.ofEpochMilli(it.completedAtEpochMs).atZone(zone).toLocalDate()
        }.toSet()
        var streak = 0
        var d = LocalDate.now(zone)
        while (d in daysWithSession) {
            streak++
            d = d.minusDays(1)
        }
        return streak
    }

    fun sessionsLast7Days(sessions: List<YogaSessionRecord>): Int {
        val weekAgo = System.currentTimeMillis() - 7L * 24 * 60 * 60 * 1000
        return sessions.count { it.completedAtEpochMs >= weekAgo }
    }
}
