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
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(200.dp)
            .background(Color(0xFFABABAB), shape = RoundedCornerShape(16.dp))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize() // Ensures it expands fully within Box
                .verticalScroll(rememberScrollState())
                .padding(8.dp) // Adds spacing for better scrolling
        ) {
            itemsList.forEach { record ->
                val heartRate = record.samples.firstOrNull()?.beatsPerMinute?.toString() ?: "N/A"
                val dateTime = record.startTime.atOffset(ZoneOffset.UTC)
                    .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))

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
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text = heartRate, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Text(text = selectedDate, fontSize = 18.sp, fontWeight = FontWeight.Normal)
        Text(text = selectedTime, fontSize = 18.sp, fontWeight = FontWeight.Normal)
    }
}
