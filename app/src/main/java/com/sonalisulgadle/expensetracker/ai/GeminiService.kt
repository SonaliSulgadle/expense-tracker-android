package com.sonalisulgadle.expensetracker.ai

import com.google.ai.client.generativeai.GenerativeModel
import com.sonalisulgadle.expensetracker.BuildConfig
import com.sonalisulgadle.expensetracker.domain.repository.CategoryRepository
import com.sonalisulgadle.expensetracker.util.Constants
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GeminiService @Inject constructor() : CategoryRepository {

    private val json = Json {
        ignoreUnknownKeys = true  // safe parsing — to ignores any extra fields Gemini adds
        isLenient = true          // tolerates minor JSON formatting issues
    }

    private val model = GenerativeModel(
        modelName = Constants.GEMINI_MODEL,
        apiKey = BuildConfig.GEMINI_API_KEY
    )

    override suspend fun categorize(description: String): CategoryResult {
        return try {
            val prompt = CategoryPromptBuilder.build(description)
            val response = model.generateContent(prompt)
            val rawText = response.text ?: return fallback(description)
            val geminiResponse = parseResponse(rawText)

            CategoryResult(
                category = geminiResponse.category,
                emoji = EmojiResolver.resolve(description),  // local resolution
                confidence = geminiResponse.confidence
            )
        } catch (e: Exception) {
            fallback(description)
        }
    }

    private fun parseResponse(raw: String): GeminiCategoryResponse {
        return try {
            val cleaned = raw.trim()
                .removePrefix("```json")
                .removePrefix("```")
                .removeSuffix("```")
                .trim()
            json.decodeFromString<GeminiCategoryResponse>(cleaned)
        } catch (e: Exception) {
            GeminiCategoryResponse(category = "Other", confidence = 0.0)
        }
    }

    private fun fallback(description: String = "") = CategoryResult(
        category = "Other",
        emoji = EmojiResolver.resolve(description),
        confidence = 0.0
    )
}