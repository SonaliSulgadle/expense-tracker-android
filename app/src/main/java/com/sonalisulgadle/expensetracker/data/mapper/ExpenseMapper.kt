package com.sonalisulgadle.expensetracker.data.mapper

import com.sonalisulgadle.expensetracker.data.local.ExpenseEntity
import com.sonalisulgadle.expensetracker.domain.model.Expense

fun ExpenseEntity.toDomain() = Expense(
    id = id,
    description = description,
    amount = amount,
    category = category,
    categoryEmoji = categoryEmoji,
    timestamp = timestamp,
    isAiCategorized = isAiCategorized
)

fun Expense.toEntity() = ExpenseEntity(
    id = id,
    description = description,
    amount = amount,
    category = category,
    categoryEmoji = categoryEmoji,
    timestamp = timestamp,
    isAiCategorized = isAiCategorized
)