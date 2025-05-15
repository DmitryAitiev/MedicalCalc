package com.example.medicalcalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.medicalcalculator.presentation.MainScreen
import com.example.medicalcalculator.presentation.audit.AuditScreen
import com.example.medicalcalculator.ui.theme.MedicalCalculatorTheme
import com.google.firebase.Firebase
import com.google.firebase.initialize

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        Firebase.initialize(this)
        setContent {
            MedicalCalculatorTheme {
                MainScreen()
            }
        }
    }
}
