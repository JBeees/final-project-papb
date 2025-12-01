package com.example.final_project_papb.data.remote

import android.util.Log
import com.example.final_project_papb.data.model.Report
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FirebaseDataSource {
    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("reports")

    suspend fun upload(report: Report): Result<String> = try {
        // pastikan report.id sudah terisi (bukan 0)
        val docRef = collection.document(report.id.toString())
        docRef.set(report).await()
        Result.success(docRef.id)
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun update(id: Long, report: Report): Result<Unit> = try {
        collection.document(id.toString()).set(report).await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun delete(id: Long): Result<Unit> = try {
        collection.document(id.toString()).delete().await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    fun get(): Flow<List<Report>> = flow {
        try {
            val snapshot = collection.get().await()
            emit(snapshot.toObjects(Report::class.java))
        } catch (e: Exception) {
            Log.e("Firebase", "Failed to fetch reports", e)
            emit(emptyList())
        }
    }
}