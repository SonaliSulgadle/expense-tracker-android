package com.sonalisulgadle.expensetracker.ai

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@DisplayName("CategoryPromptBuilder")
class CategoryPromptBuilderTest {

    @Nested
    @DisplayName("Prompt content")
    inner class PromptContent {

        @Test
        @DisplayName("prompt contains the expense description")
        fun containsDescription() {
            val prompt = CategoryPromptBuilder.build("starbucks latte")
            assertTrue(prompt.contains("starbucks latte"))
        }

        @Test
        @DisplayName("prompt contains all category names")
        fun containsAllCategories() {
            val prompt = CategoryPromptBuilder.build("anything")
            assertTrue(prompt.contains("Food & Drink"))
            assertTrue(prompt.contains("Transport"))
            assertTrue(prompt.contains("Shopping"))
            assertTrue(prompt.contains("Health"))
            assertTrue(prompt.contains("Entertainment"))
            assertTrue(prompt.contains("Bills & Utilities"))
            assertTrue(prompt.contains("Other"))
        }

        @Test
        @DisplayName("prompt instructs to return JSON only")
        fun instructsJsonOnly() {
            val prompt = CategoryPromptBuilder.build("anything")
            assertTrue(prompt.contains("JSON"))
        }

        @Test
        @DisplayName("prompt is not empty")
        fun promptIsNotEmpty() {
            val prompt = CategoryPromptBuilder.build("burger")
            assertTrue(prompt.isNotBlank())
        }

        @Test
        @DisplayName("prompt includes confidence field instruction")
        fun includesConfidenceInstruction() {
            val prompt = CategoryPromptBuilder.build("anything")
            assertTrue(prompt.contains("confidence"))
        }
    }
}