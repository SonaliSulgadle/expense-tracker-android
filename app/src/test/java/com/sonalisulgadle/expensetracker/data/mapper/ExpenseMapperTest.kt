package com.sonalisulgadle.expensetracker.data.local

import com.sonalisulgadle.expensetracker.data.mapper.toDomain
import com.sonalisulgadle.expensetracker.data.mapper.toEntity
import com.sonalisulgadle.expensetracker.domain.model.Expense
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@DisplayName("ExpenseMapper")
class ExpenseMapperTest {

    private val timestamp = System.currentTimeMillis()

    private val entity = ExpenseEntity(
        id = 1L,
        description = "burger",
        amount = 8500.0,
        category = "Food & Drink",
        categoryEmoji = "🍔",
        timestamp = timestamp,
        isAiCategorized = true
    )

    private val domain = Expense(
        id = 1L,
        description = "burger",
        amount = 8500.0,
        category = "Food & Drink",
        categoryEmoji = "🍔",
        timestamp = timestamp,
        isAiCategorized = true
    )

    @Nested
    @DisplayName("Entity to Domain")
    inner class EntityToDomain {

        @Test
        @DisplayName("maps id correctly")
        fun mapsId() {
            assertEquals(domain.id, entity.toDomain().id)
        }

        @Test
        @DisplayName("maps description correctly")
        fun mapsDescription() {
            assertEquals(domain.description, entity.toDomain().description)
        }

        @Test
        @DisplayName("maps amount correctly")
        fun mapsAmount() {
            assertEquals(domain.amount, entity.toDomain().amount, 0.01)
        }

        @Test
        @DisplayName("maps category correctly")
        fun mapsCategory() {
            assertEquals(domain.category, entity.toDomain().category)
        }

        @Test
        @DisplayName("maps categoryEmoji correctly")
        fun mapsCategoryEmoji() {
            assertEquals(domain.categoryEmoji, entity.toDomain().categoryEmoji)
        }

        @Test
        @DisplayName("maps timestamp correctly")
        fun mapsTimestamp() {
            assertEquals(domain.timestamp, entity.toDomain().timestamp)
        }

        @Test
        @DisplayName("maps isAiCategorized correctly")
        fun mapsIsAiCategorized() {
            assertEquals(domain.isAiCategorized, entity.toDomain().isAiCategorized)
        }

        @Test
        @DisplayName("full entity maps to equal domain object")
        fun fullEntityMapsCorrectly() {
            val result = entity.toDomain()
            assertEquals(domain, result)
        }
    }

    @Nested
    @DisplayName("Domain to Entity")
    inner class DomainToEntity {

        @Test
        @DisplayName("maps id correctly")
        fun mapsId() {
            assertEquals(entity.id, domain.toEntity().id)
        }

        @Test
        @DisplayName("maps description correctly")
        fun mapsDescription() {
            assertEquals(entity.description, domain.toEntity().description)
        }

        @Test
        @DisplayName("full domain maps to equal entity object")
        fun fullDomainMapsCorrectly() {
            val result = domain.toEntity()
            assertEquals(entity, result)
        }
    }

    @Nested
    @DisplayName("Round trip")
    inner class RoundTrip {

        @Test
        @DisplayName("entity → domain → entity produces equal entity")
        fun entityRoundTrip() {
            val result = entity.toDomain().toEntity()
            assertEquals(entity, result)
        }

        @Test
        @DisplayName("domain → entity → domain produces equal domain")
        fun domainRoundTrip() {
            val result = domain.toEntity().toDomain()
            assertEquals(domain, result)
        }
    }
}