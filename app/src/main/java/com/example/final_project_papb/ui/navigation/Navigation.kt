// File: ui/navigation/Navigation.kt
package com.example.final_project_papb.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.final_project_papb.ui.screens.*
import com.example.final_project_papb.ui.viewmodel.ReportViewModel

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Map : Screen("map")
    object NewReport : Screen("new_report")
    object ReportDetail : Screen("report_detail/{reportId}") {
        fun createRoute(reportId: Long) = "report_detail/$reportId"
    }
    object ReportList : Screen("report_list")
}

@Composable
fun SafeCityNavigation(
    navController: NavHostController,
    viewModel: ReportViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                navController = navController,
                viewModel = viewModel
            )
        }

        composable(Screen.Map.route) {
            MapScreen(
                navController = navController,
                viewModel = viewModel
            )
        }

        composable(Screen.NewReport.route) {
            NewReportScreen(
                navController = navController,
                viewModel = viewModel
            )
        }

        composable(
            route = Screen.ReportDetail.route,
            arguments = listOf(navArgument("reportId") { type = NavType.LongType })
        ) { backStackEntry ->
            val reportId = backStackEntry.arguments?.getLong("reportId") ?: 0L
            ReportDetailScreen(
                navController = navController,
                viewModel = viewModel,
                reportId = reportId
            )
        }

        composable(Screen.ReportList.route) {
            ReportListScreen(
                navController = navController,
                viewModel = viewModel
            )
        }
    }
}