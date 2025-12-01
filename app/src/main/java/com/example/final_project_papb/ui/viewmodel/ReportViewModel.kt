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

class ReportViewModel(app: Application) : AndroidViewModel(app) {

    private val repository: ReportRepository

    // 🔥 Sesuai permintaan: pakai `allReports`
    val allReports: StateFlow<List<Report>>

    init {
        val reportDao = AppDatabase.getDatabase(app).reportDao()
        val firebaseDataSource = FirebaseDataSource()
        repository = ReportRepository(reportDao, firebaseDataSource)

        allReports = repository.getAllReportsLocal()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

        // 🔄 Sync Firebase → Room realtime
        viewModelScope.launch {
            repository.syncFromFirebase().collect { reports ->
                reports.forEach { repository.insertOrUpdateLocal(it) }
            }
        }
    }

    fun insertReport(
        title: String,
        description: String,
        category: ReportCategory,
        latitude: Double,
        longitude: Double,
        photoUri: String,
        address: String
    ) = viewModelScope.launch {
        repository.insertReport(
            Report(
                title = title,
                description = description,
                category = category,
                latitude = latitude,
                longitude = longitude,
                photoUri = photoUri,
                address = address
            )
        )
    }

    fun updateReportStatus(report: Report, status: ReportStatus) =
        viewModelScope.launch {
            repository.updateReportStatus(report, status)
        }

    fun deleteReport(report: Report) =
        viewModelScope.launch {
            repository.deleteReport(report)
        }

    fun getReportsByStatus(status: ReportStatus): StateFlow<List<Report>> =
        repository.getReportsByStatus(status)
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                emptyList()
            )
}
