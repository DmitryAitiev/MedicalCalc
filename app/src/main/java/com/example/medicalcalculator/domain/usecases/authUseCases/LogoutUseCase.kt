package com.example.medicalcalculator.domain.usecases.authUseCases

import com.example.medicalcalculator.domain.repository.AuthListRepository

class LogoutUseCase(private val repository: AuthListRepository) {

    operator fun invoke() {
        repository.logout()
    }
}