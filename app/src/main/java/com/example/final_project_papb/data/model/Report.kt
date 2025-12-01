package com.example.final_project_papb.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.firestore.PropertyName

@Entity(tableName = "reports")
data class Report(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,   // Local Primary Key

    @get:PropertyName("idFirebase")
    @set:PropertyName("idFirebase")
    var idFirebase: String = "", // Firestore document ID

    val title: String = "",
    val description: String = "",
    val category: ReportCategory = ReportCategory.OTHER,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val photoUri: String = "",
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



