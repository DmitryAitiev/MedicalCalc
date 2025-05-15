package com.example.medicalcalculator.presentation.results

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.medicalcalculator.data.repository.InfoSaveRepositoryImpl
import com.example.medicalcalculator.domain.entity.AuditResult
import com.example.medicalcalculator.domain.repository.InfoSaveRepository
import com.example.medicalcalculator.domain.usecases.auditSaveUsecases.GetAuditResultUseCase
import com.example.medicalcalculator.domain.usecases.auditSaveUsecases.SaveAuditResultUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ResultViewModel: ViewModel() {

    private val repository = InfoSaveRepositoryImpl()
    private val saveData = SaveAuditResultUseCase(repository)
    private val getData = GetAuditResultUseCase(repository)

    val list = MutableStateFlow<List<AuditResult>>(emptyList())

    private val _saveResult = MutableStateFlow<DataResult?>(null)
    val saveResult: StateFlow<DataResult?> = _saveResult

    fun getAuditRiskLevel(score: Int): String = when {
        score <= 7   -> "Низкий риск"
        score <= 15  -> "Повышенный уровень употребления"
        score <= 19  -> "Вредное употребление"
        else         -> "Вероятность алкогольной зависимости"
    }

    fun saveAuditResult(score: Int) {
        saveData.saveAuditResult(score) { success, error ->
            if (success) {
                _saveResult.value = DataResult.Success
            }
            else {
                _saveResult.value = DataResult.Error(message = error)
            }
        }
    }

    fun getAuditResult() {
        getData.getAuditResults(onSuccess = {
            list.value = it
        }, onError = {
            Log.e("Get Result Error", it)
        })
    }
}

sealed class DataResult {
    data object Success: DataResult()
    data class Error(val message: String?): DataResult()
}