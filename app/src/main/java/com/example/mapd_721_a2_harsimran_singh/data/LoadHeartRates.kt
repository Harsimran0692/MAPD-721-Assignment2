package com.example.mapd_721_a2_harsimran_singh.data

import android.util.Log
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.records.HeartRateRecord
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter
import java.time.Instant

suspend fun loadHeartRates(client: HealthConnectClient): List<HeartRateRecord> {
    return try {
        val now = Instant.now()

        // Ensure TimeRangeFilter is available, or create a filter to match your needs
        val timeRangeFilter = TimeRangeFilter.before(now) // Ensure this is the correct filter

        val response = client.readRecords(
            ReadRecordsRequest(
                recordType = HeartRateRecord::class,
                timeRangeFilter = timeRangeFilter
            )
        )

        // Return the loaded heart rate records
        response.records
    } catch (e: Exception) {
        Log.e("HealthConnect", "Failed to load heart rate records", e)
        emptyList()
    }
}