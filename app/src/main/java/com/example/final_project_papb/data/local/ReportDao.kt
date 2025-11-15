// File: data/local/ReportDao.kt
package com.example.final_project_papb.data.local

import androidx.room.*
import com.example.final_project_papb.data.model.Report
import com.example.final_project_papb.data.model.ReportStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface ReportDao {
    @Query("SELECT * FROM reports ORDER BY timestamp DESC")
    fun getAllReports(): Flow<List<Report>>

    @Query("SELECT * FROM reports WHERE id = :id")
    suspend fun getReportById(id: Long): Report?

    @Query("SELECT * FROM reports WHERE status = :status ORDER BY timestamp DESC")
    fun getReportsByStatus(status: ReportStatus): Flow<List<Report>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReport(report: Report): Long

    @Update
    suspend fun updateReport(report: Report)

    @Delete
    suspend fun deleteReport(report: Report)

    @Query("DELETE FROM reports")
    suspend fun deleteAllReports()
}
