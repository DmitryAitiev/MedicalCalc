package com.example.medicalcalculator.presentation.authentication

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.medicalcalculator.navigation.Screen
import kotlinx.coroutines.launch

@Composable
fun AuthScreen(
    navController: NavController
) {
    val viewModel = AuthViewModel()
    val authResult by viewModel.authResult.collectAsState()
    val navigationEvent by viewModel.navigationEvent.collectAsState()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold (
        snackbarHost = { SnackbarHost(snackBarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFF4CAF50), Color(0xFFA5D6A7))
                    )
                )
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 32.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Surface(
                    shape = CircleShape,
                    color = Color.White,
                    shadowElevation = 8.dp,
                    modifier = Modifier.size(72.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text("🩺", fontSize = 36.sp)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Medical Calculator",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF388E3C),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .padding(24.dp)
                    ) {
                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            label = { Text("Email") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            label = { Text("Password") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            visualTransformation = PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Forgot password?",
                            color = Color(0xFF4CAF50),
                            fontSize = 14.sp,
                            modifier = Modifier
                                .align(Alignment.End)
                                .clickable { viewModel.onForgotPassword() }
                                .padding(vertical = 4.dp)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = { viewModel.login(email, password) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            shape = RoundedCornerShape(50)
                        ) {
                            Text(
                                text = "Sign In",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Don't have an account?",
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Register",
                                color = Color(0xFF4CAF50),
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                modifier = Modifier.clickable { viewModel.onRegisterClick() }
                            )
                        }
                    }
                }
            }
            LaunchedEffect(navigationEvent) {
                when (navigationEvent) {
                    is AuthNavigationEvent.NavigateToRegister -> {
                        navController.navigate(Screen.Registration.route)
                    }
                    is AuthNavigationEvent.NavigateToForgotPassword -> {
                        navController.navigate(Screen.ForgotPassword.route)
                    }
                    null -> {}
                }
            }
            LaunchedEffect(authResult) {
                when (authResult) {
                    is AuthResult.Success -> {
                        navController.navigate(Screen.Registration.route) {
                            popUpTo(Screen.Auth.route) { inclusive = true }
                        }
                        viewModel.onAuthResultHandled()
                    }
                    is AuthResult.Error -> {
                        scope.launch {
                            snackBarHostState.showSnackbar(
                                message = (authResult as AuthResult.Error).message!!,
                                actionLabel = "OK",
                                duration = SnackbarDuration.Short
                            )
                        }
                        viewModel.onAuthResultHandled()
                    }
                    null -> {}
                }
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun AuthScreenPreview() {
    CompositionLocalProvider(
        LocalContext provides LocalContext.current
    ) {
        MaterialTheme {
        AuthScreen(
            navController = rememberNavController()
        )
    } }

}