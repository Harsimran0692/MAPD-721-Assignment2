package com.example.mapd_721_a2_harsimran_singh

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.HeartRateRecord
import androidx.lifecycle.lifecycleScope
import com.example.mapd_721_a2_harsimran_singh.components.DatePicker
import com.example.mapd_721_a2_harsimran_singh.components.HeartRateHistory
import com.example.mapd_721_a2_harsimran_singh.components.TimePicker
import com.example.mapd_721_a2_harsimran_singh.data.loadHeartRates
import com.example.mapd_721_a2_harsimran_singh.data.saveHeartRate
import com.example.mapd_721_a2_harsimran_singh.ui.theme.MAPD721A2Harsimran_SinghTheme
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    private lateinit var healthConnectManager: HealthConnectManager

    // Define required permissions for reading and writing heart rate data
    private val requiredPermissions = setOf(
        HealthPermission.getReadPermission(androidx.health.connect.client.records.HeartRateRecord::class),
        HealthPermission.getWritePermission(androidx.health.connect.client.records.HeartRateRecord::class)
    )

    // Register the permissions launcher to request multiple permissions
    private val requestPermissionsLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions.values.all { it }) {
            Log.d("HealthConnect", "All required permissions granted!")
        } else {
            Log.e("HealthConnect", "Some permissions are missing!")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize HealthConnectManager
        healthConnectManager = application as HealthConnectManager

        // Check and request permissions if needed
        checkAndRequestPermissions()

        // Setup the UI using Jetpack Compose
        setContent {
            MAPD721A2Harsimran_SinghTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    DisplayHealthData(
                        modifier = Modifier.padding(innerPadding),
                        context = LocalContext.current
                    )
                }
            }
        }
    }

    // Function to check if all required permissions are granted, otherwise request them
    private fun checkAndRequestPermissions() {
        lifecycleScope.launch {
            val hasPermissions = healthConnectManager.hasAllPermissions()
            if (!hasPermissions) {
                requestPermissionsLauncher.launch(requiredPermissions.toTypedArray())
            } else {
                Log.d("HealthConnect", "All required permissions already granted!")
            }
        }
    }
}

@Composable
fun DisplayHealthData(modifier: Modifier = Modifier, context: Context) {
    var heartRateField by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf("") }
    var selectedTime by remember { mutableStateOf("") }
    var historyList by remember { mutableStateOf<List<HeartRateRecord>>(emptyList()) }
    val coroutineScope = rememberCoroutineScope()
    val client = HealthConnectClient.getOrCreate(context)

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Title text
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

        // Input field for heart rate
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

        // Date picker and time picker
        DatePicker(selectedDate = selectedDate, onDateChange = { selectedDate = it })
        TimePicker(selectedTime = selectedTime, onTimeChange = { selectedTime = it })

        Spacer(modifier = Modifier.height(16.dp))

        // Load heart rate history button
        Button(
            onClick = {
                coroutineScope.launch {
                    historyList = loadHeartRates(client)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp),
            colors = ButtonDefaults.buttonColors(
                contentColor = Color.White,
                containerColor = Color.Blue
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Load")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Save heart rate button
        Button(
            onClick = {
                val bpm = heartRateField.toIntOrNull()
                if (bpm != null && bpm in 1..300) {
                    coroutineScope.launch {
                        try {
                            saveHeartRate(client, bpm, selectedDate, selectedTime)
                            Toast.makeText(context, "Heart rate saved: $bpm BPM", Toast.LENGTH_SHORT).show()
                        } catch (e: Exception) {
                            Log.e("HealthConnect", "Failed to save heart rate", e)
                            Toast.makeText(context, "Failed to save heart rate", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(context, "Enter a valid heart rate (1-300 BPM)", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp),
            colors = ButtonDefaults.buttonColors(
                contentColor = Color.White,
                containerColor = Color(0xFF006400)
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Save")
        }

        // Section for displaying heart rate history
        Text(
            "Heart Rate History",
            fontSize = 23.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Red
        )

        HeartRateHistory(itemsList = historyList)

        // Box containing student information
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
                horizontalAlignment = Alignment.CenterHorizontally
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



@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MAPD721A2Harsimran_SinghTheme {
        DisplayHealthData(modifier = Modifier, LocalContext.current)
    }
}