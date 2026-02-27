package com.cleopatra.firstapp

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.telephony.SmsManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import kotlin.math.sqrt

class MainActivity : ComponentActivity(), SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private var acceleration = 0f
    private var currentAcceleration = 0f
    private var lastAcceleration = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Shake Detection Sensors
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL)
        acceleration = 10f
        currentAcceleration = SensorManager.GRAVITY_EARTH
        lastAcceleration = SensorManager.GRAVITY_EARTH

        setContent {
            val navController = rememberNavController()
            SHEildAppNavigation(navController)
        }
    }

    // Shake Detection Logic
    override fun onSensorChanged(event: SensorEvent) {
        val x = event.values[0]
        val y = event.values[1]
        val z = event.values[2]
        lastAcceleration = currentAcceleration
        currentAcceleration = sqrt(x * x + y * y + z * z)
        val delta = currentAcceleration - lastAcceleration
        acceleration = acceleration * 0.9f + delta

        if (acceleration > 12) { // Threshold for shake
            sendSilentSOS(this)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    private fun sendSilentSOS(context: Context) {
        try {
            val smsManager: SmsManager = context.getSystemService(SmsManager::class.java)
            smsManager.sendTextMessage("1234567890", null, "SHAKE DETECTED: Emergency SOS! Location: [Mock]", null, null)
            Toast.makeText(context, "Shake Detected! SOS Sent.", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

@Composable
fun SHEildAppNavigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "home") {
        composable("home") { SHEildHomeScreen(navController) }
        composable("legal") { LegalGuideScreen(navController) }
        composable("taxi") { SafeTaxiScreen(navController) }
    }
}

@Composable
fun SHEildHomeScreen(navController: NavHostController) {
    val context = LocalContext.current
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("SHEild", fontSize = 42.sp, fontWeight = FontWeight.Bold, color = Color(0xFF6200EE))
        Text("Safety & Empowerment", fontSize = 14.sp, color = Color.Gray)
        Spacer(Modifier.height(40.dp))

        Button(
            onClick = {
                try {
                    val smsManager: SmsManager = context.getSystemService(SmsManager::class.java)
                    smsManager.sendTextMessage("1234567890", null, "Manual SOS triggered!", null, null)
                    Toast.makeText(context, "SOS Sent!", Toast.LENGTH_LONG).show()
                } catch (e: Exception) {
                    // Fixed: Using 'e' to avoid unused parameter warning
                    e.printStackTrace()
                    Toast.makeText(context, "Grant SMS Permission in Settings", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth().height(80.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
        ) {
            Text("🚨 SILENT SOS", color = Color.White, fontSize = 20.sp)
        }

        Spacer(modifier = Modifier.height(20.dp))
        Button(onClick = { navController.navigate("taxi") }, modifier = Modifier.fillMaxWidth()) {
            Text("🚕 SAFE-TAXI TRACKER")
        }
        Spacer(modifier = Modifier.height(15.dp))
        Button(onClick = { navController.navigate("legal") }, modifier = Modifier.fillMaxWidth()) {
            Text("⚖️ AI LEGAL GUIDE")
        }
    }
}

@Composable
fun SafeTaxiScreen(navController: NavHostController) {
    var isDiverged by remember { mutableStateOf(false) }
    Column(Modifier.fillMaxSize().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Safe-Taxi Monitor", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(20.dp))
        Text("Route Status: ${if (isDiverged) "⚠️ DIVERGED" else "✅ ON TRACK"}")
        Button(onClick = { isDiverged = !isDiverged }, Modifier.padding(top = 20.dp)) {
            Text("Simulate Route Deviation")
        }
        if (isDiverged) {
            Card(colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE)), modifier = Modifier.padding(top = 20.dp)) {
                Text("ALERT: Vehicle off-route. Contacts notified.", Modifier.padding(16.dp), color = Color.Red)
            }
        }
        Spacer(Modifier.weight(1f))
        Button(onClick = { navController.popBackStack() }) { Text("Back Home") }
    }
}

@Composable
fun LegalGuideScreen(navController: NavHostController) {
    Column(Modifier.fillMaxSize().padding(24.dp).verticalScroll(rememberScrollState())) {
        Text("AI Legal Guide", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(20.dp))
        Text("FORMAL COMPLAINT DRAFT:\n\nTo: The SHO, Police Station\nSubject: Harassment Complaint\n\nI am reporting an incident...", color = Color.DarkGray)
        Spacer(Modifier.height(40.dp))
        Button(onClick = { navController.popBackStack() }) { Text("Back Home") }
    }
}
