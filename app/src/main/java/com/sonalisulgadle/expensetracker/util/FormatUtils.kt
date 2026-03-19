package com.sonalisulgadle.expensetracker.util

import java.text.NumberFormat
import java.util.Locale

object FormatUtils {
    private val numberFormat = NumberFormat
        .getNumberInstance(Locale.US).apply {
            maximumFractionDigits = 0
        }

    fun formatAmount(amount: Double): String =
        numberFormat.format(amount)
}
