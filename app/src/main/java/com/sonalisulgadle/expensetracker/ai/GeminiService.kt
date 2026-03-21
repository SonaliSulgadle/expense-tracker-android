package com.sonalisulgadle.expensetracker.ai

import com.google.ai.client.generativeai.GenerativeModel
import com.sonalisulgadle.expensetracker.BuildConfig
import com.sonalisulgadle.expensetracker.domain.repository.CategoryRepository
import com.sonalisulgadle.expensetracker.util.Constants
import kotlinx.coroutines.delay
import kotlinx.serialization.json.Json
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

private const val AI_CONFIDENCE_NONE = 0.0
private const val MAX_RETRIES = 3
private const val RETRY_DELAY_MS = 1000L

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
        var lastException: Exception? = null

        repeat(MAX_RETRIES) { attempt ->
            try {
                val prompt = CategoryPromptBuilder.build(description)
                val response = model.generateContent(prompt)
                val rawText = response.text ?: return@repeat
                val geminiResponse = parseResponse(rawText)
                return CategoryResult(
                    category = geminiResponse.category,
                    emoji = EmojiResolver.resolve(description),
                    confidence = geminiResponse.confidence
                )
            } catch (e: Exception) {
                lastException = e
                Timber.w(e, "Gemini attempt ${attempt + 1} of $MAX_RETRIES failed")
                if (attempt < MAX_RETRIES - 1) {
                    delay(RETRY_DELAY_MS * (attempt + 1))
                }
            }
        }

        Timber.e(lastException, "All $MAX_RETRIES Gemini attempts failed, using fallback")
        return fallback(description)
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
            GeminiCategoryResponse(category = "Other", confidence = AI_CONFIDENCE_NONE)
        }
    }

    private fun fallback(description: String = "") = CategoryResult(
        category = "Other",
        emoji = EmojiResolver.resolve(description),
        confidence = 0.0
    )
}