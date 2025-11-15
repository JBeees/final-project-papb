// File: ui/screens/MapScreen.kt
package com.example.final_project_papb.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.final_project_papb.data.model.Report
import com.example.final_project_papb.ui.navigation.Screen
import com.example.final_project_papb.ui.viewmodel.ReportViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    navController: NavController,
    viewModel: ReportViewModel
) {
    val reports by viewModel.allReports.collectAsState()

    // Default location (Malang, Indonesia)
    val defaultLocation = LatLng(-7.9797, 112.6304)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(defaultLocation, 13f)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Peta Laporan") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                properties = MapProperties(isMyLocationEnabled = false),
                uiSettings = MapUiSettings(
                    zoomControlsEnabled = true,
                    myLocationButtonEnabled = true
                )
            ) {
                reports.forEach { report ->
                    val position = LatLng(report.latitude, report.longitude)

                    Marker(
                        state = MarkerState(position = position),
                        title = report.title,
                        snippet = report.category.displayName,
                        onInfoWindowClick = {
                            navController.navigate(Screen.ReportDetail.createRoute(report.id))
                        }
                    )
                }
            }
        }
    }
}