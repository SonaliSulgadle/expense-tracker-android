package com.sonalisulgadle.expensetracker.data.local

import kotlinx.coroutines.flow.Flow

interface UserPreferencesDataSource {
    val userName: Flow<String>
    suspend fun saveUserName(name: String)
}