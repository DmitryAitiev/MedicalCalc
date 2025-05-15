package com.example.medicalcalculator.domain.usecases.authUseCases

import com.example.medicalcalculator.domain.repository.AuthListRepository

class ResetPasswordUseCase(private val repository: AuthListRepository) {

    fun resetPassword(email: String, onResult: (Boolean, String?) -> Unit) {
        repository.resetPassword(email, onResult)
    }
}