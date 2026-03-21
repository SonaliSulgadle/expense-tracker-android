package com.sonalisulgadle.expensetracker.data.repository

import com.sonalisulgadle.expensetracker.data.local.CategoryTotal
import com.sonalisulgadle.expensetracker.data.local.ExpenseDao
import com.sonalisulgadle.expensetracker.data.mapper.toDomain
import com.sonalisulgadle.expensetracker.data.mapper.toEntity
import com.sonalisulgadle.expensetracker.domain.model.Expense
import com.sonalisulgadle.expensetracker.domain.repository.ExpenseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

class ExpenseRepositoryImpl @Inject constructor(
    private val dao: ExpenseDao
) : ExpenseRepository {

    override fun getAllExpenses(): Flow<List<Expense>> =
        dao.getAllExpenses()
            .map { entities -> entities.map { it.toDomain() } }
            .catch { e ->
                Timber.e(e, "Error fetching expenses")
                emit(emptyList())
            }

    override fun getCategoryTotals(): Flow<List<CategoryTotal>> =
        dao.getCategoryTotals()

    override suspend fun addExpense(expense: Expense): Long =
        dao.insert(expense.toEntity())

    override suspend fun deleteExpense(expense: Expense) =
        dao.delete(expense.toEntity())
}