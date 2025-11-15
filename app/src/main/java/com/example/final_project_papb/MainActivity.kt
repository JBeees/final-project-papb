// File: MainActivity.kt
package com.example.final_project_papb

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.final_project_papb.ui.navigation.SafeCityNavigation
import com.example.final_project_papb.ui.theme.SafeCityTheme
import com.example.final_project_papb.ui.viewmodel.ReportViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: ReportViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SafeCityTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    SafeCityNavigation(
                        navController = navController,
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}