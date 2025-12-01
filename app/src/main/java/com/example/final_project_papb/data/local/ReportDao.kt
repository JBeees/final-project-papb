package com.example.final_project_papb.data.local

import androidx.room.*
import com.example.final_project_papb.data.model.Report
import com.example.final_project_papb.data.model.ReportStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface ReportDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(report: Report): Long

    @Update
    suspend fun updateReport(report: Report)

    @Delete
    suspend fun deleteReport(report: Report)

    @Query("SELECT * FROM reports ORDER BY timestamp DESC")
    fun getAllReports(): Flow<List<Report>>

    @Query("SELECT * FROM reports WHERE status = :status ORDER BY timestamp DESC")
    fun getReportsByStatus(status: ReportStatus): Flow<List<Report>>

    @Query("SELECT * FROM reports WHERE id = :id LIMIT 1")
    suspend fun getReportById(id: Long): Report?
}
