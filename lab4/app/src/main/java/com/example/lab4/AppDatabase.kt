package com.example.lab4

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Subject::class, LabWork::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dao(): StudyDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "study_db"
                )
                    .addCallback(PrepopulateCallback(context)) // Додаємо колбек
                    .build()
                    .also { INSTANCE = it }
            }
        }

        // Клас для заповнення бази при першому запуску
        private class PrepopulateCallback(private val context: Context) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                CoroutineScope(Dispatchers.IO).launch {
                    val dao = getDatabase(context).dao()

                    val mathId = dao.insertSubject(Subject(name = "Android Dev"))
                    dao.insertLab(LabWork(subjectId = mathId.toInt(), name = "Lab 1: Intro", status = "Здано", comment = "Easy"))
                    dao.insertLab(LabWork(subjectId = mathId.toInt(), name = "Lab 2: UI", status = "В процесі", comment = "In process"))

                    val historyId = dao.insertSubject(Subject(name = "History"))
                    dao.insertLab(LabWork(subjectId = historyId.toInt(), name = "essay", status = "Не почато", comment = ""))
                }
            }
        }
    }
}