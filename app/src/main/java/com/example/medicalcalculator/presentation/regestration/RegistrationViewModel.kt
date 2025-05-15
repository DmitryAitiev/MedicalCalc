package com.example.medicalcalculator.presentation.regestration

import androidx.lifecycle.ViewModel
import com.example.medicalcalculator.data.repository.AuthRepositoryImpl
import com.example.medicalcalculator.domain.usecases.authUseCases.RegistrationUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class RegistrationViewModel: ViewModel() {

    private val repository = AuthRepositoryImpl()
    private val registrationUseCase = RegistrationUseCase(repository)

    private val _registrationResult = MutableStateFlow<RegistrationResult?>(null)
    val registrationResult: StateFlow<RegistrationResult?> = _registrationResult

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun onRegisterClicked(email: String, password: String, confirm: String) {
        val validationError = when {
            email.isBlank() ->
                "Введите email"
            !android.util.Patterns.EMAIL_ADDRESS
                .matcher(email)
                .matches() ->
                "Некорректный email"
            password.length < 6 ->
                "Пароль должен быть не менее 6 символов"
            password != confirm ->
                "Пароли не совпадают"
            else -> null
        }

        if (validationError != null) {
            _errorMessage.value = validationError
            return
        }
        _errorMessage.value = null

        registrationUseCase.registration(email, password) { success, error ->
            if (success) {
                _registrationResult.value = RegistrationResult.Success
            } else {
                _registrationResult.value = RegistrationResult.Error(error)
            }
        }
    }

    fun onRegistrationResultHandled() {
        _registrationResult.value = null
    }
    fun onErrorMessageShown() {
        _errorMessage.value = null
    }
}

sealed class RegistrationResult {
    data object Success: RegistrationResult()
    data class Error(val message: String?): RegistrationResult()
}