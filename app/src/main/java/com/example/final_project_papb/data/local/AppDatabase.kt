// File: data/local/AppDatabase.kt
package com.example.final_project_papb.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.final_project_papb.data.model.Report
import com.example.final_project_papb.data.local.ReportDao

@Database(entities = [Report::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun reportDao(): ReportDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "safecity_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
