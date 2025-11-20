package com.example.lab4

import androidx.room.Entity
import androidx.room.PrimaryKey

// Таблиця предметів
@Entity
data class Subject(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String
)

// Таблиця лабораторних (зв'язана з предметом через subjectId)
@Entity
data class LabWork(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val subjectId: Int, // Зовнішній ключ
    val name: String,
    val status: String,
    val comment: String
)