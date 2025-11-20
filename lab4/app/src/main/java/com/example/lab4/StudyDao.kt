package com.example.lab4

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface StudyDao {
    // Отримати список предметів. Flow означає, що список оновиться сам, якщо щось зміниться.
    @Query("SELECT * FROM Subject")
    fun getSubjects(): Flow<List<Subject>>

    // Отримати лаби конкретного предмету
    @Query("SELECT * FROM LabWork WHERE subjectId = :subId")
    fun getLabsBySubject(subId: Int): Flow<List<LabWork>>

    // Оновити лабу (змінити статус чи комент)
    @Update
    suspend fun updateLab(lab: LabWork)

    // Вставка даних (для початкового заповнення)
    @Insert
    suspend fun insertSubject(subject: Subject): Long

    @Insert
    suspend fun insertLab(lab: LabWork)

    // Видалення
    @Delete
    suspend fun deleteSubject(subject: Subject)

    @Delete
    suspend fun deleteLab(lab: LabWork)
}