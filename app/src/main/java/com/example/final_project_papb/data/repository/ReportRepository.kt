// File: data/repository/ReportRepository.kt
package com.example.final_project_papb.data.repository

import com.example.final_project_papb.data.model.Report
import com.example.final_project_papb.data.model.ReportStatus
import com.example.final_project_papb.data.local.ReportDao
import com.example.final_project_papb.data.remote.FirebaseDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class ReportRepository(
    private val reportDao: ReportDao,
    private val firebaseDataSource: FirebaseDataSource
) {

    fun getAllReportsRemote(): Flow<List<Report>> = firebaseDataSource.get()

    fun getAllReportsLocal(): Flow<List<Report>> = reportDao.getAllReports()

    fun getReportsByStatus(status: ReportStatus): Flow<List<Report>> =
        reportDao.getReportsByStatus(status)

    suspend fun getReportById(id: Long): Report? = withContext(Dispatchers.IO) {
        reportDao.getReportById(id)
    }

    suspend fun insertReport(report: Report): Long = withContext(Dispatchers.IO) {
        val generatedId = reportDao.insertReport(report)
        val reportWithId = report.copy(id = generatedId)

        firebaseDataSource.upload(reportWithId)
        generatedId
    }

    suspend fun updateReport(report: Report) = withContext(Dispatchers.IO) {
        reportDao.updateReport(report)
        report.id.let { firebaseDataSource.update(it, report) }
    }

    suspend fun deleteReport(report: Report) = withContext(Dispatchers.IO) {
        reportDao.deleteReport(report)
        report.id.let { firebaseDataSource.delete(it) }
    }

    suspend fun updateReportStatus(reportId: Long, newStatus: ReportStatus) =
        withContext(Dispatchers.IO) {
            val report = reportDao.getReportById(reportId)
            report?.let {
                val updated = it.copy(status = newStatus)
                reportDao.updateReport(updated)
                // Safely handle the nullable id before calling Firebase
                updated.id?.let { id ->
                    firebaseDataSource.update(id, updated)
                }
            }
        }
}