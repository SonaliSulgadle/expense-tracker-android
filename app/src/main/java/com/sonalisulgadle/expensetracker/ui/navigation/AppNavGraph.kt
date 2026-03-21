package com.sonalisulgadle.expensetracker.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
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
                onNavigateToCategory = { categoryName ->
                    navController.navigate(
                        Screen.CategoryDetail.createRoute(categoryName)
                    )
                }
            )
        }

        composable(Screen.ExpenseList.route) {
            ExpenseListScreen(
                onBackClick = { navController.navigateUp() }
            )
        }

        composable(
            route = Screen.CategoryDetail.route,
            arguments = listOf(
                navArgument(Constants.NAV_ARG_CATEGORY_NAME) {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            // CategoryDetailScreen — coming next

            // Placeholder for now
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Category Detail — Coming Soon",
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}