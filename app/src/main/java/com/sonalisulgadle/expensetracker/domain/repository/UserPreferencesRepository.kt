package com.sonalisulgadle.expensetracker.domain.repository

import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
    val userName: Flow<String>
    suspend fun saveUserName(name: String)
}