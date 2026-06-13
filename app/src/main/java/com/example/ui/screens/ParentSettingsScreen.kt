package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import com.example.ui.viewmodel.DhivehiViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ParentSettingsScreen(viewModel: DhivehiViewModel) {
    var isPinVerified by remember { mutableStateOf(false) }
    var enteredPin by remember { mutableStateOf("") }
    var pinErrorMsg by remember { mutableStateOf("") }

    val profile by viewModel.userProfile.collectAsState()
    val progressList by viewModel.lessonProgress.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Parents Zone / ބެލެނިވެރިން") },
                navigationIcon = {
                    IconButton(
                        onClick = { viewModel.setScreen("dashboard") },
                        modifier = Modifier.testTag("parent_back_btn")
                    ) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = LagoonSkyLight)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(LagoonSkyLight)
        ) {
            if (!isPinVerified) {
                // Pin Verification Block (Gatekeeper)
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("🔒 Parents Gateway 🔒", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = MidnightDeepBlue)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                "To keep our kids safe, please verify your Parent PIN code to access settings and statistics. (Default PIN: 1234)",
                                color = Color.Gray,
                                fontSize = 12.sp,
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            OutlinedTextField(
                                value = enteredPin,
                                onValueChange = { enteredPin = it },
                                visualTransformation = PasswordVisualTransformation(),
                                placeholder = { Text("Enter 4-Digit PIN") },
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("parent_pin_input")
                            )

                            if (pinErrorMsg.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(pinErrorMsg, color = ReefCoral, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                            Button(
                                onClick = {
                                    val success = viewModel.unlockScreenWithPin(enteredPin)
                                    if (success) {
                                        isPinVerified = true
                                        pinErrorMsg = ""
                                    } else {
                                        pinErrorMsg = "Incorrect PIN! Please try again."
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = ReefCoral),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                                    .testTag("parent_pin_verify_btn")
                            ) {
                                Text("Verify to Access", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            } else {
                // Parents settings Panel
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Item 1: Kid Profile settings
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Filled.Person, contentDescription = null, tint = OceanTeal)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Kid Identity Details", fontWeight = FontWeight.Bold, color = MidnightDeepBlue)
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            Text("Name: ${profile.name}", fontSize = 14.sp, color = Color.DarkGray)
                            Text("Age group category: Cohort ${profile.ageGroup} (${if (profile.ageGroup == 1) "3-5" else if (profile.ageGroup == 2) "6-8" else "9-12"} Years)", fontSize = 14.sp, color = Color.DarkGray)
                        }
                    }

                    // Item 2: Screen time limiter countdown controller
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Filled.Settings, contentDescription = null, tint = OceanTeal)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Screen Time Management", fontWeight = FontWeight.Bold, color = MidnightDeepBlue)
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                "Configure children's daily usage constraints to maintain healthy eye-care focus.",
                                fontSize = 11.sp,
                                color = Color.Gray
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                listOf(15, 30, 45, 60).forEach { limit ->
                                    val isSelected = profile.screenTimeLimitMinutes == limit
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(if (isSelected) OceanTeal else Color(0xFFF1F5F9))
                                            .clickable { viewModel.updateScreenLimit(limit) }
                                            .padding(vertical = 10.dp)
                                            .testTag("time_limit_$limit"),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "$limit min",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isSelected) Color.White else Color.Black
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Item 3: Statistics / Lesson summaries tracker
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Filled.Star, contentDescription = null, tint = ReefCoral)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Cumulative Progress Logs", fontWeight = FontWeight.Bold, color = MidnightDeepBlue)
                            }
                            Spacer(modifier = Modifier.height(12.dp))

                            val categories = listOf("thaana" to "Alphabet", "numbers" to "Numbers", "colors" to "Colors", "shapes" to "Shapes", "vocabulary" to "Vocabulary", "grammar" to "Grammar")

                            categories.forEach { (catId, catLabel) ->
                                val completedCount = progressList.filter { it.category == catId }.size
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(text = catLabel, fontSize = 13.sp, color = Color.DarkGray)
                                    Text(
                                        text = "$completedCount units completed",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = OceanTeal
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}
