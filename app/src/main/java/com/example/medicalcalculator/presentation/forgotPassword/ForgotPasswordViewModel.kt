package com.example.medicalcalculator.presentation.forgotPassword

import androidx.lifecycle.ViewModel
import com.example.medicalcalculator.data.repository.AuthRepositoryImpl
import com.example.medicalcalculator.domain.usecases.authUseCases.ResetPasswordUseCase
import kotlinx.coroutines.flow.MutableStateFlow

class ForgotPasswordViewModel: ViewModel() {

    private val repository = AuthRepositoryImpl()
    private val resetPasswordUseCase = ResetPasswordUseCase(repository)

    private val _resetResult = MutableStateFlow<ResetResult?>(null)
    val resetResult = _resetResult

    fun resetPassword(email: String) {
        resetPasswordUseCase.resetPassword(email) { success, error ->
            if (success) _resetResult.value = ResetResult.Success
            else _resetResult.value = ResetResult.Error(error)
        }
    }

    fun onResetResultHandled() {
        _resetResult.value = null
    }
}

sealed class ResetResult {
    data object Success: ResetResult()
    data class Error(val message: String?): ResetResult()
}