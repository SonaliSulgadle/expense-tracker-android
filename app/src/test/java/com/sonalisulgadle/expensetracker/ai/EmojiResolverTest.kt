package com.sonalisulgadle.expensetracker.ai

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.ValueSource

@DisplayName("EmojiResolver")
class EmojiResolverTest {

    @Nested
    @DisplayName("Default fallback")
    inner class DefaultFallback {

        @Test
        @DisplayName("empty string returns default emoji")
        fun emptyStringReturnsDefault() {
            assertEquals("💸", EmojiResolver.resolve(""))
        }

        @Test
        @DisplayName("blank string returns default emoji")
        fun blankStringReturnsDefault() {
            assertEquals("💸", EmojiResolver.resolve("   "))
        }

        @Test
        @DisplayName("unknown description returns default emoji")
        fun unknownDescriptionReturnsDefault() {
            assertEquals("💸", EmojiResolver.resolve("xyzabc123"))
        }
    }

    @Nested
    @DisplayName("Case insensitivity")
    inner class CaseInsensitivity {

        @ParameterizedTest(name = "{0} returns burger emoji")
        @ValueSource(strings = ["BURGER", "burger", "Burger", "BuRgEr"])
        @DisplayName("burger in any case returns burger emoji")
        fun burgerInAnyCaseReturnsBurgerEmoji(input: String) {
            assertEquals("🍔", EmojiResolver.resolve(input))
        }

        @ParameterizedTest(name = "{0} returns coffee emoji")
        @ValueSource(strings = ["STARBUCKS", "starbucks", "Starbucks"])
        fun starbucksInAnyCaseReturnsCoffeeEmoji(input: String) {
            assertEquals("☕", EmojiResolver.resolve(input))
        }
    }
    @Nested
    @DisplayName("Substring case")
    inner class SubStringCase {

        @Test
        @DisplayName("skincare does not match car rule")
        fun skincareDoesNotMatchCarRule() {
            val result = EmojiResolver.resolve("olive young skincare")
            assertEquals("💄", result)
            assertNotEquals("🚗", result)
        }

        @Test
        @DisplayName("car as standalone word matches car rule")
        fun carAsStandaloneWordMatchesCarRule() {
            assertEquals("🚗", EmojiResolver.resolve("car wash"))
        }
    }

    @Nested
    @DisplayName("Food and Drink")
    inner class FoodAndDrink {

        @Test
        fun burgerReturnsBurgerEmoji() {
            assertEquals("🍔", EmojiResolver.resolve("burger combo"))
        }

        @Test
        fun mcdonaldReturnsBurgerEmoji() {
            assertEquals("🍔", EmojiResolver.resolve("McDonald's lunch"))
        }

        @Test
        fun coffeeReturnsCoffeeEmoji() {
            assertEquals("☕", EmojiResolver.resolve("coffee"))
        }

        @Test
        fun pizzaReturnsPizzaEmoji() {
            assertEquals("🍕", EmojiResolver.resolve("pizza delivery"))
        }

        @Test
        fun lunchReturnsMealEmoji() {
            assertEquals("🍽️", EmojiResolver.resolve("lunch with team"))
        }

        @ParameterizedTest(name = "{0} → coffee emoji")
        @CsvSource(
            "starbucks latte, ☕",
            "hollys coffee, ☕",
            "ediya americano, ☕",
            "cafe visit, ☕"
        )
        @DisplayName("various coffee places return coffee emoji")
        fun coffeePlacesReturnCoffeeEmoji(input: String, expected: String) {
            assertEquals(expected, EmojiResolver.resolve(input))
        }
    }

    @Nested
    @DisplayName("Transport")
    inner class Transport {

        @Test
        fun uberReturnsTaxiEmoji() {
            assertEquals("🚕", EmojiResolver.resolve("uber ride"))
        }

        @Test
        fun metroReturnsMetroEmoji() {
            assertEquals("🚇", EmojiResolver.resolve("seoul metro top-up"))
        }
    }

    @Nested
    @DisplayName("Health")
    inner class Health {

        @Test
        fun gymReturnsGymEmoji() {
            assertEquals("💪", EmojiResolver.resolve("gym membership"))
        }

        @Test
        fun hospitalReturnsHospitalEmoji() {
            assertEquals("🏥", EmojiResolver.resolve("hospital visit"))
        }
    }

    @Nested
    @DisplayName("Bills")
    inner class Bills {

        @Test
        fun electricityReturnsElectricEmoji() {
            assertEquals("⚡", EmojiResolver.resolve("electricity bill"))
        }
    }

    @Nested
    @DisplayName("Null safety")
    inner class NullSafety {

        @Test
        @DisplayName("resolve never returns empty string")
        fun resolveNeverReturnsEmpty() {
            assertNotEquals("", EmojiResolver.resolve("anything"))
        }

        @Test
        @DisplayName("resolve always returns a value")
        fun resolveAlwaysReturnsValue() {
            assertNotNull(EmojiResolver.resolve("anything"))
        }
    }
}