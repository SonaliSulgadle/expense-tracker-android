package com.sonalisulgadle.expensetracker.ai

object CategoryPromptBuilder {

    private val CATEGORIES = listOf(
        "Food & Drink",
        "Transport",
        "Shopping",
        "Health",
        "Entertainment",
        "Bills & Utilities",
        "Travel",
        "Education",
        "Other"
    )

    fun build(description: String): String = """
        You are an expense categorizer for a personal finance app.

        Given an expense description, return ONLY a valid JSON object.

        Available categories: ${CATEGORIES.joinToString(", ")}

        Rules:
        - Pick the single most relevant category from the list above
        - Confidence is a number between 0.0 and 1.0
        - Return ONLY the JSON object
        - No markdown, no explanation, no backticks, no extra text

        Expense description: "$description"

        Respond with exactly this format:
        {"category": "Food & Drink", "confidence": 0.95}
""".trimIndent()
}