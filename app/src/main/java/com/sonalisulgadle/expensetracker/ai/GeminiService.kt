package com.sonalisulgadle.expensetracker.ai

import com.sonalisulgadle.expensetracker.BuildConfig
import com.sonalisulgadle.expensetracker.domain.repository.CategoryRepository
import kotlinx.coroutines.delay
import kotlinx.serialization.json.Json
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

private const val MAX_RETRIES = 3
private const val RETRY_DELAY_MS = 1000L
private const val AI_CONFIDENCE_NONE = 0.0

@Singleton
class GeminiService @Inject constructor(
    private val geminiApi: GeminiApi
) : CategoryRepository {

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    override suspend fun categorize(description: String): CategoryResult {
        var lastException: Exception? = null

        repeat(MAX_RETRIES) { attempt ->
            try {
                val response = geminiApi.generateContent(
                    apiKey = BuildConfig.GEMINI_API_KEY,
                    request = buildRequest(description)
                )
                val rawText = extractText(response) ?: return@repeat
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

        Timber.e(lastException, "All Gemini attempts failed, using fallback")
        return fallback(description)
    }

    private fun buildRequest(description: String): GeminiRequest =
        GeminiRequest(
            contents = listOf(
                GeminiContent(
                    parts = listOf(
                        GeminiPart(
                            text = CategoryPromptBuilder.build(description)
                        )
                    )
                )
            )
        )

    private fun extractText(response: GeminiResponse): String? =
        response.candidates
            .firstOrNull()
            ?.content
            ?.parts
            ?.firstOrNull()
            ?.text

    private fun parseResponse(raw: String): GeminiCategoryResponse {
        return try {
            val cleaned = raw.trim()
                .removePrefix("```json")
                .removePrefix("```")
                .removeSuffix("```")
                .trim()
            json.decodeFromString<GeminiCategoryResponse>(cleaned)
        } catch (e: Exception) {
            Timber.e(e, "Failed to parse Gemini response: $raw")
            GeminiCategoryResponse(category = "Other", confidence = 0.0)
        }
    }

    private fun fallback(description: String = "") = CategoryResult(
        category = "Other",
        emoji = EmojiResolver.resolve(description),
        confidence = AI_CONFIDENCE_NONE
    )
}