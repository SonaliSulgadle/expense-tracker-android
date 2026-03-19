package com.sonalisulgadle.expensetracker.util

import java.time.Instant
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId
import java.time.format.DateTimeFormatter

object DateUtils {
    private val monthYearFormatter =
        DateTimeFormatter.ofPattern("MMMM yyyy")
    private val shortDateFormatter =
        DateTimeFormatter.ofPattern("MMM d")
    private val monthYearGroupFormatter =
        DateTimeFormatter.ofPattern("MMMM yyyy")

    fun formatMonthYear(timestamp: Long): String =
        Instant.ofEpochMilli(timestamp)
            .atZone(ZoneId.systemDefault())
            .format(monthYearFormatter)

    fun formatShortDate(timestamp: Long): String =
        Instant.ofEpochMilli(timestamp)
            .atZone(ZoneId.systemDefault())
            .format(shortDateFormatter)

    fun formatCurrentMonth(): String =
        LocalDate.now().format(monthYearGroupFormatter)

    fun getDaysOfMonth(): Int =
        YearMonth.now().lengthOfMonth()
}
