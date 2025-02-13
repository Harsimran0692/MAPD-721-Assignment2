package com.example.mapd_721_a2_harsimran_singh.data

import android.util.Log
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.records.HeartRateRecord
import androidx.health.connect.client.records.metadata.Metadata
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

suspend fun saveHeartRate(client: HealthConnectClient, bpm: Int, date: String, time: String) {
    try {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
        val formattedDateTime = "$date $time"
        val dateTime = LocalDateTime.parse(formattedDateTime, formatter)

        val startInstant = dateTime.toInstant(ZoneOffset.UTC)
        val endInstant = startInstant.plusSeconds(30)  // Adjust this if needed

        // Create metadata with date and time
        val metadata = Metadata(mapOf("date" to date, "time" to time).toString())

        // Prepare the record with the selected date and time
        val record = HeartRateRecord(
            startTime = startInstant,
            endTime = endInstant,
            startZoneOffset = ZoneOffset.UTC,
            endZoneOffset = ZoneOffset.UTC,
            samples = listOf(HeartRateRecord.Sample(time = startInstant, beatsPerMinute = bpm.toLong())),
            metadata = metadata
        )

        // Insert the record using the client
        client.insertRecords(listOf(record))
        Log.d("HealthConnect", "Heart rate saved: $bpm BPM")
    } catch (e: Exception) {
        Log.e("HealthConnect", "Failed to save heart rate", e)
    }
}