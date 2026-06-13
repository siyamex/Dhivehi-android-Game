package com.example.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import com.example.ui.viewmodel.DhivehiViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(viewModel: DhivehiViewModel) {
    val profile by viewModel.userProfile.collectAsState()
    val progressList by viewModel.lessonProgress.collectAsState()

    // Determine Mascot details
    val mascotEmoji = when (profile.avatarId) {
        "dolphin" -> "🐬"
        "turtle" -> "🐢"
        "parrot" -> "🦜"
        "crab" -> "🦀"
        else -> "🐬"
    }
    val mascotName = when (profile.avatarId) {
        "dolphin" -> "Koamas the Dolphin"
        "turtle" -> "Velaa the Turtle"
        "parrot" -> "Dhanbu the Parrot"
        "crab" -> "Kaku the Coral Crab"
        else -> "Koamas"
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .border(3.dp, Color.White, CircleShape)
                                .background(OrangeStreak, CircleShape)
                                .shadow(2.dp, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = profile.name.firstOrNull()?.uppercase() ?: "K",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "LEVEL ${profile.level}",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = MidnightDeepBlue.copy(alpha = 0.5f),
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = profile.name.ifEmpty { "Kuda" },
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Black,
                                color = MidnightDeepBlue,
                                modifier = Modifier.offset(y = (-2).dp)
                            )
                        }
                    }
                },
                actions = {
                    Row(
                        modifier = Modifier.padding(end = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        // Stars Badge Capsule
                        Row(
                            modifier = Modifier
                                .background(Color.White, RoundedCornerShape(20.dp))
                                .border(1.9.dp, Color(0xFFF1F5F9), RoundedCornerShape(20.dp))
                                .padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "⭐", fontSize = 14.sp)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${profile.stars}",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Black,
                                color = MidnightDeepBlue.copy(alpha = 0.8f)
                            )
                        }

                        // Streak Badge Capsule
                        Row(
                            modifier = Modifier
                                .background(Color.White, RoundedCornerShape(20.dp))
                                .border(1.9.dp, Color(0xFFF1F5F9), RoundedCornerShape(20.dp))
                                .padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "🔥", fontSize = 14.sp)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${profile.dailyStreak}",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Black,
                                color = MidnightDeepBlue.copy(alpha = 0.8f)
                            )
                        }

                        IconButton(
                            onClick = { viewModel.setScreen("parent_settings") },
                            modifier = Modifier
                                .size(36.dp)
                                .background(Color.White, CircleShape)
                                .border(1.9.dp, Color(0xFFF1F5F9), CircleShape)
                                .testTag("parent_settings_button")
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Settings,
                                contentDescription = "Parents Corner",
                                tint = OceanTeal,
                                modifier = Modifier.size(20.dp)
                            )
                        }
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
            // Screen Time Warning
            val timeLimit = viewModel.timeLeftSeconds.collectAsState().value
            if (timeLimit < 300) { // Under 5 minutes left
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(ReefCoral)
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "⏳ Screen Time Limit active! ${timeLimit / 60} mins left. Learn offline!",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            // Streak & Progress Header Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .background(Color.White, RoundedCornerShape(24.dp))
                    .border(1.5.dp, Color(0xFFF1F5F9), RoundedCornerShape(24.dp))
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Streak info
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "🔥", fontSize = 26.sp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Column {
                        Text("DAILY STREAK", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = MidnightDeepBlue.copy(alpha = 0.4f), letterSpacing = 0.5.sp)
                        Text("${profile.dailyStreak} Days", fontSize = 15.sp, fontWeight = FontWeight.Black, color = OrangeStreak)
                    }
                }

                // XP Progress bar
                Column(modifier = Modifier.weight(1f).padding(horizontal = 16.dp)) {
                    val nextXpGoal = profile.level * 100
                    val currentLevelBase = (profile.level - 1) * 100
                    val relativeXp = profile.xp - currentLevelBase
                    val relativeGoal = 100
                    val progressValue = (relativeXp.toFloat() / relativeGoal).coerceIn(0f, 1f)

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text("XP PROGRESS", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = MidnightDeepBlue.copy(alpha = 0.4f), letterSpacing = 0.5.sp)
                        Text("$relativeXp / $relativeGoal", fontSize = 11.sp, fontWeight = FontWeight.Black, color = OceanTeal)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    LinearProgressIndicator(
                        progress = { progressValue },
                        color = OceanTeal,
                        trackColor = Color(0xFFE2E8F0),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                            .clip(RoundedCornerShape(5.dp))
                    )
                }
            }

            // Scrollable central area
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Featured Lesson Card (Vibrant banner from design HTML)
                FeaturedLessonCard(viewModel)

                // Mascot Tip Speech (dashed style)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .background(Color.White, RoundedCornerShape(24.dp))
                        .border(2.dp, PaleTealAccent, RoundedCornerShape(24.dp))
                        .clickable {
                            viewModel.speakText("Hi there! I am $mascotName. Let's learn Dhivehi language together on our beautiful islands!")
                        }
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = mascotEmoji, fontSize = 42.sp)
                    Spacer(modifier = Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "BUDDY TIP: $mascotName".uppercase(),
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = OceanTeal,
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Assalaamu Alaikum! Ready to find the hidden letters in the ocean today?",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = MidnightDeepBlue,
                            lineHeight = 17.sp
                        )
                    }
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .background(PaleTealAccent, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Filled.PlayArrow, contentDescription = "Listen voice", tint = OceanTeal, modifier = Modifier.size(16.dp))
                    }
                }

                Text(
                    text = "🌴 Island Learning Course Map 🌴",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Black,
                    color = PalmLeafGreen,
                    letterSpacing = 1.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )

                // Stop 1: Alphabet Island
                IslandPortalCard(
                    title = "Alphabet Island (ހ ށ ނ)",
                    description = "Learn the 24 letters of Thaana alphabet, track writing strokes, and finger trace characters.",
                    emoji = "🏝️",
                    completedCount = progressList.filter { it.category == "thaana" }.size,
                    totalCount = 24,
                    color = OceanTeal, // cyan
                    backgroundColor = PaleTealAccent,
                    onClick = { viewModel.setScreen("alphabet") },
                    tag = "island_alphabet"
                )

                // Stop 2: Numbers Lagoon
                IslandPortalCard(
                    title = "Numbers Lagoon (1 - 100)",
                    description = "Learn numbers, arithmetic math, shapes, colors and match visual objects.",
                    emoji = "🪸",
                    completedCount = progressList.filter { it.category == "numbers" || it.category == "colors" }.size,
                    totalCount = 18,
                    color = SandyGold, // amber
                    backgroundColor = SoftAmberAccent,
                    onClick = { viewModel.setScreen("numbers_colors") },
                    tag = "island_numbers"
                )

                // Stop 3: Vocabulary Beach
                IslandPortalCard(
                    title = "Vocabulary Beach",
                    description = "Learn Maldives plants, fruits, animals, and sea creatures with picture translations.",
                    emoji = "🌊",
                    completedCount = progressList.filter { it.category == "vocabulary" }.size,
                    totalCount = 12,
                    color = ReefCoral, // rose
                    backgroundColor = PaleCoralAccent,
                    onClick = { viewModel.setScreen("vocabulary") },
                    tag = "island_vocabulary"
                )

                // Stop 4: Grammar Cruiser
                IslandPortalCard(
                    title = "Grammar Cruiser",
                    description = "Speak/write nouns and verbs correctly. Master sentence structures.",
                    emoji = "⛵",
                    completedCount = progressList.filter { it.category == "grammar" }.size,
                    totalCount = 3,
                    color = LagoonAqua, // indigo
                    backgroundColor = SoftIndigoAccent,
                    onClick = { viewModel.setScreen("grammar") },
                    tag = "island_grammar"
                )

                // Stop 5: Stories & Songs Tower
                IslandPortalCard(
                    title = "Stories & Songs Tower",
                    description = "Folk stories, rhyming songs, and AI Story custom creation generator.",
                    emoji = "🦜",
                    completedCount = progressList.filter { it.category == "stories" }.size,
                    totalCount = 2,
                    color = PalmLeafGreen, // emerald
                    backgroundColor = SoftGreenAccent,
                    onClick = { viewModel.setScreen("stories_songs") },
                    tag = "island_stories"
                )

                // Stop 6: Interactive Game Arcade
                IslandPortalCard(
                    title = "Mascot Mini-Games Hub",
                    description = "Pop spelling balloons, play memory cards, and help the hungry fish swim!",
                    emoji = "🎮",
                    completedCount = progressList.filter { it.category == "games" }.size,
                    totalCount = 3,
                    color = OrangeStreak, // orange
                    backgroundColor = SoftOrangeAccent,
                    onClick = { viewModel.setScreen("games_hub") },
                    tag = "island_games"
                )

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun FeaturedLessonCard(viewModel: DhivehiViewModel) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFF06B6D4)), // cyan-500
        shape = RoundedCornerShape(32.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .testTag("featured_lesson_card")
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.linearGradient(
                        colors = listOf(Color(0xFF06B6D4), Color(0xFF0891B2))
                    )
                )
                .padding(24.dp)
        ) {
            // Right-aligned huge decorative Thaana character backdrop
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(96.dp)
                    .background(Color.White.copy(alpha = 0.2f), RoundedCornerShape(24.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "ހ",
                    fontSize = 52.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            // Foreground Content
            Column(
                modifier = Modifier.fillMaxWidth(0.65f),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "CONTINUE LEARNING",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFCFFAFE), // Cyan 100
                        letterSpacing = 1.25.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Thaana Alphabet",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                        lineHeight = 28.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Let's learn the letter 'Haa' (ހ) today!",
                        fontSize = 13.sp,
                        color = Color.White.copy(alpha = 0.9f),
                        lineHeight = 16.sp
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                Button(
                    onClick = { viewModel.setScreen("alphabet") },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    shape = RoundedCornerShape(16.dp),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp),
                    modifier = Modifier.height(40.dp)
                ) {
                    Text(
                        text = "START LESSON",
                        color = Color(0xFF0891B2),
                        fontWeight = FontWeight.Black,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

@Composable
fun IslandPortalCard(
    title: String,
    description: String,
    emoji: String,
    completedCount: Int,
    totalCount: Int,
    color: Color,
    backgroundColor: Color,
    onClick: () -> Unit,
    tag: String
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(28.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clickable { onClick() }
            .border(2.dp, backgroundColor.copy(alpha = 0.5f), RoundedCornerShape(28.dp))
            .testTag(tag)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(backgroundColor),
                contentAlignment = Alignment.Center
            ) {
                Text(text = emoji, fontSize = 32.sp)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Black,
                    color = MidnightDeepBlue
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = description,
                    fontSize = 12.sp,
                    color = MidnightDeepBlue.copy(alpha = 0.6f),
                    lineHeight = 15.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    val progressValue = if (totalCount > 0) completedCount.toFloat() / totalCount else 0f
                    LinearProgressIndicator(
                        progress = { progressValue.coerceIn(0f, 1f) },
                        color = color,
                        trackColor = Color(0xFFF1F5F9),
                        modifier = Modifier
                            .width(80.dp)
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp))
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "$completedCount/$totalCount Completed",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = color
                    )
                }
            }
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(backgroundColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.PlayArrow,
                    contentDescription = "Go",
                    tint = color,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}
