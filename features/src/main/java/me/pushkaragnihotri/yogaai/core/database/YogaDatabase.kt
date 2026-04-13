package me.pushkaragnihotri.yogaai.core.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [YogaSessionEntity::class],
    version = 1,
    exportSchema = false
)
abstract class YogaDatabase : RoomDatabase() {
    abstract fun yogaSessionDao(): YogaSessionDao
}
