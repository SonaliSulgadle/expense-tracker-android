package com.sonalisulgadle.expensetracker.ai

import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface GeminiApi {

    @POST("v1beta/models/gemini-2.5-flash-lite:generateContent")
    suspend fun generateContent(
        @Header("x-goog-api-key") apiKey: String,
        @Body request: GeminiRequest
    ): GeminiResponse
}