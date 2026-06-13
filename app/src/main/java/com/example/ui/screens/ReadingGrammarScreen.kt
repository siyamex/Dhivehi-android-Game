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
import androidx.compose.material.icons.filled.Info
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
import com.example.data.GrammarLesson
import com.example.ui.theme.*
import com.example.ui.viewmodel.DhivehiViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReadingGrammarScreen(viewModel: DhivehiViewModel) {
    var selectedLessonIdx by remember { mutableStateOf(0) }

    var userSelectedAnswer by remember { mutableStateOf<Int?>(null) }
    var quizSubmitted by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Grammar Cruiser / ގްރެމަރ") },
                navigationIcon = {
                    IconButton(
                        onClick = { viewModel.setScreen("dashboard") },
                        modifier = Modifier.testTag("grammar_back_button")
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
            // Lesson selection bar supporting 3 static grammar sections + 1 dynamic AI Coach stop
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .background(Color.White, RoundedCornerShape(16.dp))
                    .padding(6.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val totalTabs = 4
                repeat(totalTabs) { idx ->
                    val isSelected = selectedLessonIdx == idx
                    val tabName = when (idx) {
                        0 -> "Nouns"
                        1 -> "Verbs"
                        2 -> "Adjectives"
                        else -> "Coach 💬"
                    }
                    Button(
                        onClick = {
                            selectedLessonIdx = idx
                            userSelectedAnswer = null
                            quizSubmitted = false
                            if (idx < 3) {
                                viewModel.speakText("Selected " + DhivehiContent.grammarLessons[idx].title)
                            } else {
                                viewModel.speakText("Coaching active! Let's practice speaking Maldivian sea sentences together!")
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isSelected) OceanTeal else Color.Transparent,
                            contentColor = if (isSelected) Color.White else Color.Gray
                        ),
                        modifier = Modifier
                            .weight(1.1f)
                            .height(44.dp)
                            .testTag("grammar_tab_$idx"),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(horizontal = 4.dp)
                    ) {
                        Text(
                            text = tabName,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Scrollable Lesson Body containing rules & live Quiz questions, or AI Speaking Coach
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (selectedLessonIdx < 3) {
                    val currentLesson = DhivehiContent.grammarLessons[selectedLessonIdx]
                    // Rule Card
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Filled.Info, contentDescription = null, tint = OceanTeal)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = currentLesson.title,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MidnightDeepBlue
                                )
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = currentLesson.explanationDhivehi,
                                fontSize = 20.sp,
                                color = OceanTeal,
                                fontWeight = FontWeight.Bold,
                                lineHeight = 24.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = currentLesson.explanationEnglish,
                                fontSize = 13.sp,
                                color = Color.DarkGray,
                                lineHeight = 17.sp
                            )

                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Examples / މިސާލުތައް:",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = ReefCoral
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            currentLesson.examples.forEach { (dh, eg) ->
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                        .background(LagoonSkyLight, RoundedCornerShape(12.dp))
                                        .padding(10.dp)
                                ) {
                                    Text(
                                        text = dh,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MidnightDeepBlue
                                    )
                                    Text(
                                        text = eg,
                                        fontSize = 11.sp,
                                        color = Color.Gray
                                    )
                                }
                            }
                        }
                    }

                    // Interactive Multiple Choice Quiz Box
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(24.dp),
                        modifier = Modifier.padding(bottom = 16.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "🌴 Island Grammar Quiz 🌴",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = PalmLeafGreen
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = currentLesson.quizQuestion,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = MidnightDeepBlue,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(16.dp))

                            // Render options
                            currentLesson.quizOptions.forEachIndexed { index, option ->
                                val isSelected = userSelectedAnswer == index
                                val correctIndex = currentLesson.quizAnswerIndex
                                val optionBg = when {
                                    quizSubmitted && index == correctIndex -> PalmLeafGreen.copy(alpha = 0.15f)
                                    quizSubmitted && isSelected && index != correctIndex -> ReefCoral.copy(alpha = 0.15f)
                                    isSelected -> OceanTeal.copy(alpha = 0.15f)
                                    else -> Color(0xFFF1F5F9)
                                }
                                val optionBorder = when {
                                    quizSubmitted && index == correctIndex -> PalmLeafGreen
                                    quizSubmitted && isSelected && index != correctIndex -> ReefCoral
                                    isSelected -> OceanTeal
                                    else -> Color.Transparent
                                }

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 6.dp)
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(optionBg)
                                        .clickable(enabled = !quizSubmitted) { userSelectedAnswer = index }
                                        .padding(14.dp)
                                        .testTag("quiz_option_$index"),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = when (index) {
                                            0 -> "A. "
                                            1 -> "B. "
                                            2 -> "C. "
                                            else -> ""
                                        } + option,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MidnightDeepBlue,
                                        modifier = Modifier.weight(1f)
                                    )
                                    if (quizSubmitted && index == correctIndex) {
                                        Icon(Icons.Filled.CheckCircle, contentDescription = "Correct", tint = PalmLeafGreen)
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            if (userSelectedAnswer != null && !quizSubmitted) {
                                Button(
                                    onClick = {
                                        quizSubmitted = true
                                        val isCorrect = userSelectedAnswer == currentLesson.quizAnswerIndex
                                        if (isCorrect) {
                                            viewModel.awardCompletion("grammar", currentLesson.id, 3)
                                            viewModel.speakText("Correct answer! You have earned three stars!")
                                        } else {
                                            viewModel.speakText("Aww! Almost. Let's try once more!")
                                        }
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(48.dp)
                                        .testTag("submit_quiz_button"),
                                    colors = ButtonDefaults.buttonColors(containerColor = ReefCoral)
                                ) {
                                    Text("Check Answer! / ޖަވާބު ޔަގީންކުރަމާ", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                }
                            } else if (quizSubmitted) {
                                val isCorrect = userSelectedAnswer == currentLesson.quizAnswerIndex
                                Text(
                                    text = if (isCorrect) "🎉 Correct! You earned stars!" else "⛵ Try another lesson stop above to learn!",
                                    color = if (isCorrect) PalmLeafGreen else ReefCoral,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                } else {
                    // Render fully functional Dynamic AI Pronunciation Coach Sandbox!
                    AiSpeakingCoachLounge(viewModel)
                }
            }
        }
    }
}

@Composable
fun AiSpeakingCoachLounge(viewModel: DhivehiViewModel) {
    val evaluationResult by viewModel.aiEvaluationResult.collectAsState()
    val isEvaluatingReading by viewModel.isEvaluatingReading.collectAsState()
    
    val practiceSentences = listOf(
        "މަސް ކަނޑުގައި އުޅެއެވެ." to "Fish lives in the beautiful Maldives ocean.",
        "ވެލާ ދޮންވެލިގަނޑު މަތީގައި އޮވެއެވެ." to "The turtle walks along the soft sandy beach.",
        "ދޯނިން ކަނޑަށް ފުރަނީއެވެ." to "The traditional boat is setting sail out to sea.",
        "ދޮންވެލިގަނޑު ސާފުކޮށް ބާއްވަން ޖެހެއެވެ." to "We must always keep our sandy island beach clean."
    )
    
    var selectedSentenceIdx by remember { mutableStateOf(0) }
    val (targetDhivehi, translation) = practiceSentences[selectedSentenceIdx]
    
    var kidTypedText by remember { mutableStateOf("") }
    var evaluationTriggered by remember { mutableStateOf(false) }
    
    LaunchedEffect(selectedSentenceIdx) {
        kidTypedText = ""
        evaluationTriggered = false
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Coach Introduction Card
        Card(
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("🐬 Koamas Dolphin AI Coaching 🐬", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = OceanTeal)
                Text(
                    text = "Hello friend! I am Koamas, your mascot coach. Select a beautiful Maldives sentence below, write or type your practice repetition, and ask me to evaluate it directly!",
                    fontSize = 11.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    lineHeight = 16.sp
                )
                
                // Horizontal selection row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    practiceSentences.forEachIndexed { sIdx, Pair ->
                        val isSel = selectedSentenceIdx == sIdx
                        Button(
                            onClick = { selectedSentenceIdx = sIdx },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isSel) ReefCoral else Color(0xFFF1F5F9),
                                contentColor = if (isSel) Color.White else Color.Black
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(38.dp)
                        ) {
                            Text("Line ${sIdx + 1}", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Active Sentence Target
        Card(
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = targetDhivehi,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = OceanTeal,
                    textAlign = TextAlign.Center
                )
                
                Text(
                    text = "Translation: $translation",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
                
                Button(
                    onClick = {
                        viewModel.speakText("Repeat after me: $targetDhivehi")
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = LagoonAqua),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.height(44.dp).testTag("coach_read_button")
                ) {
                    Icon(Icons.Filled.PlayArrow, contentDescription = "Play sound")
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Read Out Loud / ވާހަކަ ދައްކަވާ", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Writing Input Card
        Card(
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Practice copying or matching below in Dhivehi:",
                    fontSize = 11.sp,
                    color = Color.DarkGray,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.Start)
                )
                
                OutlinedTextField(
                    value = kidTypedText,
                    onValueChange = { kidTypedText = it },
                    placeholder = { Text("Type characters...") },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("coach_input_field"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = OceanTeal,
                        unfocusedBorderColor = Color.LightGray
                    )
                )
                
                if (isEvaluatingReading) {
                    CircularProgressIndicator(color = ReefCoral)
                    Text("Koamas dolphin is evaluating script accuracy with Gemini...", fontSize = 11.sp, color = ReefCoral)
                } else {
                    Button(
                        onClick = {
                            if (kidTypedText.trim().isNotEmpty()) {
                                evaluationTriggered = true
                                viewModel.evaluatePronunciation(targetDhivehi, kidTypedText)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = ReefCoral),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("coach_submit_btn"),
                        enabled = kidTypedText.trim().isNotEmpty()
                    ) {
                        Text("Get Mascot Coaching Feedback ✨", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Dynamic coaching feedback display!
        if (evaluationTriggered) {
            evaluationResult?.let { eval ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp).testTag("coach_report_card")
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text("🌴 Wave Evaluation Report 🌴", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = PalmLeafGreen)
                        
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = "🎯 Accuracy:", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = MidnightDeepBlue)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "${eval.accuracyScore}%", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, color = OceanTeal)
                        }
                        
                        Divider(color = Color.LightGray.copy(alpha = 0.3f))
                        
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(LagoonSkyLight, RoundedCornerShape(14.dp))
                                .padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "Dhivehi Praise / ތައުރީފު:",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = ReefCoral
                            )
                            Text(
                                text = eval.feedbackDhivehi,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = MidnightDeepBlue
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "English Coach review:",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Gray
                            )
                            Text(
                                text = eval.feedbackEnglish,
                                fontSize = 12.sp,
                                color = Color.DarkGray,
                                lineHeight = 16.sp
                            )
                        }
                        
                        if (eval.suggestions.isNotEmpty()) {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = "Coach Tips for Success / އިރުޝާދު:",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Gray
                                )
                                eval.suggestions.forEach { suggestion ->
                                    Row(
                                        verticalAlignment = Alignment.Top,
                                        modifier = Modifier.padding(vertical = 2.dp)
                                    ) {
                                        Text("☀️", fontSize = 11.sp, modifier = Modifier.padding(top = 1.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(suggestion, fontSize = 11.sp, color = Color.DarkGray)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
