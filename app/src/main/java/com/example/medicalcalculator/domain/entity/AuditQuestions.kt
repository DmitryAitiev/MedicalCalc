package com.example.medicalcalculator.domain.entity

data class AuditQuestion(
    val text: String,
    val options: List<Pair<String, Int>>
)