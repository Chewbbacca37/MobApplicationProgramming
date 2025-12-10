package com.example.lab5

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Subject::class, LabWork::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dao(): StudyDao
    // Тут більше немає коду Singleton, він переїхав у KoinModule
}