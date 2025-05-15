package com.example.medicalcalculator.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.medicalcalculator.presentation.authentication.AuthScreen
import com.example.medicalcalculator.presentation.forgotPassword.ForgotPasswordScreen
import com.example.medicalcalculator.presentation.regestration.RegistrationScreen

fun NavGraphBuilder.authScreenNavGraph(
    navController: NavHostController
) {
    navigation(
        route = AUTH_GRAPH_ROUTE,
        startDestination = Screen.Auth.route
    ) {
        composable(route = Screen.Auth.route) {
            AuthScreen(navController = navController)
        }
        composable(route = Screen.Registration.route) {
            RegistrationScreen(navController = navController)
        }
        composable(route = Screen.ForgotPassword.route) {
            ForgotPasswordScreen(navController = navController)
        }
    }
}