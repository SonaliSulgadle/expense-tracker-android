package com.sonalisulgadle.expensetracker.data.repository

import com.sonalisulgadle.expensetracker.data.local.UserPreferencesDataStore
import com.sonalisulgadle.expensetracker.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserPreferencesRepositoryImpl @Inject constructor(
    private val dataStore: UserPreferencesDataStore
) : UserPreferencesRepository {

    override val userName: Flow<String> = dataStore.userName

    override suspend fun saveUserName(name: String) =
        dataStore.saveUserName(name)
}