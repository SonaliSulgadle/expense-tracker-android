package com.sonalisulgadle.expensetracker.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.sonalisulgadle.expensetracker.ui.category.CategoryDetailScreen
import com.sonalisulgadle.expensetracker.ui.dashboard.DashboardScreen
import com.sonalisulgadle.expensetracker.ui.expense.ExpenseListScreen
import com.sonalisulgadle.expensetracker.util.Constants

@Composable
fun AppNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Dashboard.route,
        modifier = modifier
    ) {
        composable(Screen.Dashboard.route) {
            DashboardScreen(
                onNavigateToExpenseList = {
                    navController.navigate(Screen.ExpenseList.route)
                },
                onNavigateToCategory = { categoryName, categoryEmoji ->
                    navController.navigate(
                        Screen.CategoryDetail.createRoute(categoryName, categoryEmoji)
                    )
                }
            )
        }

        composable(Screen.ExpenseList.route) {
            ExpenseListScreen(
                onBackClick = { navController.navigateUp() },
                onNavigateToCategory = { name, emoji ->
                    navController.navigate(
                        Screen.CategoryDetail.createRoute(name, emoji)
                    )
                }
            )
        }

        composable(
            route = Screen.CategoryDetail.route,
            arguments = listOf(
                navArgument(Constants.NAV_ARG_CATEGORY_NAME) {
                    type = NavType.StringType
                },
                navArgument(Constants.NAV_ARG_CATEGORY_EMOJI) {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            CategoryDetailScreen(
                onBackClick = { navController.navigateUp() }
            )
        }
    }
}