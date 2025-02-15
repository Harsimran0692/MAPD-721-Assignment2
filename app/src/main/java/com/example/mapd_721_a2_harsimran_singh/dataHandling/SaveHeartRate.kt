package com.example.mapd_721_a2_harsimran_singh.dataHandling

import android.util.Log
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.records.HeartRateRecord
import androidx.health.connect.client.records.metadata.Metadata
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

suspend fun saveHeartRate(client: HealthConnectClient, bpm: Int, date: String, time: String) {
    try {
        // Define the date and time format
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")

        // Combine date and time into a single string
        val formattedDateTime = "$date $time"

        // Parse the formatted date-time string into a LocalDateTime object
        val dateTime = LocalDateTime.parse(formattedDateTime, formatter)

        // Convert LocalDateTime to Instant using UTC timezone
        val startInstant = dateTime.toInstant(ZoneOffset.UTC)
        val endInstant = startInstant.plusSeconds(30)  // Adjust duration if needed

        // Create metadata containing date and time information
        val metadata = Metadata(mapOf("date" to date, "time" to time).toString())

        // Create a HeartRateRecord with the provided data
        val record = HeartRateRecord(
            startTime = startInstant,
            endTime = endInstant,
            startZoneOffset = ZoneOffset.UTC,
            endZoneOffset = ZoneOffset.UTC,
            samples = listOf(HeartRateRecord.Sample(time = startInstant, beatsPerMinute = bpm.toLong())),
            metadata = metadata
        )

        // Insert the heart rate record into Health Connect
        client.insertRecords(listOf(record))
        Log.d("HealthConnect", "Heart rate saved: $bpm BPM")
    } catch (e: Exception) {
        // Log the error in case saving fails
        Log.e("HealthConnect", "Failed to save heart rate", e)
    }
}
