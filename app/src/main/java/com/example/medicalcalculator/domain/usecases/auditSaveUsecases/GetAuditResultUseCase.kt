package com.example.medicalcalculator.domain.usecases.auditSaveUsecases

import com.example.medicalcalculator.domain.entity.AuditResult
import com.example.medicalcalculator.domain.repository.InfoSaveRepository

class GetAuditResultUseCase(private val repository: InfoSaveRepository) {

    fun getAuditResults(onSuccess: (List<AuditResult>) -> Unit, onError: (String) -> Unit) {
        repository.getAuditResults(onSuccess, onError)
    }
}