// File: ui/viewmodel/ReportViewModel.kt
package com.example.final_project_papb.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.final_project_papb.data.local.AppDatabase
import com.example.final_project_papb.data.model.Report
import com.example.final_project_papb.data.model.ReportCategory
import com.example.final_project_papb.data.model.ReportStatus
import com.example.final_project_papb.data.remote.FirebaseDataSource
import com.example.final_project_papb.data.repository.ReportRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ReportViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ReportRepository

    val allReports: StateFlow<List<Report>>

    private val _selectedReport = MutableStateFlow<Report?>(null)
    val selectedReport: StateFlow<Report?> = _selectedReport.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        val reportDao = AppDatabase.getDatabase(application).reportDao()
        val firebaseDataSource = FirebaseDataSource()
        repository = ReportRepository(reportDao, firebaseDataSource)

        allReports = repository.getAllReportsRemote()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )
    }

    fun insertReport(
        title: String,
        description: String,
        category: ReportCategory,
        latitude: Double,
        longitude: Double,
        photoUri: String,
        address: String
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val report = Report(
                    title = title,
                    description = description,
                    category = category,
                    latitude = latitude,
                    longitude = longitude,
                    photoUri = photoUri,
                    address = address
                )
                repository.insertReport(report)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateReportStatus(reportId: Long, newStatus: ReportStatus) {
        viewModelScope.launch {
            repository.updateReportStatus(reportId, newStatus)
        }
    }

    fun selectReport(report: Report?) {
        _selectedReport.value = report
    }

    fun deleteReport(report: Report) {
        viewModelScope.launch {
            repository.deleteReport(report)
        }
    }

    fun getReportsByStatus(status: ReportStatus): StateFlow<List<Report>> {
        return repository.getReportsByStatus(status)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )
    }
}