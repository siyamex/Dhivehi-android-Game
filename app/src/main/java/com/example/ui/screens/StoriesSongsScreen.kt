package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.DhivehiContent
import com.example.data.StaticStory
import com.example.ui.theme.*
import com.example.ui.viewmodel.DhivehiViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoriesSongsScreen(viewModel: DhivehiViewModel) {
    var activeSection by remember { mutableStateOf("Stories") } // Stories, Songs, AI Generator
    val progressList by viewModel.lessonProgress.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Stories & Songs / ވާހަކައާއި ލަވަ") },
                navigationIcon = {
                    IconButton(
                        onClick = { viewModel.setScreen("dashboard") },
                        modifier = Modifier.testTag("stories_back_button")
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
            // Section triggers
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .background(Color.White, RoundedCornerShape(16.dp))
                    .padding(6.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("Stories", "Songs", "AI Story").forEach { section ->
                    val isSelected = activeSection == section
                    Button(
                        onClick = { activeSection = section },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isSelected) OceanTeal else Color.Transparent,
                            contentColor = if (isSelected) Color.White else Color.Gray
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .testTag("section_btn_${section.lowercase().replace(" ", "")}"),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(text = section, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Body
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .padding(horizontal = 16.dp)
            ) {
                when (activeSection) {
                    "Stories" -> StoriesPanel(viewModel)
                    "Songs" -> SongsPanel(viewModel)
                    "AI Story" -> AiStoryGeneratorPanel(viewModel)
                }
            }
        }
    }
}

@Composable
fun StoriesPanel(viewModel: DhivehiViewModel) {
    var activeStory by remember { mutableStateOf<StaticStory?>(null) }
    var storyPage by remember { mutableStateOf(0) }
    var storyCompleted by remember { mutableStateOf(false) }

    if (activeStory == null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                "Offline Island Storybooks:",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = MidnightDeepBlue
            )

            DhivehiContent.stories.forEach { story ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            activeStory = story
                            storyPage = 0
                            storyCompleted = false
                            viewModel.speakText("Selected story: " + story.title)
                        }
                        .testTag("story_card_${story.id}")
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "📚", fontSize = 36.sp)
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                story.title,
                                fontWeight = FontWeight.Bold,
                                color = MidnightDeepBlue,
                                fontSize = 16.sp
                            )
                            Text(
                                story.desc,
                                fontSize = 11.sp,
                                color = Color.Gray,
                                lineHeight = 14.sp
                            )
                        }
                    }
                }
            }
        }
    } else {
        val story = activeStory!!
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = story.title,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = OceanTeal,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(18.dp))

                    // Narrative details
                    Text(
                        text = story.pages[storyPage],
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = MidnightDeepBlue,
                        textAlign = TextAlign.Center,
                        lineHeight = 32.sp,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = story.pagesEnglish[storyPage],
                        fontSize = 14.sp,
                        color = Color.DarkGray,
                        textAlign = TextAlign.Center,
                        lineHeight = 18.sp,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Page ${storyPage + 1} of ${story.pages.size}",
                            fontSize = 12.sp,
                            color = Color.LightGray
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            if (storyPage > 0) {
                                Button(
                                    onClick = { storyPage-- },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray)
                                ) {
                                    Text("Back", fontSize = 11.sp)
                                }
                            }
                            if (storyPage < story.pages.size - 1) {
                                Button(
                                    onClick = { storyPage++ },
                                    colors = ButtonDefaults.buttonColors(containerColor = OceanTeal)
                                ) {
                                    Text("Next", fontSize = 11.sp, color = Color.White)
                                }
                            } else {
                                Button(
                                    onClick = {
                                        storyCompleted = true
                                        viewModel.awardCompletion("stories", story.id, 3)
                                        viewModel.speakText("Amazing! You read the whole book!")
                                        activeStory = null
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = ReefCoral)
                                ) {
                                    Text("Finish!", fontSize = 11.sp, color = Color.White)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SongsPanel(viewModel: DhivehiViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        DhivehiContent.songs.forEach { song ->
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("🎵", fontSize = 20.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = song.title,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = MidnightDeepBlue
                            )
                        }
                        IconButton(onClick = { viewModel.speakText("Singing along to ${song.title}") }) {
                            Icon(Icons.Filled.PlayArrow, contentDescription = "Sing", tint = OceanTeal)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = song.lyricsDhivehi,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = OceanTeal,
                        textAlign = TextAlign.Center,
                        lineHeight = 24.sp,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = song.lyricsEnglish,
                        fontSize = 12.sp,
                        color = Color.DarkGray,
                        textAlign = TextAlign.Center,
                        lineHeight = 16.sp,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Rhythm: " + song.rhythmDescription,
                        fontSize = 10.sp,
                        color = Color.LightGray,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
fun AiStoryGeneratorPanel(viewModel: DhivehiViewModel) {
    var themeInput by remember { mutableStateOf("Koamas the dolphin looking for turtle eggs") }
    val isGenerating by viewModel.isGeneratingStory.collectAsState()
    val generatedStory by viewModel.aiStoryResult.collectAsState()

    var selectedAnswer by remember { mutableStateOf<String?>(null) }
    var answerChecked by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Form trigger
        Card(
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.Star, contentDescription = null, tint = SandyGold)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "AI Custom Story Generator",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MidnightDeepBlue
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Type any characters (e.g., dolphin pearl hunt, coconut trees conversation) and let our AI coach write a lovely kids story!",
                    fontSize = 11.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = themeInput,
                    onValueChange = { themeInput = it },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("ai_story_input"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = OceanTeal,
                        unfocusedBorderColor = Color.LightGray
                    )
                )

                Spacer(modifier = Modifier.height(14.dp))

                if (isGenerating) {
                    CircularProgressIndicator(color = ReefCoral)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("AI is writing beautiful Dhivehi script...", fontSize = 11.sp, color = ReefCoral)
                } else {
                    Button(
                        onClick = {
                            selectedAnswer = null
                            answerChecked = false
                            viewModel.searchAndGenerateStory(themeInput)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = ReefCoral),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("ai_story_submit_btn")
                    ) {
                        Text("Create AI Story ✨", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Show Generated output
        generatedStory?.let { story ->
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Text(
                        text = "📖 " + story.title,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = OceanTeal
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = story.contentDhivehi,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = MidnightDeepBlue,
                        lineHeight = 28.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = story.contentEnglish,
                        fontSize = 13.sp,
                        color = Color.DarkGray,
                        lineHeight = 17.sp
                    )

                    Divider(modifier = Modifier.padding(vertical = 16.dp))

                    Text(
                        text = "Check Your Understanding / އިތުރު ސުވާލު:",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = ReefCoral
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = story.question,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MidnightDeepBlue
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Quiz Options
                    listOf(
                        "A" to story.optionA,
                        "B" to story.optionB,
                        "C" to story.optionC
                    ).forEach { (optCode, optText) ->
                        val isSelected = selectedAnswer == optCode
                        val isCorrect = optCode == story.correctAnswer

                        val bg = when {
                            answerChecked && isCorrect -> PalmLeafGreen.copy(alpha = 0.15f)
                            answerChecked && isSelected && !isCorrect -> ReefCoral.copy(alpha = 0.15f)
                            isSelected -> OceanTeal.copy(alpha = 0.15f)
                            else -> Color(0xFFF1F5F9)
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(bg)
                                .clickable(enabled = !answerChecked) { selectedAnswer = optCode }
                                .padding(12.dp)
                                .testTag("ai_story_option_$optCode"),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "$optCode. $optText",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = MidnightDeepBlue,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    if (selectedAnswer != null && !answerChecked) {
                        Button(
                            onClick = {
                                answerChecked = true
                                val correct = selectedAnswer == story.correctAnswer
                                if (correct) {
                                    viewModel.awardCompletion("stories", "ai_${story.title}", 5)
                                    viewModel.speakText("Whoopee! Correct answer!")
                                } else {
                                    viewModel.speakText("Oh! Almost. Try again next time!")
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = ReefCoral)
                        ) {
                            Text("Check Option Answer")
                        }
                    } else if (answerChecked) {
                        Text(
                            text = if (selectedAnswer == story.correctAnswer) "🎉 Hooray! You earned custom stars!" else "Keep practicing tropical reading!",
                            fontWeight = FontWeight.Bold,
                            color = if (selectedAnswer == story.correctAnswer) PalmLeafGreen else ReefCoral,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
    }
}
