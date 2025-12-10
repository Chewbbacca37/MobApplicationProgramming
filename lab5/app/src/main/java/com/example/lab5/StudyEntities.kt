package com.example.lab5

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Subject(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String
)

@Entity
data class LabWork(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val subjectId: Int,
    val name: String,
    val status: String,
    val comment: String
)