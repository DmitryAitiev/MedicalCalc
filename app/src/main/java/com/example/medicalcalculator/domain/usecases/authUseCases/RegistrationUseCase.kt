package com.example.medicalcalculator.domain.usecases.authUseCases

import com.example.medicalcalculator.domain.repository.AuthListRepository

class RegistrationUseCase(private val repository: AuthListRepository) {

    fun registration(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        repository.registration(email, password, onResult)
    }
}