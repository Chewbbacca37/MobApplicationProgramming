package com.example.lab5

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

// Приймаємо dao через конструктор (Koin сам його сюди покладе)
class StudyViewModel(private val dao: StudyDao) : ViewModel() {

    val subjects: Flow<List<Subject>> = dao.getSubjects()

    fun getLabs(subjectId: Int): Flow<List<LabWork>> {
        return dao.getLabsBySubject(subjectId)
    }

    fun addSubject(name: String) {
        viewModelScope.launch { dao.insertSubject(Subject(name = name)) }
    }

    fun addLab(subjectId: Int, name: String) {
        viewModelScope.launch {
            dao.insertLab(LabWork(subjectId = subjectId, name = name, status = "New", comment = ""))
        }
    }

    fun updateLab(lab: LabWork) {
        viewModelScope.launch { dao.updateLab(lab) }
    }

    fun deleteSubject(subject: Subject) {
        viewModelScope.launch { dao.deleteSubject(subject) }
    }

    fun deleteLab(lab: LabWork) {
        viewModelScope.launch { dao.deleteLab(lab) }
    }
}