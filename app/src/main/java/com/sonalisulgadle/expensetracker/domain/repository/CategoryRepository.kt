package com.sonalisulgadle.expensetracker.domain.repository

import com.sonalisulgadle.expensetracker.ai.CategoryResult

interface CategoryRepository {
    suspend fun categorize(description: String): CategoryResult
}