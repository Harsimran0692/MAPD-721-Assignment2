package com.example.mapd_721_a2_harsimran_singh.data

import android.util.Log
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.records.HeartRateRecord
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter
import java.time.Instant

suspend fun loadHeartRates(client: HealthConnectClient): List<HeartRateRecord> {
    return try {
        val now = Instant.now() // Get the current time

        // Create a time range filter to fetch heart rate records before the current time
        val timeRangeFilter = TimeRangeFilter.before(now)

        // Request heart rate records from Health Connect
        val response = client.readRecords(
            ReadRecordsRequest(
                recordType = HeartRateRecord::class, // Specify the type of record to read
                timeRangeFilter = timeRangeFilter   // Apply the time filter
            )
        )

        // Return the retrieved heart rate records
        response.records
    } catch (e: Exception) {
        // Log the error and return an empty list in case of failure
        Log.e("HealthConnect", "Failed to load heart rate records", e)
        emptyList()
    }
}
