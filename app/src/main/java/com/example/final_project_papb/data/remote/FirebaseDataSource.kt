package com.example.final_project_papb.data.remote

import com.example.final_project_papb.data.model.Report
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirebaseDataSource {
    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("reports")

    suspend fun upload(report: Report): Result<String> = try {
        val docRef = collection.add(report).await()
        Result.success(docRef.id)
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun sync(): Result<List<Report>> = try {
        val snapshot = collection.get().await()
        Result.success(snapshot.toObjects(Report::class.java))
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
}