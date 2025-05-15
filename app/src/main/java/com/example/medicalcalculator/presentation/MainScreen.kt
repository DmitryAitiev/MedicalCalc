package com.example.medicalcalculator.presentation

import android.annotation.SuppressLint
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import com.example.medicalcalculator.navigation.AppNavGraph
import com.example.medicalcalculator.navigation.rememberNavigationState
import com.example.medicalcalculator.presentation.authentication.AuthScreen
import com.example.medicalcalculator.presentation.forgotPassword.ForgotPasswordScreen
import com.example.medicalcalculator.presentation.regestration.RegistrationScreen

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen() {

    val navigationState = rememberNavigationState()
    val navController = navigationState.navHostController

    Scaffold {
        AppNavGraph(
            navHostController = navController
        )
    }
}