package com.example.mapd_721_a2_harsimran_singh.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.health.connect.client.records.HeartRateRecord
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@Composable
fun HeartRateHistory(itemsList: List<HeartRateRecord>) {
    // Box container for heart rate history section
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(200.dp)
            .background(Color(0xFFABABAB), shape = RoundedCornerShape(16.dp)) // Grey background with rounded corners
    ) {
        // Column to display multiple heart rate records with scroll functionality
        Column(
            modifier = Modifier
                .fillMaxSize() // Ensures it takes full space within the Box
                .verticalScroll(rememberScrollState()) // Enables scrolling when list grows
                .padding(8.dp) // Adds inner padding for better spacing
        ) {
            // Loop through each heart rate record and display its details
            itemsList.forEach { record ->
                val heartRate = record.samples.firstOrNull()?.beatsPerMinute?.toString() ?: "N/A" // Get BPM or "N/A" if empty
                val dateTime = record.startTime.atOffset(ZoneOffset.UTC)
                    .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) // Format timestamp

                // Extract date and time separately
                RowItem(
                    heartRate = heartRate,
                    selectedDate = dateTime.split(" ")[0],
                    selectedTime = dateTime.split(" ")[1]
                )
            }
        }
    }
}

@Composable
fun RowItem(heartRate: String, selectedDate: String, selectedTime: String) {
    // Row layout for displaying heart rate, date, and time in a structured manner
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp), // Adds spacing between items
        horizontalArrangement = Arrangement.SpaceAround, // Distributes items evenly
        verticalAlignment = Alignment.CenterVertically, // Aligns items in the center
    ) {
        // Display heart rate value
        Text(text = heartRate, fontSize = 18.sp, fontWeight = FontWeight.Bold)

        // Display recorded date
        Text(text = selectedDate, fontSize = 18.sp, fontWeight = FontWeight.Normal)

        // Display recorded time
        Text(text = selectedTime, fontSize = 18.sp, fontWeight = FontWeight.Normal)
    }
}

