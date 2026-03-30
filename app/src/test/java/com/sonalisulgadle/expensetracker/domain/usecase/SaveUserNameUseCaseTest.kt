package com.sonalisulgadle.expensetracker.domain.usecase

import com.sonalisulgadle.expensetracker.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

@DisplayName("SaveUserNameUseCase")
class SaveUserNameUseCaseTest {

    private val repository: UserPreferencesRepository = mock()
    private lateinit var useCase: SaveUserNameUseCase

    @BeforeEach
    fun setup() {
        useCase = SaveUserNameUseCase(repository)
    }

    @Nested
    @DisplayName("Save behavior")
    inner class SaveBehavior {

        @Test
        @DisplayName("valid name is saved to repository")
        fun validNameIsSaved() = runTest {
            useCase("Sonali")
            verify(repository).saveUserName("Sonali")
        }

        @Test
        @DisplayName("name is trimmed before saving")
        fun nameIsTrimmedBeforeSaving() = runTest {
            useCase("  Sonali  ")
            verify(repository).saveUserName("Sonali")
        }

        @Test
        @DisplayName("single character name is valid")
        fun singleCharacterIsValid() = runTest {
            useCase("S")
            verify(repository).saveUserName("S")
        }
    }
}