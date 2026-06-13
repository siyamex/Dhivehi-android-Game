package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import com.example.ui.viewmodel.DhivehiViewModel

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun OnboardingScreen(viewModel: DhivehiViewModel) {
    var name by remember { mutableStateOf("Kuda Kuda") }
    var selectedAgeGroup by remember { mutableStateOf(1) } // 1: 3-5, 2: 6-8, 3: 9-12
    var selectedAvatar by remember { mutableStateOf("dolphin") } // dolphin, turtle, parrot, crab

    val avatars = listOf(
        Triple("dolphin", "🐬", "Koamas the Dolphin"),
        Triple("turtle", "🐢", "Velaa the Turtle"),
        Triple("parrot", "🦜", "Dhanbu the Parrot"),
        Triple("crab", "🦀", "Kaku the Coral Crab")
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(LagoonSkyLight, SoftBeachSand)
                )
            )
            .windowInsetsPadding(WindowInsets.statusBars)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Header
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text(
                    text = "ދިވެހި ކުދިން",
                    fontSize = 38.sp,
                    fontWeight = FontWeight.Bold,
                    color = OceanTeal,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Dhivehi Kids",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = ReefCoral,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Your tropical learning adventure awaits!",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
            }

            // Central Form Card
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(28.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(vertical = 16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    // Item 1: Name Input
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "My Name / އަހަރެންގެ ނަން:",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = MidnightDeepBlue
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            placeholder = { Text("Enter name in Dhivehi or English") },
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("onboarding_name_input"),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = OceanTeal,
                                unfocusedBorderColor = Color.LightGray
                            )
                        )
                    }

                    // Item 2: Age cohorts with large kid-friendly touch indicators
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "How Old Are You? / އުމުރު:",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = MidnightDeepBlue
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            listOf(
                                1 to "3 - 5\nYears",
                                2 to "6 - 8\nYears",
                                3 to "9 - 12\nYears"
                            ).forEach { (group, label) ->
                                val isSelected = selectedAgeGroup == group
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(64.dp)
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(if (isSelected) OceanTeal else Color(0xFFF1F5F9))
                                        .border(
                                            width = 2.dp,
                                            color = if (isSelected) SandyGold else Color.Transparent,
                                            shape = RoundedCornerShape(16.dp)
                                        )
                                        .clickable {
                                            selectedAgeGroup = group
                                            viewModel.speakText("Age group selected")
                                        }
                                        .testTag("onboarding_age_$group"),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = label,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) Color.White else Color.Black,
                                        textAlign = TextAlign.Center,
                                        lineHeight = 16.sp
                                    )
                                }
                            }
                        }
                    }

                    // Item 3: Choose beautiful Maldives Mascot
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "Choose Your Buddy Mascot / މަސްކޮޓު:",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = MidnightDeepBlue
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            avatars.forEach { (avId, emoji, label) ->
                                val isSelected = selectedAvatar == avId
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(16.dp))
                                        .clickable {
                                            selectedAvatar = avId
                                            viewModel.speakText("Hi! I am your $label")
                                        }
                                        .padding(8.dp)
                                        .testTag("avatar_$avId")
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(56.dp)
                                            .clip(RoundedCornerShape(16.dp))
                                            .background(if (isSelected) ReefCoral else Color(0xFFE2E8F0))
                                            .border(
                                                width = 2.dp,
                                                color = if (isSelected) SandyGold else Color.Transparent,
                                                shape = RoundedCornerShape(16.dp)
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(text = emoji, fontSize = 28.sp)
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = avId.replaceFirstChar { it.uppercase() },
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = if (isSelected) ReefCoral else Color.Gray
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Navigation trigger
            Button(
                onClick = {
                    viewModel.saveProfileSettings(name, selectedAgeGroup, selectedAvatar)
                    viewModel.speakText("Welcome to Dhivehi Kids!")
                    viewModel.setScreen("dashboard")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .testTag("start_adventure_button"),
                colors = ButtonDefaults.buttonColors(containerColor = ReefCoral),
                shape = RoundedCornerShape(18.dp)
            ) {
                Icon(Icons.Filled.Star, contentDescription = "School info")
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Let's Go! / ފަށަމާ!",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(Icons.Filled.ArrowForward, contentDescription = null)
            }
        }
    }
}
