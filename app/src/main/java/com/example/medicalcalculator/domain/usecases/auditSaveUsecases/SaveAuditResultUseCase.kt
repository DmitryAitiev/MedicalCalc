package com.example.medicalcalculator.domain.usecases.auditSaveUsecases

import com.example.medicalcalculator.domain.repository.InfoSaveRepository

class SaveAuditResultUseCase(private val repository: InfoSaveRepository) {

    fun saveAuditResult(score: Int, onComplete: (Boolean, String?) -> Unit) {
        repository.saveAuditResult(score, onComplete)
    }
}