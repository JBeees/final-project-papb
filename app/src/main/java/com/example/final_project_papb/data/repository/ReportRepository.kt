// File: data/repository/ReportRepository.kt
package com.example.final_project_papb.data.repository

import com.example.final_project_papb.data.model.Report
import com.example.final_project_papb.data.model.ReportStatus
import com.example.final_project_papb.data.local.ReportDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class ReportRepository(private val reportDao: ReportDao) {

    fun getAllReports(): Flow<List<Report>> = reportDao.getAllReports()

    fun getReportsByStatus(status: ReportStatus): Flow<List<Report>> =
        reportDao.getReportsByStatus(status)

    suspend fun getReportById(id: Long): Report? = withContext(Dispatchers.IO) {
        reportDao.getReportById(id)
    }

    suspend fun insertReport(report: Report): Long = withContext(Dispatchers.IO) {
        reportDao.insertReport(report)
    }

    suspend fun updateReport(report: Report) = withContext(Dispatchers.IO) {
        reportDao.updateReport(report)
    }

    suspend fun deleteReport(report: Report) = withContext(Dispatchers.IO) {
        reportDao.deleteReport(report)
    }

    suspend fun updateReportStatus(reportId: Long, newStatus: ReportStatus) =
        withContext(Dispatchers.IO) {
            val report = reportDao.getReportById(reportId)
            report?.let {
                reportDao.updateReport(it.copy(status = newStatus))
            }
        }
}