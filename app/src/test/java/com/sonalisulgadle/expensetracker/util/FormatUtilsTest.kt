package com.sonalisulgadle.expensetracker.util

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

@DisplayName("FormatUtils")
class FormatUtilsTest {

    @Nested
    @DisplayName("formatAmount")
    inner class FormatAmount {

        @Test
        fun formatsThousandsWithComma() {
            assertEquals("8,500", FormatUtils.formatAmount(8500.0))
        }

        @Test
        fun formatsMillionsWithComma() {
            assertEquals("1,000,000", FormatUtils.formatAmount(1_000_000.0))
        }

        @Test
        fun roundsDownDecimal() {
            assertEquals("8,500", FormatUtils.formatAmount(8500.4))
        }

        @Test
        fun roundsUpDecimal() {
            assertEquals("8,501", FormatUtils.formatAmount(8500.6))
        }

        @Test
        fun handlesZero() {
            assertEquals("0", FormatUtils.formatAmount(0.0))
        }

        @ParameterizedTest(name = "{0} formats to {1}")
        @CsvSource(
            "500.0, '500'",
            "1000.0, '1,000'",
            "99999.0, '99,999'"
        )
        fun variousAmountsFormatCorrectly(amount: Double, expected: String) {
            assertEquals(expected, FormatUtils.formatAmount(amount))
        }
    }

    @Nested
    @DisplayName("formatPercentage")
    inner class FormatPercentage {

        @Test
        fun roundsToOneDecimal() {
            assertEquals("14.8", FormatUtils.formatPercentage(14.799999))
        }

        @Test
        fun roundsUpCorrectly() {
            assertEquals("14.9", FormatUtils.formatPercentage(14.85))
        }

        @Test
        fun handlesZero() {
            assertEquals("0.0", FormatUtils.formatPercentage(0.0))
        }

        @Test
        fun handles100() {
            assertEquals("100.0", FormatUtils.formatPercentage(100.0))
        }
    }

    @Nested
    @DisplayName("formatCompactAmount")
    inner class FormatCompactAmount {

        @Test
        fun showsKForThousands() {
            assertEquals("125k", FormatUtils.formatCompactAmount(124500.0))
        }

        @Test
        fun showsMForMillions() {
            assertEquals("1.5M", FormatUtils.formatCompactAmount(1_500_000.0))
        }

        @Test
        fun showsRawForUnder1000() {
            assertEquals("500", FormatUtils.formatCompactAmount(500.0))
        }
    }
}