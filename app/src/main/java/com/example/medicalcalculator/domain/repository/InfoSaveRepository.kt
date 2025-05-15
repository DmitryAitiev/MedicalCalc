package com.example.medicalcalculator.domain.repository

import com.example.medicalcalculator.domain.entity.AuditResult

interface InfoSaveRepository {

    fun saveAuditResult(score: Int, onComplete: (Boolean, String?) -> Unit)
    fun getAuditResults(onSuccess: (List<AuditResult>) -> Unit, onError: (String) -> Unit)
}