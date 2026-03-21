package com.sonalisulgadle.expensetracker.domain.model

sealed class ExpenseError : Exception() {
    class EmptyDescription : ExpenseError()
    class InvalidAmount : ExpenseError()
    class AiFailed : ExpenseError()
    class DatabaseError : ExpenseError()
    class DescriptionTooLong : ExpenseError()
    class AmountTooLarge : ExpenseError()
    class NetworkError : ExpenseError()
    data class Unknown(val unknownCause: String) : ExpenseError()
}