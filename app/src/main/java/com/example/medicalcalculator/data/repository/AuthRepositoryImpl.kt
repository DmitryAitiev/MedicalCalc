package com.example.medicalcalculator.data.repository

import android.util.Log
import com.example.medicalcalculator.domain.repository.AuthListRepository
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class AuthRepositoryImpl: AuthListRepository {

    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    override fun authorization(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user: FirebaseUser? = auth.currentUser
                    Log.d("AuthRepository", "Authorization successful: User ID = ${user?.uid}")
                    onResult(true, null)
                } else {
                    Log.e("AuthRepository", "Authorization failed", task.exception)
                    onResult(false, task.exception?.localizedMessage)
                }
            }
    }

    override fun registration(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user: FirebaseUser? = auth.currentUser
                    Log.d("AuthRepository", "Registration successful: User ID = ${user?.uid}")
                    onResult(true, null)
                } else {
                    Log.e("AuthRepository", "Registration failed", task.exception)
                    onResult(false, task.exception?.localizedMessage)
                }
            }
    }

    override fun resetPassword(email: String, onResult: (Boolean, String?) -> Unit) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("AuthRepository", "Password reset email sent to $email")
                    onResult(true, null)
                } else {
                    Log.e("AuthRepository", "Password reset failed", task.exception)
                    onResult(false, task.exception?.localizedMessage)
                }
            }
    }
}