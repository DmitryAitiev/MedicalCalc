package com.example.medicalcalculator.domain.repository

interface AuthListRepository {

    fun authorization(email: String, password: String, onResult: (Boolean, String?) -> Unit): Unit
    fun registration(email: String, password: String, onResult: (Boolean, String?) -> Unit): Unit
    fun resetPassword(email: String, onResult: (Boolean, String?) -> Unit): Unit
    fun logout(): Unit
}