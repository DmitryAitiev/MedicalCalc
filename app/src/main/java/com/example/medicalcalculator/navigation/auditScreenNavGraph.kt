package com.example.medicalcalculator.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.medicalcalculator.presentation.audit.AuditScreen
import com.example.medicalcalculator.presentation.results.AuditResultScreen

fun NavGraphBuilder.auditScreenNavGraph(
    navController: NavHostController) {
        navigation(
            route = MAIN_GRAPH_ROUTE,
            startDestination = Screen.AuditTest.route
        ) {
            composable(route = Screen.AuditTest.route) {
                AuditScreen(
                    onNavigateToResult = { score ->
                        navController.navigate(Screen.AuditResult.createRoute(score))
                    }
                )
            }
            composable(
                route = Screen.AuditResult.route,
                arguments = Screen.auditResultArguments
            ) { backStackEntry ->
                val score = backStackEntry.arguments?.getInt(Screen.ARG_SCORE)
                requireNotNull(score) { "Score argument is missing for ${Screen.AuditResult.route}" }
                AuditResultScreen(score = score)
            }
        }
}