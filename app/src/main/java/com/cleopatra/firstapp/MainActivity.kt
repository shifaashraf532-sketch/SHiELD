package com.cleopatra.firstapp

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun SHEildHomeScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) { // <--- Content starts here
        Text(
            text = "SHEild",
            fontSize = 32.sp
        )

        Spacer(modifier = Modifier.height(40.dp))

        Button(
            onClick = { println("Emergency Pressed!") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("🚨 Emergency")
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = { println("Safe Route Pressed!") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("🗺️ Safe Route")
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = { println("Legal Guide Pressed!") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("⚖️ Legal Guide")
        }
    } // <--- Closes Column
} // <--- Closes Function

@Preview(showBackground = true)
@Composable
fun SHEildPreview() {
    SHEildHomeScreen()
}