package com.example.medicalcalculator.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class Screen(val route: String) {
    data object Auth: Screen(ROUTE_AUTH)
    data object Registration: Screen(ROUTE_REGISTRATION)
    data object ForgotPassword: Screen(ROUTE_FORGOT_PASSWORD)
    data object AuditTest: Screen(ROUTE_AUDIT_TEST)
    data object AuditResult : Screen("$ROUTE_AUDIT_RESULT/{$ARG_SCORE}") {
        fun createRoute(score: Int) = "$ROUTE_AUDIT_RESULT/$score"
    }

    companion object {
        const val ROUTE_AUTH = "auth"
        const val ROUTE_REGISTRATION = "registration"
        const val ROUTE_FORGOT_PASSWORD = "forgot_password"
        const val ROUTE_AUDIT_TEST = "audit_test"
        const val ROUTE_AUDIT_RESULT = "audit_result"
        const val ARG_SCORE = "score"

        val auditResultArguments = listOf(
            navArgument(ARG_SCORE) { type = NavType.IntType }
        )
    }
}