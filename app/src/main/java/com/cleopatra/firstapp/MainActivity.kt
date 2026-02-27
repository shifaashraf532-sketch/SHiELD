package com.cleopatra.firstapp

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

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            SHEildAppNavigation(navController)
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
        Spacer(Modifier.height(40.dp))

        // SOS Button
        Button(
            onClick = {
                try {
                    // FIX: Modern way to get SmsManager from System Services
                    val smsManager: SmsManager = context.getSystemService(SmsManager::class.java)

                    // Replace "1234567890" with a real test number
                    smsManager.sendTextMessage(
                        "1234567890",
                        null,
                        "EMERGENCY! I need help. My location: [Mock Link]",
                        null,
                        null
                    )
                    Toast.makeText(context, "SOS Sent!", Toast.LENGTH_LONG).show()
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(context, "Error: Check SMS Permissions", Toast.LENGTH_SHORT).show()
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
        Spacer(Modifier.height(40.dp))

        Text("Route Status: ${if (isDiverged) "⚠️ DIVERGED" else "✅ ON TRACK"}")

        Button(onClick = { isDiverged = !isDiverged }, Modifier.padding(top = 20.dp)) {
            Text("Simulate Route Deviation")
        }

        if (isDiverged) {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE)),
                modifier = Modifier.padding(top = 20.dp)
            ) {
                Text(
                    "ALERT: Vehicle is off-route. Trusted contacts notified.",
                    Modifier.padding(16.dp),
                    color = Color.Red
                )
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
        Text(
            "FORMAL COMPLAINT DRAFT:\n\nTo: The SHO, Police Station\nSubject: Complaint for Harassment\n\nI am writing to report an incident regarding...",
            color = Color.DarkGray
        )
        Spacer(Modifier.height(40.dp))
        Button(onClick = { navController.popBackStack() }) { Text("Back Home") }
    }
}