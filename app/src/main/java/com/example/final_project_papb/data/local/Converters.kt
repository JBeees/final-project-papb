// File: data/local/Converters.kt
package com.example.final_project_papb.data.local

import androidx.room.TypeConverter
import com.example.final_project_papb.data.model.ReportCategory
import com.example.final_project_papb.data.model.ReportStatus

class Converters {
    @TypeConverter
    fun fromReportCategory(value: ReportCategory): String = value.name

    @TypeConverter
    fun toReportCategory(value: String): ReportCategory = ReportCategory.valueOf(value)

    @TypeConverter
    fun fromReportStatus(value: ReportStatus): String = value.name

    @TypeConverter
    fun toReportStatus(value: String): ReportStatus = ReportStatus.valueOf(value)
}