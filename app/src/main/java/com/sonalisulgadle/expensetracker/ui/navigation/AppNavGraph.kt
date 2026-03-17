package com.sonalisulgadle.expensetracker.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.sonalisulgadle.expensetracker.ui.dashboard.DashboardScreen

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
                onNavigateToCategory = { categoryName ->
                    navController.navigate(
                        Screen.CategoryDetail.createRoute(categoryName)
                    )
                }
            )
        }

        composable(Screen.ExpenseList.route) {
            // ExpenseListScreen — coming next
        }

        composable(
            route = Screen.CategoryDetail.route,
            arguments = listOf(
                navArgument("categoryName") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val categoryName = backStackEntry.arguments?.getString("categoryName") ?: ""
            // CategoryDetailScreen — coming next
        }
    }
}