package com.example.final_project_papb.ui.viewmodel

import android.content.Context
import android.location.Geocoder
import android.location.Location
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import kotlinx.coroutines.tasks.await
import java.util.*

sealed class LocationState {
    object Idle : LocationState()
    object Loading : LocationState()
    data class Success(
        val latitude: Double,
        val longitude: Double,
        val address: String
    ) : LocationState()
    data class Error(val message: String) : LocationState()
}

class LocationHelper(
    private val context: Context,
    private val fusedLocationClient: FusedLocationProviderClient
) {
    suspend fun getCurrentLocation(): LocationState {
        return try {
            android.util.Log.d("GPS", "Memulai mendapatkan lokasi...")

            // Coba lastLocation dulu
            var location: Location? = fusedLocationClient.lastLocation.await()
            android.util.Log.d("GPS", "LastLocation result: $location")

            // Jika null, request current location
            if (location == null) {
                android.util.Log.d("GPS", "Requesting current location...")

                val cancellationTokenSource =
                    com.google.android.gms.tasks.CancellationTokenSource()

                location = fusedLocationClient.getCurrentLocation(
                    Priority.PRIORITY_HIGH_ACCURACY,
                    cancellationTokenSource.token
                ).await()

                android.util.Log.d("GPS", "CurrentLocation result: $location")
            }

            if (location != null) {
                val address = getAddressFromLocation(location.latitude, location.longitude)
                LocationState.Success(
                    latitude = location.latitude,
                    longitude = location.longitude,
                    address = address
                )
            } else {
                LocationState.Error(
                    "Tidak dapat mendapatkan lokasi. Pastikan GPS aktif dan coba lagi!"
                )
            }
        } catch (e: SecurityException) {
            android.util.Log.e("GPS", "SecurityException: ${e.message}")
            LocationState.Error("Permission error: ${e.message}")
        } catch (e: Exception) {
            android.util.Log.e("GPS", "Error: ${e.message}", e)
            LocationState.Error("Error: ${e.message}")
        }
    }

    private fun getAddressFromLocation(latitude: Double, longitude: Double): String {
        return try {
            val geocoder = Geocoder(context, Locale.getDefault())
            val addresses = geocoder.getFromLocation(latitude, longitude, 1)
            addresses?.firstOrNull()?.getAddressLine(0)
                ?: "Lat: $latitude, Lon: $longitude"
        } catch (e: Exception) {
            android.util.Log.e("GPS", "Geocoding error: ${e.message}")
            "Lat: $latitude, Lon: $longitude"
        }
    }
}