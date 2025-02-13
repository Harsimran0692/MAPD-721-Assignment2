package com.example.mapd_721_a2_harsimran_singh

import android.app.Application
import androidx.activity.result.contract.ActivityResultContract
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.PermissionController
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.WeightRecord
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HealthConnectManager : Application() {

    // Initialize the HealthConnectClient
    private lateinit var healthConnectClient: HealthConnectClient

    override fun onCreate() {
        super.onCreate()
        // Initialize HealthConnectClient
        healthConnectClient = HealthConnectClient.getOrCreate(applicationContext)
    }

    // Define required permissions for reading and writing WeightRecord
    private val permissions = setOf(
        HealthPermission.getReadPermission(WeightRecord::class),
        HealthPermission.getWritePermission(WeightRecord::class)
    )

    // Function to check if all required permissions are granted
    suspend fun hasAllPermissions(): Boolean {
        return withContext(Dispatchers.IO) {
            // Check if all required permissions are granted
            val grantedPermissions =
                healthConnectClient.permissionController.getGrantedPermissions()
            grantedPermissions.containsAll(permissions)
        }
    }

    // Function to return the contract for requesting permissions
    fun requestPermissionsActivityContract(): ActivityResultContract<Set<String>, Set<String>> {
        return PermissionController.createRequestPermissionResultContract()
    }

    // Expose required permissions for external use (i.e., MainActivity)
    fun getRequiredPermissions(): Set<String> {
        return permissions.map { it.toString() }
            .toSet() // Convert permission object to string using toString()
    }
}
