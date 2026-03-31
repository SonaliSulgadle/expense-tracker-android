package com.sonalisulgadle.expensetracker.data.repository

import com.sonalisulgadle.expensetracker.data.local.UserPreferencesDataSource
import com.sonalisulgadle.expensetracker.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserPreferencesRepositoryImpl @Inject constructor(
    private val dataStore: UserPreferencesDataSource
) : UserPreferencesRepository {

    override val userName: Flow<String>
        get() = dataStore.userName

    override suspend fun saveUserName(name: String) =
        dataStore.saveUserName(name)
}