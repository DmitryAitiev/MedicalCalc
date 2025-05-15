package com.example.medicalcalculator.presentation.forgotPassword

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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch

@Composable
fun ForgotPasswordScreen(
    navController: NavController
) {
    val viewModel = ForgotPasswordViewModel()
    val resetResult by viewModel.resetResult.collectAsState()
    var email by remember { mutableStateOf("") }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
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
                // Логотип
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

                Spacer(Modifier.height(16.dp))

                Text(
                    text = "Восстановление пароля",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF388E3C),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            label = { Text("Email") },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(Modifier.height(24.dp))

                        Button(
                            onClick = { viewModel.resetPassword(email) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            shape = RoundedCornerShape(50)
                        ) {
                            Text(
                                text = "Отправить ссылку",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(Modifier.height(12.dp))

                        Text(
                            text = "Вернуться к входу",
                            color = Color(0xFF4CAF50),
                            fontSize = 14.sp,
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .clickable { navController.popBackStack() }
                                .padding(vertical = 4.dp)
                        )
                    }
                }
            }

            LaunchedEffect(resetResult) {
                when (val result = resetResult) {
                    is ResetResult.Success -> {
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                message = "Письмо для сброса пароля отправлено",
                                duration = SnackbarDuration.Short
                            )
                        }
                        viewModel.onResetResultHandled()
                    }
                    is ResetResult.Error -> {
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                message = result.message!!,
                                duration = SnackbarDuration.Short
                            )
                        }
                        viewModel.onResetResultHandled()
                    }
                    null -> { }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ForgotPasswordScreenPreview() {
    val navController = rememberNavController()
    MaterialTheme {
        ForgotPasswordScreen(navController = navController)
    }
}