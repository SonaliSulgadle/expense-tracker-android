package com.sonalisulgadle.expensetracker.util

import java.math.RoundingMode
import java.text.NumberFormat
import java.util.Locale

object FormatUtils {
    private val numberFormat = NumberFormat
        .getNumberInstance(Locale.US).apply {
            maximumFractionDigits = 0
        }

    fun formatAmount(amount: Double): String =
        numberFormat.format(amount)

    fun formatPercentage(value: Double): String =
        value.toBigDecimal()
            .setScale(1, RoundingMode.HALF_UP)
            .toPlainString()

    fun formatCompactAmount(amount: Double): String {
        return when {
            amount >= 1_000_000 -> "${(amount / 1_000_000).toBigDecimal().setScale(1, RoundingMode.HALF_UP)}M"
            amount >= 1_000 -> "${(amount / 1_000).toBigDecimal().setScale(0, RoundingMode.HALF_UP)}k"
            else -> formatAmount(amount)
        }
    }
}
