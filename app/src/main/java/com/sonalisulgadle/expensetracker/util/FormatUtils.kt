package com.sonalisulgadle.expensetracker.util

import java.text.NumberFormat
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Locale

fun formatAmount(amount: Double): String {
    return NumberFormat.getNumberInstance(Locale.US).apply {
        maximumFractionDigits = 0
    }.format(amount)
}

fun formatCurrentMonth(): String =
    LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM yyyy"))

fun getDaysOfMonth(): Int = YearMonth.now().lengthOfMonth()