package com.example.lab5

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface StudyDao {
    @Query("SELECT * FROM Subject")
    fun getSubjects(): Flow<List<Subject>>

    @Query("SELECT * FROM LabWork WHERE subjectId = :subId")
    fun getLabsBySubject(subId: Int): Flow<List<LabWork>>

    @Update
    suspend fun updateLab(lab: LabWork)

    @Insert
    suspend fun insertSubject(subject: Subject): Long

    @Insert
    suspend fun insertLab(lab: LabWork)

    @Delete
    suspend fun deleteSubject(subject: Subject)

    @Delete
    suspend fun deleteLab(lab: LabWork)
}