// File: ui/screens/ReportDetailScreen.kt
package com.example.final_project_papb.ui.screens

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.final_project_papb.data.model.Report
import com.example.final_project_papb.data.model.ReportStatus
import com.example.final_project_papb.ui.viewmodel.ReportViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportDetailScreen(
    navController: NavController,
    viewModel: ReportViewModel,
    reportId: Long
) {
    val reports by viewModel.allReports.collectAsState()
    val report = reports.find { it.id == reportId }
    var showStatusDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    if (report == null) {
        Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator()
        }
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detail Laporan") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali")
                    }
                },
                actions = {
                    IconButton(onClick = { showDeleteDialog = true }) {
                        Icon(Icons.Default.Delete, contentDescription = "Hapus")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            // Photo
            Image(
                painter = rememberAsyncImagePainter(Uri.parse(report.photoUri)),
                contentDescription = "Foto laporan",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Title
                Text(
                    report.title,
                    style = MaterialTheme.typography.headlineMedium
                )

                // Status Badge
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AssistChip(
                        onClick = { showStatusDialog = true },
                        label = { Text(report.status.displayName) },
                        leadingIcon = {
                            Icon(
                                when (report.status) {
                                    ReportStatus.PENDING -> Icons.Default.Schedule
                                    ReportStatus.IN_PROGRESS -> Icons.Default.Build
                                    ReportStatus.COMPLETED -> Icons.Default.CheckCircle
                                    ReportStatus.REJECTED -> Icons.Default.Cancel
                                },
                                contentDescription = null
                            )
                        }
                    )
                    AssistChip(
                        onClick = { },
                        label = { Text(report.category.displayName) }
                    )
                }

                Divider()

                // Description
                Text(
                    "Deskripsi",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    report.description,
                    style = MaterialTheme.typography.bodyLarge
                )

                Divider()

                // Location Info
                Text(
                    "Lokasi",
                    style = MaterialTheme.typography.titleMedium
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(Icons.Default.LocationOn, contentDescription = null)
                    Column {
                        Text(report.address)
                        Text(
                            "Koordinat: ${String.format("%.6f", report.latitude)}, ${String.format("%.6f", report.longitude)}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Divider()

                // Timestamp
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(Icons.Default.AccessTime, contentDescription = null)
                    Text(
                        SimpleDateFormat("dd MMM yyyy, HH:mm", Locale("id", "ID"))
                            .format(Date(report.timestamp))
                    )
                }
            }
        }
    }

    // Status Update Dialog
    // Status Update Dialog
    if (showStatusDialog) {
        AlertDialog(
            onDismissRequest = { showStatusDialog = false },
            title = { Text("Update Status") },
            text = {
                Column {
                    ReportStatus.values().forEach { status ->
                        TextButton(
                            onClick = {
                                scope.launch {
                                    viewModel.updateReportStatus(report, status)
                                    showStatusDialog = false
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Icon(
                                    when (status) {
                                        ReportStatus.PENDING -> Icons.Default.Schedule
                                        ReportStatus.IN_PROGRESS -> Icons.Default.Build
                                        ReportStatus.COMPLETED -> Icons.Default.CheckCircle
                                        ReportStatus.REJECTED -> Icons.Default.Cancel
                                    },
                                    contentDescription = null
                                )
                                Text(status.displayName)
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showStatusDialog = false }) {
                    Text("Batal")
                }
            }
        )
    }


    // Delete Confirmation Dialog
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Hapus Laporan") },
            text = { Text("Apakah Anda yakin ingin menghapus laporan ini?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        scope.launch {
                            viewModel.deleteReport(report)
                            navController.popBackStack()
                        }
                    }
                ) {
                    Text("Hapus")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Batal")
                }
            }
        )
    }
}