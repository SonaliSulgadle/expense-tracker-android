package com.sonalisulgadle.expensetracker.ui.navigation

import com.sonalisulgadle.expensetracker.util.Constants

sealed class Screen(val route: String) {
    object Dashboard : Screen("dashboard")
    object ExpenseList : Screen("expense_list")
    object CategoryDetail : Screen(
        "category_detail/{${Constants.NAV_ARG_CATEGORY_NAME}}"
    ) {
        fun createRoute(categoryName: String) =
            "category_detail/$categoryName"
    }
}