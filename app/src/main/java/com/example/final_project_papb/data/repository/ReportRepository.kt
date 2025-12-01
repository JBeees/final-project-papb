package com.example.final_project_papb.data.repository

import com.example.final_project_papb.data.local.ReportDao
import com.example.final_project_papb.data.model.Report
import com.example.final_project_papb.data.model.ReportStatus
import com.example.final_project_papb.data.remote.FirebaseDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class ReportRepository(
    private val dao: ReportDao,
    private val firebase: FirebaseDataSource
) {

    fun getAllReportsLocal(): Flow<List<Report>> = dao.getAllReports()

    fun getReportsByStatus(status: ReportStatus): Flow<List<Report>> =
        dao.getReportsByStatus(status)

    suspend fun insertOrUpdateLocal(report: Report) {
        dao.insertOrUpdate(report)
    }

    suspend fun insertReport(report: Report) {
        val localId = dao.insertOrUpdate(report)
        val idFirebase = firebase.upload(report.copy(id = localId))
        dao.updateReport(report.copy(id = localId, idFirebase = idFirebase))
    }

    suspend fun updateReportStatus(report: Report, status: ReportStatus) {
        val updated = report.copy(status = status)
        dao.updateReport(updated)
        firebase.updateStatus(updated.idFirebase, status)
    }

    suspend fun deleteReport(report: Report) {
        dao.deleteReport(report)
        firebase.delete(report.idFirebase)
    }

    fun syncFromFirebase() = firebase.getRealtime()
}
