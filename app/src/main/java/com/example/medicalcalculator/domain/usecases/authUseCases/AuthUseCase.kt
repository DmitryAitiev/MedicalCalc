package com.example.medicalcalculator.domain.usecases.authUseCases

import com.example.medicalcalculator.domain.repository.AuthListRepository

class AuthUseCase(private val repository: AuthListRepository) {

    fun authorization(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        repository.authorization(email, password, onResult)
    }
}