package com.example.lab4

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class StudyViewModel(app: Application) : AndroidViewModel(app) {
    // Ініціалізація бази
    private val db = AppDatabase.getDatabase(app)
    private val dao = db.dao()

    // Дані для екранів
    val subjects: Flow<List<Subject>> = dao.getSubjects()

    fun getLabs(subjectId: Int): Flow<List<LabWork>> {
        return dao.getLabsBySubject(subjectId)
    }

    // Збереження змін
    fun updateLab(lab: LabWork) {
        viewModelScope.launch {
            dao.updateLab(lab)
        }
    }

    // Додавання та видалення
    fun addSubject(name: String) {
        viewModelScope.launch {
            dao.insertSubject(Subject(name = name))
        }
    }

    fun addLab(subjectId: Int, name: String) {
        viewModelScope.launch {
            dao.insertLab(LabWork(subjectId = subjectId, name = name, status = "Не почато", comment = ""))
        }
    }

    fun deleteSubject(subject: Subject) {
        viewModelScope.launch { dao.deleteSubject(subject) }
    }

    fun deleteLab(lab: LabWork) {
        viewModelScope.launch { dao.deleteLab(lab) }
    }
}