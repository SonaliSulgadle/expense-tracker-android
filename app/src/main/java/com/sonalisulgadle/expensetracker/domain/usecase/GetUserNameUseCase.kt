package com.sonalisulgadle.expensetracker.domain.usecase

import com.sonalisulgadle.expensetracker.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserNameUseCase @Inject constructor(
    private val repository: UserPreferencesRepository
) {
    operator fun invoke(): Flow<String> = repository.userName
}