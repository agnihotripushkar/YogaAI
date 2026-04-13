package me.pushkaragnihotri.yogaai.core.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface YogaSessionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(session: YogaSessionEntity)

    @Query("SELECT * FROM yoga_sessions ORDER BY completedAtEpochMs DESC")
    fun getAllSessions(): Flow<List<YogaSessionEntity>>

    @Query("DELETE FROM yoga_sessions")
    suspend fun deleteAll()
}
