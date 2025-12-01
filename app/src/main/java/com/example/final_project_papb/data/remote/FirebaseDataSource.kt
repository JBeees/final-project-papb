package com.example.final_project_papb.data.remote

import com.example.final_project_papb.data.model.Report
import com.example.final_project_papb.data.model.ReportStatus
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class FirebaseDataSource {

    private val collection = FirebaseFirestore.getInstance().collection("reports")

    // 🔥 Inilah fungsi yang dicari ViewModel dan Repository
    fun getRealtime() = callbackFlow<List<Report>> {
        val listener = collection.addSnapshotListener { snapshot, error ->
            if (error != null) {
                trySend(emptyList())
                return@addSnapshotListener
            }
            val data = snapshot?.toObjects(Report::class.java) ?: emptyList()
            trySend(data)
        }

        awaitClose { listener.remove() }
    }

    suspend fun upload(report: Report): String {
        val docRef = collection.document()
        val idFirebase = docRef.id
        docRef.set(report.copy(idFirebase = idFirebase)).await()
        return idFirebase
    }

    suspend fun updateStatus(idFirebase: String, newStatus: ReportStatus) {
        if (idFirebase.isNotEmpty())
            collection.document(idFirebase).update("status", newStatus).await()
    }

    suspend fun delete(idFirebase: String) {
        if (idFirebase.isNotEmpty())
            collection.document(idFirebase).delete().await()
    }
}
