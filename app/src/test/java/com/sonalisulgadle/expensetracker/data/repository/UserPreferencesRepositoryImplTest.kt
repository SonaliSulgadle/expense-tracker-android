package com.sonalisulgadle.expensetracker.data.repository

import com.sonalisulgadle.expensetracker.data.local.UserPreferencesDataStore
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

@DisplayName("UserPreferencesRepositoryImpl")
class UserPreferencesRepositoryImplTest {

    private val dataStore: UserPreferencesDataStore = mock()
    private lateinit var repository: UserPreferencesRepositoryImpl

    @BeforeEach
    fun setup() {
        repository = UserPreferencesRepositoryImpl(dataStore)
    }

    @Nested
    @DisplayName("Save username")
    inner class SaveUsername {

        @Test
        @DisplayName("delegates saveUserName to DataStore")
        fun delegatesToDataStore() = runTest {
            repository.saveUserName("Sonali")
            verify(dataStore).saveUserName("Sonali")
        }
    }
}