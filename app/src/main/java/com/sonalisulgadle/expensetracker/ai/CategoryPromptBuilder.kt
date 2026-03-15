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
        - Choose an emoji that represents the expense (not the category)
        - Confidence is a number between 0.0 and 1.0
        - Return ONLY the JSON object, no markdown, no explanation, no backticks
        
        Expense description: "$description"
        
        Response format:
        {"category": "Food & Drink", "emoji": "🍕", "confidence": 0.95}
    """.trimIndent()
}