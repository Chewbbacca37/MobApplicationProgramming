package com.vovan.lab7.ui.screens.subjectDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vovan.lab7.data.GeminiAIRepository
import com.vovan.lab7.data.entity.TextPair
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class GameScreenViewModel(
    val geminiAIRepository: GeminiAIRepository
) : ViewModel() {

    private val _isLoadingStateFlow = MutableStateFlow<Boolean>(false)
    val isLoadingStateFlow: StateFlow<Boolean>
        get() = _isLoadingStateFlow

    private val _textPairListStateFlow = MutableStateFlow<List<TextPair>?>(null)
    val textPairListStateFlow: StateFlow<List<TextPair>?>
        get() = _textPairListStateFlow

    // Тепер приймаємо topic
    fun requestGameData(topic: String) {
        viewModelScope.launch {
            _isLoadingStateFlow.value = true
            // Викликаємо метод репозиторію з темою
            _textPairListStateFlow.value = geminiAIRepository.generateTriviaQuestions(topic)
            _isLoadingStateFlow.value = false
        }
    }
}