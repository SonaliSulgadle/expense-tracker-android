package com.sonalisulgadle.expensetracker.data.repository

import app.cash.turbine.test
import com.sonalisulgadle.expensetracker.data.local.UserPreferencesDataSource
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@DisplayName("UserPreferencesRepositoryImpl")
class UserPreferencesRepositoryImplTest {

    private val dataSource = mockk<UserPreferencesDataSource>()
    private lateinit var repository: UserPreferencesRepositoryImpl

    @BeforeEach
    fun setup() {
        repository = UserPreferencesRepositoryImpl(dataSource)
    }

    @Nested
    @DisplayName("User name")
    inner class UserName {

        @Test
        @DisplayName("returns username from data source")
        fun returnsUsernameFromDataSource() = runTest {
            every { dataSource.userName } returns flowOf("Sonali")

            repository.userName.test {
                assertEquals("Sonali", awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }

        @Test
        @DisplayName("returns empty string when no name set")
        fun returnsEmptyWhenNoName() = runTest {
            every { dataSource.userName } returns flowOf("")

            repository.userName.test {
                assertEquals("", awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }

        @Test
        @DisplayName("emits updated name when data source changes")
        fun emitsUpdatedName() = runTest {
            every { dataSource.userName } returns flow {
                emit("")
                emit("Sonali")
            }

            repository.userName.test {
                assertEquals("", awaitItem())
                assertEquals("Sonali", awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Nested
    @DisplayName("Save username")
    inner class SaveUsername {

        @Test
        @DisplayName("delegates saveUserName to data source")
        fun delegatesToDataSource() = runTest {
            coEvery { dataSource.saveUserName(any()) } just Runs

            repository.saveUserName("Sonali")

            coVerify { dataSource.saveUserName("Sonali") }
        }
    }
}