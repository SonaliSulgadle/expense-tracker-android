package com.sonalisulgadle.expensetracker.domain.usecase

import com.sonalisulgadle.expensetracker.domain.repository.UserPreferencesRepository
import javax.inject.Inject

class SaveUserNameUseCase @Inject constructor(
    private val repository: UserPreferencesRepository
) {
    suspend operator fun invoke(name: String) {
        repository.saveUserName(name.trim())
    }
}