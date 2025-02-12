package com.example.mapd_721_a2_harsimran_singh

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mapd_721_a2_harsimran_singh.ui.theme.MAPD721A2Harsimran_SinghTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MAPD721A2Harsimran_SinghTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    DisplayHealthData(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun DisplayHealthData(modifier: Modifier) {
    var heartRateField by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf("") }
    var selectedTime by remember { mutableStateOf("") }
    val itemsList = List(3) { "Item $it" }

    // The Column is wrapped inside a LazyColumn for the entire content to be scrollable
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "MAPD-721-Assignment 2",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .background(Color(0xFF000080), shape = RoundedCornerShape(6.dp))
                .padding(8.dp)
                .fillMaxWidth()

        )

        Spacer(modifier = Modifier.height(16.dp))

        // Heart Rate Input
        OutlinedTextField(
            value = heartRateField,
            onValueChange = { heartRateField = it },
            label = {
                Text(
                    "Heart Rate (1-300bpm)",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 5.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Date and Time Picker
        DatePicker(selectedDate = selectedDate, onDateChange = { selectedDate = it })
        TimePicker(selectedTime = selectedTime, onTimeChange = { selectedTime = it })

        Spacer(modifier = Modifier.height(16.dp))

        // Load Button
        Button(
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp),
            enabled = true,
            colors = ButtonDefaults.buttonColors(
                contentColor = Color.White,
                containerColor = Color.Blue
            ),
            shape = RoundedCornerShape(12.dp),
            content = { Text("Load") },
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Save Button
        Button(
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp),
            enabled = true,
            colors = ButtonDefaults.buttonColors(
                contentColor = Color.White,
                containerColor = Color(0xFF006400)
            ),
            shape = RoundedCornerShape(12.dp),
            content = { Text("Save") },
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Heart Rate History Section
        Text(
            "Heart Rate History",
            fontSize = 23.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Red
        )

        // LazyColumn for scrolling through the heart rate history
        HeartRateHistory(
            itemsList = itemsList,
            heartRateField = heartRateField,
            selectedDate = selectedDate,
            selectedTime = selectedTime
        )


        // Bottom Section (Name and Student ID)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .background(Color(0xFFABABAB), shape = RoundedCornerShape(16.dp))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center // Center alignment
            ) {
                Text(
                    text = "Name: Harsimran Singh",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Student ID: 301500536",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
            }
        }
    }
}

@Composable
fun HeartRateHistory(
    itemsList: List<String>,
    heartRateField: String,
    selectedDate: String,
    selectedTime: String
){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(200.dp)
            .background(Color(0xFFABABAB), shape = RoundedCornerShape(16.dp))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()) // Enable scroll on the column
        ) {
            // Display the first 5 items
            itemsList.forEach { item ->
                RowItem(
                    heartRate = heartRateField,
                    selectedDate = selectedDate,
                    selectedTime = selectedTime
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

@Composable
fun TimePicker(selectedTime: String, onTimeChange: (String) -> Unit){
    val context = LocalContext.current
    val calender = Calendar.getInstance()
    val hour = calender.get(Calendar.HOUR)
    val minute = calender.get(Calendar.MINUTE)

//    var selectedTime by remember { mutableStateOf("") }
    var showTimePicker by remember { mutableStateOf(false) }

    if(showTimePicker){
        TimePickerDialog(
            context, {
                _, selectedHour, selectedMinute ->
                val time = "$selectedHour:${selectedMinute.toString().padStart(2, '0')}"
                onTimeChange(time)
                showTimePicker = false
            },
            hour, minute, false
        ).show()
    }

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Using a Card as a date selection button
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showTimePicker = true }
                .padding(6.dp, 10.dp, 6.dp, 10.dp),
            shape = MaterialTheme.shapes.large,
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Select Date",
                    tint = Color(0xFF6200EE),
                    modifier = Modifier.padding(end = 16.dp)
                )
                Text(
                    text = if (selectedTime.isEmpty()) "Select Time (hh:mm)" else "Time: $selectedTime",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = if (selectedTime.isEmpty()) Color.Gray else Color.Black
                )
            }
        }

    }
}

@Composable
fun DatePicker(selectedDate: String, onDateChange: (String) -> Unit) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    var showDatePicker by remember { mutableStateOf(false) }

    // Show DatePickerDialog when showDatePicker is true
    if (showDatePicker) {
        DatePickerDialog(
            context,
            { _, selectedYear, selectedMonth, selectedDay ->
                val date = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                onDateChange(date)
                showDatePicker = false
            },
            year, month, day
        ).show()
    }

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Using a Card as a date selection button
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showDatePicker = true }
                .padding(6.dp, 10.dp, 6.dp, 10.dp),
            shape = MaterialTheme.shapes.large,
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Select Date",
                    tint = Color(0xFF6200EE), // Icon color
                    modifier = Modifier.padding(end = 16.dp)
                )
                Text(
                    text = if (selectedDate.isEmpty()) "Select Date (DD/MM/YYYY)" else "Date: $selectedDate",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold), // Correct style
                    color = if (selectedDate.isEmpty()) Color.Gray else Color.Black
                )
            }
        }

    }
}




@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MAPD721A2Harsimran_SinghTheme {
        DisplayHealthData(modifier = Modifier)
    }
}