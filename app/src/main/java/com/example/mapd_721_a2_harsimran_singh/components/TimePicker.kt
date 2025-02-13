package com.example.mapd_721_a2_harsimran_singh.components

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.icu.util.Calendar
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@SuppressLint("DefaultLocale")
@Composable
fun TimePicker(selectedTime: String, onTimeChange: (String) -> Unit) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val hour = calendar.get(Calendar.HOUR_OF_DAY) // Gets the current hour in 24-hour format
    val minute = calendar.get(Calendar.MINUTE) // Gets the current minute

    var showTimePicker by remember { mutableStateOf(false) } // State to control the visibility of the time picker dialog

    // Show the TimePickerDialog when showTimePicker is true
    if (showTimePicker) {
        TimePickerDialog(
            context, { _, selectedHour, selectedMinute ->
                val formattedTime = String.format("%02d:%02d", selectedHour, selectedMinute) // Format time as HH:mm
                onTimeChange(formattedTime) // Update the selected time
                showTimePicker = false // Close the dialog after selection
            },
            hour, minute, true // true for 24-hour format
        ).show()
    }

    // UI layout for time picker selection
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Clickable card to open the time picker dialog
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showTimePicker = true } // Opens time picker when clicked
                .padding(6.dp, 10.dp, 6.dp, 10.dp),
            shape = MaterialTheme.shapes.large,
        ) {
            // Row layout containing an icon and selected time text
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Clock icon to indicate time selection
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Select Time",
                    tint = Color(0xFF6200EE),
                    modifier = Modifier.padding(end = 16.dp)
                )
                // Display selected time or placeholder text
                Text(
                    text = if (selectedTime.isEmpty()) "Select Time (HH:mm)" else "Time: $selectedTime",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = if (selectedTime.isEmpty()) Color.Gray else Color.Black
                )
            }
        }
    }
}
