package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.screens.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.DhivehiViewModel
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    private val viewModel: DhivehiViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    contentWindowInsets = WindowInsets.safeDrawing
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        AppNavigationHost(viewModel)

                        // Global screen-time safety overlay (above everything)
                        ScreenTimeLockOverlay(viewModel)
                    }
                }
            }
        }
    }
}

@Composable
fun AppNavigationHost(viewModel: DhivehiViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsState()
    val profile by viewModel.userProfile.collectAsState()

    // Slide/fade transitions based on the selected screen
    AnimatedContent(
        targetState = currentScreen,
        transitionSpec = {
            fadeIn() togetherWith fadeOut()
        },
        label = "screen_nav"
    ) { screen ->
        when (screen) {
            "splash" -> SplashScreenStop(
                onTimeout = {
                    if (profile.name.isEmpty() || profile.name == "Kuda Kuda") {
                        viewModel.setScreen("onboarding")
                    } else {
                        viewModel.setScreen("dashboard")
                    }
                }
            )
            "onboarding" -> OnboardingScreen(viewModel)
            "dashboard" -> DashboardScreen(viewModel)
            "alphabet" -> AlphabetScreen(viewModel)
            "numbers_colors" -> NumbersColorsScreen(viewModel)
            "vocabulary" -> VocabularyScreen(viewModel)
            "grammar" -> ReadingGrammarScreen(viewModel)
            "stories_songs" -> StoriesSongsScreen(viewModel)
            "games_hub" -> GamesHubScreen(viewModel)
            "parent_settings" -> ParentSettingsScreen(viewModel)
            else -> DashboardScreen(viewModel)
        }
    }
}

/**
 * Visual Maldives-themed Splash screen
 */
@Composable
fun SplashScreenStop(onTimeout: () -> Unit) {
    LaunchedEffect(Unit) {
        delay(2000) // 2 sec visual experience
        onTimeout()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(OceanTeal, LagoonAqua)
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "🐳", fontSize = 80.sp)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "ދިވެހި ކުދިން",
                fontSize = 42.sp,
                fontWeight = FontWeight.Bold,
                color = SandyGold,
                textAlign = TextAlign.Center
            )
            Text(
                text = "Dhivehi Kids",
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Fun-filled language journeys in the Maldives",
                fontSize = 13.sp,
                color = Color.White.copy(alpha = 0.8f),
                textAlign = TextAlign.Center
            )
        }
    }
}

/**
 * Overlay preventing screen usage once child's screen-time has elapsed
 */
@Composable
fun ScreenTimeLockOverlay(viewModel: DhivehiViewModel) {
    val isLocked by viewModel.isScreenTimeLocked.collectAsState()
    var enteredUnlockPin by remember { mutableStateOf("") }
    var unlockErrorMsg by remember { mutableStateOf("") }

    if (isLocked) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MidnightDeepBlue.copy(alpha = 0.96f))
                .padding(24.dp)
                .clickable(enabled = false) {}, // Intercept touch events
            contentAlignment = Alignment.Center
        ) {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(28.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Filled.Lock,
                        contentDescription = null,
                        tint = ReefCoral,
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Screen Time Spent! / ވަގުތު ހަމަވެއްޖެ",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = MidnightDeepBlue,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Good job! You practiced Dhivehi beautifully today! Time to take a run outside, look at palm trees, or ask a parent to unlock.",
                        color = Color.Gray,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    OutlinedTextField(
                        value = enteredUnlockPin,
                        onValueChange = { enteredUnlockPin = it },
                        visualTransformation = PasswordVisualTransformation(),
                        placeholder = { Text("Parent PIN to bypass") },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("overlay_pin_input")
                    )

                    if (unlockErrorMsg.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = unlockErrorMsg,
                            color = ReefCoral,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            val success = viewModel.unlockScreenWithPin(enteredUnlockPin)
                            if (success) {
                                enteredUnlockPin = ""
                                unlockErrorMsg = ""
                            } else {
                                unlockErrorMsg = "Incorrect PIN code. Try again!"
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = ReefCoral),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("overlay_unlock_btn")
                    ) {
                        Icon(Icons.Filled.Lock, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Add 30 Minutes", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
