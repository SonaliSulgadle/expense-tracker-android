package com.sonalisulgadle.expensetracker.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "expenses")
data class ExpenseEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val description: String,
    val amount: Double,
    val category: String,
    val categoryEmoji: String,
    val timestamp: Long,
    val isAiCategorized: Boolean
)