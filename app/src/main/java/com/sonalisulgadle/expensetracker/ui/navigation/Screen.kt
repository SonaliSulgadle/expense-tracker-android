package com.sonalisulgadle.expensetracker.ui.navigation

import com.sonalisulgadle.expensetracker.util.Constants
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

sealed class Screen(val route: String) {
    object Dashboard : Screen("dashboard")
    object ExpenseList : Screen("expense_list")
    object CategoryDetail : Screen(
        "category_detail/{${Constants.NAV_ARG_CATEGORY_NAME}}/{${Constants.NAV_ARG_CATEGORY_EMOJI}}"
    ) {
        fun createRoute(categoryName: String, categoryEmoji: String): String {
            val encodedName = URLEncoder.encode(
                categoryName,
                StandardCharsets.UTF_8.toString()
            )
            val encodedEmoji = URLEncoder.encode(
                categoryEmoji,
                StandardCharsets.UTF_8.toString()
            )
            return "category_detail/$encodedName/$encodedEmoji"
        }
    }
}