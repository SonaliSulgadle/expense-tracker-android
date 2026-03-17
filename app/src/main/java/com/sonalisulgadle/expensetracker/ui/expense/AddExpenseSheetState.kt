package com.sonalisulgadle.expensetracker.ui.expense

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.sonalisulgadle.expensetracker.R

class AddExpenseSheetState {
    var description by mutableStateOf("")
    var amount by mutableStateOf("")
    var descriptionError by mutableStateOf<Int?>(null)
    var amountError by mutableStateOf<Int?>(null)

    fun onDescriptionChange(value: String) {
        description = value
        descriptionError = null   // clear error on type
    }

    fun onAmountChange(value: String) {
        amount = value
        amountError = null        // clear error on type
    }

    fun validate(): Boolean {
        var valid = true
        if (description.isBlank()) {
            descriptionError = R.string.error_empty_description
            valid = false
        }
        val parsed = amount.toDoubleOrNull()
        if (parsed == null || parsed <= 0) {
            amountError = R.string.error_invalid_amount
            valid = false
        }
        return valid
    }

    fun parsedAmount(): Double? = amount.toDoubleOrNull()

    fun reset() {
        description = ""
        amount = ""
        descriptionError = null
        amountError = null
    }
}

@Composable
fun rememberAddExpenseSheetState() = remember { AddExpenseSheetState() }