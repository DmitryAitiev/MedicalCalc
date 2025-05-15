package com.example.medicalcalculator.data.repository

import com.example.medicalcalculator.domain.entity.AuditResult
import com.example.medicalcalculator.domain.repository.InfoSaveRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class InfoSaveRepositoryImpl: InfoSaveRepository {

    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val db: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    override fun getAuditResults(
        onSuccess: (List<AuditResult>) -> Unit,
        onError: (String) -> Unit
    ) {
        val uid = auth.currentUser?.uid ?: run {
            onError("Не авторизован")
            return
        }
        db.collection("users")
            .document(uid)
            .collection("auditResults")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { snapshot ->
                val list = snapshot.map { doc ->
                    AuditResult(
                        score = doc.getLong("score")?.toInt() ?: 0,
                        timestamp = doc.getLong("timestamp") ?: 0L
                    )
                }
                onSuccess(list)
            }
            .addOnFailureListener { e -> onError(e.localizedMessage ?: "") }
    }

    override fun saveAuditResult(score: Int, onComplete: (Boolean, String?) -> Unit) {
        val uid = auth.currentUser?.uid
        if (uid == null) {
            onComplete(false, "Пользователь не найден")
            return
        }
        val data = mapOf(
            "score" to score,
            "timestamp" to System.currentTimeMillis()
        )
        db.collection("users")
            .document(uid)
            .collection("auditResults")
            .add(data)
            .addOnSuccessListener { onComplete(true, null) }
            .addOnFailureListener { e -> onComplete(false, e.localizedMessage) }
    }
}