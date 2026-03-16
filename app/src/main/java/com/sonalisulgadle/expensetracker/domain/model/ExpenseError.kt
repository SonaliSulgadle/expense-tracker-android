package com.sonalisulgadle.expensetracker.domain.model

sealed class ExpenseError : Exception() {
    class EmptyDescription : ExpenseError()
    class InvalidAmount : ExpenseError()
    class AiFailed : ExpenseError()
    class DatabaseError : ExpenseError()
    data class Unknown(val unknownCause: String) : ExpenseError()
}