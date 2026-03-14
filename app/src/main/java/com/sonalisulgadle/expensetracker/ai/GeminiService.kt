package com.sonalisulgadle.expensetracker.ai

import com.google.ai.client.generativeai.GenerativeModel
import com.sonalisulgadle.expensetracker.BuildConfig
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GeminiService @Inject constructor() {

    private val json = Json {
        ignoreUnknownKeys = true  // safe parsing — ignores any extra fields Gemini adds
        isLenient = true          // tolerates minor JSON formatting issues
    }

    private val model = GenerativeModel(
        modelName = "gemini-1.5-flash",  // fast + cheap, perfect for classification
        apiKey = BuildConfig.GEMINI_API_KEY
    )

    suspend fun categorize(description: String): CategoryResult {
        return try {
            val prompt = CategoryPromptBuilder.build(description)
            val response = model.generateContent(prompt)
            val rawText = response.text ?: return fallback()
            parseResponse(rawText)
        } catch (e: Exception) {
            // Never crash the app if AI fails — just return a fallback
            fallback()
        }
    }

    private fun parseResponse(raw: String): CategoryResult {
        return try {
            // Strip any accidental markdown fences Gemini might add
            val cleaned = raw
                .trim()
                .removePrefix("```json")
                .removePrefix("```")
                .removeSuffix("```")
                .trim()
            json.decodeFromString<CategoryResult>(cleaned)
        } catch (e: Exception) {
            fallback()
        }
    }

    private fun fallback() = CategoryResult(
        category = "Other",
        emoji = "💸",
        confidence = 0.0
    )
}