package com.vovan.lab7.data

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.GenerativeBackend
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.vovan.lab7.data.entity.TextPair
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GeminiAIRepository {
    companion object {
        private const val MODEL = "gemini-2.5-flash"
    }

    private val aiModel = Firebase.ai(backend = GenerativeBackend.googleAI())
        .generativeModel(MODEL)

    private val gson = Gson()

    suspend fun generateTextParList(): List<TextPair>? {
        return generateTriviaQuestions("General Knowledge")
    }

    // Новий метод, що приймає тему
    suspend fun generateTriviaQuestions(topic: String): List<TextPair>? {
        return try {
            withContext(Dispatchers.IO) {
                // Динамічний промпт
                val prompt = """
                    Generate 5 trivia questions with short answers about "$topic".
                    Return ONLY a valid JSON array in this format:
                    [
                      {"text1": "Question", "text2": "Answer"},
                      ...
                    ]
                """.trimIndent()

                val response = aiModel.generateContent(prompt)
                val outputRaw = response.text ?: ""

                val outputJson = outputRaw
                    .replace(Regex("```json|```"), "")
                    .trim()

                val type = object : TypeToken<List<TextPair>>() {}.type
                val textPairList: List<TextPair> = gson.fromJson(outputJson, type)

                Log.i("GeminiAIRepository", "Questions for $topic: $textPairList")
                textPairList
            }
        } catch (e: Exception) {
            Log.e("GeminiAIRepository", "Error generating questions: $e")
            null
        }
    }
}