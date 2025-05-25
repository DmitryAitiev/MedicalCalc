package com.example.medicalcalculator.presentation.authentication

import androidx.lifecycle.ViewModel
import com.example.medicalcalculator.data.repository.AuthRepositoryImpl
import com.example.medicalcalculator.domain.usecases.authUseCases.AuthUseCase
import com.example.medicalcalculator.domain.usecases.authUseCases.LogoutUseCase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AuthViewModel: ViewModel() {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _currentUser = MutableStateFlow<FirebaseUser?>(null)
    val currentUser: StateFlow<FirebaseUser?> = _currentUser

    private val _authState = MutableStateFlow(determineInitialAuthState())
    val authState: StateFlow<AuthState> = _authState

    private val repository = AuthRepositoryImpl()
    private val authUseCase = AuthUseCase(repository)
    private val logoutUseCase = LogoutUseCase(repository)

    private val _authResult = MutableStateFlow<AuthResult?>(null)
    val authResult: StateFlow<AuthResult?> = _authResult

    private val _navigationEvent = MutableStateFlow<AuthNavigationEvent?>(null)
    val navigationEvent: StateFlow<AuthNavigationEvent?> = _navigationEvent

    private val authStateListener = FirebaseAuth.AuthStateListener { auth ->
        val user = auth.currentUser
        _currentUser.value = user
        _authState.value = if (user != null) AuthState.AUTHENTICATED else AuthState.UNAUTHENTICATED
    }

    init {
        firebaseAuth.addAuthStateListener(authStateListener)
    }

    private fun determineInitialAuthState(): AuthState {
        return if (firebaseAuth.currentUser != null) AuthState.AUTHENTICATED else AuthState.UNAUTHENTICATED
    }

    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) return
        authUseCase.authorization(email, password) { success, error ->
            if (success) _authResult.value = AuthResult.Success
            else _authResult.value = AuthResult.Error(error)
        }
    }

    fun signOut() {
        logoutUseCase.invoke()
        _authResult.value = null
    }

    fun onRegisterClick() {
        _navigationEvent.value = AuthNavigationEvent.NavigateToRegister
    }

    fun onForgotPassword() {
        _navigationEvent.value = AuthNavigationEvent.NavigateToForgotPassword
    }

    fun onAuthResultHandled() {
        _authResult.value = null
    }

    fun onNavigationEventHandled() {
        _navigationEvent.value = null
    }

    override fun onCleared() {
        super.onCleared()
        firebaseAuth.removeAuthStateListener(authStateListener)
    }
}

sealed class AuthResult {
    data object Success: AuthResult()
    data class Error(val message: String?): AuthResult()
}

sealed class AuthNavigationEvent {
    data object NavigateToRegister: AuthNavigationEvent()
    data object NavigateToForgotPassword: AuthNavigationEvent()
}

enum class AuthState {
    LOADING,
    AUTHENTICATED,
    UNAUTHENTICATED
}