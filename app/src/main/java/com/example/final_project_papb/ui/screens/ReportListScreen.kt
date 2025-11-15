// File: ui/screens/ReportListScreen.kt
package com.example.final_project_papb.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.final_project_papb.data.model.ReportStatus
import com.example.final_project_papb.ui.components.ReportCard
import com.example.final_project_papb.ui.navigation.Screen
import com.example.final_project_papb.ui.viewmodel.ReportViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportListScreen(
    navController: NavController,
    viewModel: ReportViewModel
) {
    var selectedStatus by remember { mutableStateOf<ReportStatus?>(null) }
    var showFilterDialog by remember { mutableStateOf(false) }

    val allReports by viewModel.allReports.collectAsState()
    val filteredReports = remember(allReports, selectedStatus) {
        if (selectedStatus == null) {
            allReports
        } else {
            allReports.filter { it.status == selectedStatus }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Daftar Laporan")
                        if (selectedStatus != null) {
                            Text(
                                selectedStatus!!.displayName,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali")
                    }
                },
                actions = {
                    IconButton(onClick = { showFilterDialog = true }) {
                        Icon(Icons.Default.FilterList, contentDescription = "Filter")
                    }
                }
            )
        }
    ) { paddingValues ->
        if (filteredReports.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "Tidak ada laporan",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.outline
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filteredReports) { report ->
                    ReportCard(
                        report = report,
                        onClick = {
                            navController.navigate(Screen.ReportDetail.createRoute(report.id))
                        }
                    )
                }
            }
        }
    }

    // Filter Dialog
    if (showFilterDialog) {
        AlertDialog(
            onDismissRequest = { showFilterDialog = false },
            title = { Text("Filter Status") },
            text = {
                Column {
                    TextButton(
                        onClick = {
                            selectedStatus = null
                            showFilterDialog = false
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Semua")
                    }
                    ReportStatus.values().forEach { status ->
                        TextButton(
                            onClick = {
                                selectedStatus = status
                                showFilterDialog = false
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(status.displayName)
                        }

                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showFilterDialog = false }) {
                    Text("Tutup")
                }
            }
        )
    }
}