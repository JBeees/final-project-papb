// File: data/model/Report.kt
package com.example.final_project_papb.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reports")
data class Report(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val description: String,
    val category: ReportCategory,
    val latitude: Double,
    val longitude: Double,
    val photoUri: String,
    val status: ReportStatus = ReportStatus.PENDING,
    val timestamp: Long = System.currentTimeMillis(),
    val address: String = ""
)

enum class ReportCategory(val displayName: String) {
    DAMAGED_FACILITY("Fasilitas Rusak"),
    CRIME_PRONE("Area Rawan Kriminal"),
    POTHOLE("Jalan Berlubang"),
    STREET_LIGHT("Lampu Jalan Mati"),
    FLOOD("Banjir"),
    GARBAGE("Sampah Menumpuk"),
    OTHER("Lainnya")
}

enum class ReportStatus(val displayName: String) {
    PENDING("Menunggu"),
    IN_PROGRESS("Dalam Perbaikan"),
    COMPLETED("Selesai"),
    REJECTED("Ditolak")
}



