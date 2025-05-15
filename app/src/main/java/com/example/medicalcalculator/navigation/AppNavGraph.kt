package com.example.medicalcalculator.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.medicalcalculator.presentation.audit.AuditScreen
import com.example.medicalcalculator.presentation.authentication.AuthNavigationEvent
import com.example.medicalcalculator.presentation.authentication.AuthScreen
import com.example.medicalcalculator.presentation.authentication.AuthState
import com.example.medicalcalculator.presentation.authentication.AuthViewModel
import com.example.medicalcalculator.presentation.forgotPassword.ForgotPasswordScreen
import com.example.medicalcalculator.presentation.regestration.RegistrationScreen
import com.example.medicalcalculator.presentation.results.AuditResultScreen

@Composable
fun AuthNavScreen(
    navController: NavHostController,
    authViewModel: AuthViewModel
) {
    val navigationEvent by authViewModel.navigationEvent.collectAsState()
    LaunchedEffect(navigationEvent) {
        when (navigationEvent) {
            is AuthNavigationEvent.NavigateToRegister -> {
                navController.navigate(Screen.Registration.route)
                authViewModel.onNavigationEventHandled()
            }
            is AuthNavigationEvent.NavigateToForgotPassword -> {
                navController.navigate(Screen.ForgotPassword.route)
                authViewModel.onNavigationEventHandled()
            }
            null -> {}
        }
    }
    AuthScreen(navController)
}

const val AUTH_GRAPH_ROUTE = "auth_graph"
const val MAIN_GRAPH_ROUTE = "main_graph"

@Composable
fun AppNavGraph(
    navHostController: NavHostController,
    authViewModel: AuthViewModel = viewModel()
) {
    val authState by authViewModel.authState.collectAsState()

    val startDestination = when (authState) {
        AuthState.AUTHENTICATED -> MAIN_GRAPH_ROUTE
        AuthState.UNAUTHENTICATED -> AUTH_GRAPH_ROUTE
        AuthState.LOADING -> null
    }

    if (startDestination != null) {
        NavHost(
            navController = navHostController,
            startDestination = startDestination
        ) {
            authScreenNavGraph(
                navController = navHostController,
            )
            auditScreenNavGraph(
                navController = navHostController
            )
        }
    }
}
