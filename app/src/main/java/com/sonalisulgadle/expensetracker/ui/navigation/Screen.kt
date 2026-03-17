package com.sonalisulgadle.expensetracker.ui.navigation

sealed class Screen(val route: String) {
    object Dashboard : Screen("dashboard")
    object ExpenseList : Screen("expense_list")
    object CategoryDetail : Screen("category_detail/{categoryName}") {
        fun createRoute(categoryName: String) = "category_detail/$categoryName"
    }
}