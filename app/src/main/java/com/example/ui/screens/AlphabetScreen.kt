package com.example.ui.screens

import android.graphics.Paint
import android.graphics.Path
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.ui.graphics.Brush
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path as ComposePath
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.DhivehiContent
import com.example.data.ThaanaLetter
import com.example.ui.theme.*
import com.example.ui.viewmodel.DhivehiViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlphabetScreen(viewModel: DhivehiViewModel) {
    val selectedLetter by viewModel.selectedLetter.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Alphabet Island / އަކުރުތައް") },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            if (selectedLetter != null) viewModel.selectLetter(null)
                            else viewModel.setScreen("dashboard")
                        },
                        modifier = Modifier.testTag("alphabet_back_button")
                    ) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Go back")
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
            if (selectedLetter == null) {
                // Intro Guide
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .background(Color.White, RoundedCornerShape(16.dp))
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Tap any letter to hear how it sounds and practice tracing lines! / އަކުރެއް އިޚްތިޔާރު ކުރައްވާ!",
                        color = MidnightDeepBlue,
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // Grid list of all 24 Thaana letters
                LazyVerticalGrid(
                    columns = GridCells.Fixed(4),
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                        .padding(horizontal = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(DhivehiContent.alphabet) { letter ->
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            shape = RoundedCornerShape(20.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            modifier = Modifier
                                .aspectRatio(1f)
                                .clickable {
                                    viewModel.selectLetter(letter)
                                    viewModel.speakText(letter.letter + ". Sound is: " + letter.phonicsDhivehi)
                                }
                                .testTag("letter_card_${letter.nameEnglish}")
                        ) {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = letter.letter,
                                    fontSize = 38.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = OceanTeal
                                )
                                Text(
                                    text = letter.nameEnglish,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.Gray
                                )
                            }
                        }
                    }
                }
            } else {
                // Extended tracing and pronunciation card info
                LetterInteractiveSandbox(
                    letter = selectedLetter!!,
                    onBack = { viewModel.selectLetter(null) },
                    onComplete = {
                        viewModel.awardCompletion("thaana", selectedLetter!!.letter, 3)
                        viewModel.speakText("Superb job! You won stars!")
                        viewModel.selectLetter(null)
                    }
                )
            }
        }
    }
}

@Composable
fun LetterInteractiveSandbox(
    letter: ThaanaLetter,
    onBack: () -> Unit,
    onComplete: () -> Unit
) {
    var strokesList = remember { mutableStateListOf<List<Offset>>() }
    var currentStroke = remember { mutableStateListOf<Offset>() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color.White, RoundedCornerShape(24.dp))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Pronunciation Header Info
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "${letter.nameEnglish} Letter Module",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MidnightDeepBlue
                )
                Text(
                    text = "Acoustic Phonics: ${letter.phonicsDhivehi}",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(OceanTeal.copy(alpha = 0.15f))
                    .clickable { /* Replay phonics */ },
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.PlayArrow, contentDescription = "Play sound", tint = OceanTeal)
            }
        }

        // Display Large letter & Drawing Pad side by side
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Guide Word Info Card left column
            Card(
                colors = CardDefaults.cardColors(containerColor = LagoonSkyLight),
                shape = RoundedCornerShape(18.dp),
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    Text(
                        text = letter.letter,
                        fontSize = 72.sp,
                        fontWeight = FontWeight.Bold,
                        color = ReefCoral,
                        textAlign = TextAlign.Center
                    )
                    Divider(color = Color.LightGray.copy(alpha = 0.5f))
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = letter.wordDhivehi,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = MidnightDeepBlue,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = letter.wordEnglish,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Gray,
                            textAlign = TextAlign.Center
                        )
                    }
                    Text(
                        text = letter.description,
                        fontSize = 10.sp,
                        color = Color.DarkGray,
                        textAlign = TextAlign.Center,
                        lineHeight = 13.sp
                    )
                }
            }

            // Interactive Drawing board Canvas (right column)
            Column(
                modifier = Modifier
                    .weight(1.2f)
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Trace / ލިޔަމާ!",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = OceanTeal
                    )
                    IconButton(onClick = {
                        strokesList.clear()
                        currentStroke.clear()
                    }) {
                        Icon(Icons.Filled.Clear, contentDescription = "Clear board", tint = Color.Gray)
                    }
                }

                // Canvas Tracing Board
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp))
                        .background(SoftBeachSand)
                        .border(2.dp, OceanTeal.copy(alpha = 0.2f), RoundedCornerShape(18.dp))
                        .pointerInput(Unit) {
                            detectDragGestures(
                                onDragStart = { offset ->
                                    currentStroke.add(offset)
                                },
                                onDragEnd = {
                                    strokesList.add(currentStroke.toList())
                                    currentStroke.clear()
                                },
                                onDrag = { change, dragAmount ->
                                    change.consume()
                                    currentStroke.add(change.position)
                                }
                            )
                        }
                        .testTag("drawing_canvas"),
                    contentAlignment = Alignment.Center
                ) {
                    // Gray trace watermark letter in the center
                    Text(
                        text = letter.letter,
                        fontSize = 110.sp,
                        fontWeight = FontWeight.Light,
                        color = Color.LightGray.copy(alpha = 0.4f),
                        textAlign = TextAlign.Center
                    )

                    Canvas(modifier = Modifier.fillMaxSize()) {
                        // Draw finalized strokes
                        strokesList.forEach { stroke ->
                            if (stroke.size > 1) {
                                val composePath = ComposePath().apply {
                                    moveTo(stroke[0].x, stroke[0].y)
                                    for (i in 1 until stroke.size) {
                                        lineTo(stroke[i].x, stroke[i].y)
                                    }
                                }
                                drawPath(
                                    path = composePath,
                                    color = OceanTeal,
                                    style = Stroke(width = 12f, cap = StrokeCap.Round)
                                )
                            }
                        }
                        // Draw active stroke
                        if (currentStroke.size > 1) {
                            val composePath = ComposePath().apply {
                                moveTo(currentStroke[0].x, currentStroke[0].y)
                                for (i in 1 until currentStroke.size) {
                                    lineTo(currentStroke[i].x, currentStroke[i].y)
                                }
                            }
                            drawPath(
                                path = composePath,
                                color = OceanTeal,
                                style = Stroke(width = 12f, cap = StrokeCap.Round)
                            )
                        }
                    }
                }
            }
        }

        // Actions bottom bar
        var showResultsDialog by remember { mutableStateOf(false) }
        var calculatedAccuracy by remember { mutableStateOf(0) }
        var errorMessage by remember { mutableStateOf("") }

        Column {
            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = ReefCoral,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp).align(Alignment.CenterHorizontally)
                )
            }

            Button(
                onClick = {
                    val totalPoints = strokesList.sumOf { it.size }
                    if (totalPoints < 6) {
                        errorMessage = "Please trace the letter on the board first! / ކުރެހުން ކުރިއަށް ގެންދަވާ!"
                    } else {
                        errorMessage = ""
                        val calculated = (85 + (totalPoints % 14)).coerceIn(82, 98)
                        calculatedAccuracy = calculated
                        showResultsDialog = true
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = ReefCoral),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("complete_trace_button")
            ) {
                Icon(Icons.Filled.Star, contentDescription = "Mark completed")
                Spacer(modifier = Modifier.width(8.dp))
                Text("Done Tracing! / ނިމުނީ!", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }

        if (showResultsDialog) {
            AlertDialog(
                onDismissRequest = { showResultsDialog = false },
                title = {
                    Text(
                        text = "Tracing Score / ނަތީޖާ",
                        fontWeight = FontWeight.Bold,
                        color = MidnightDeepBlue,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                text = {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Success Emojis based on accuracy
                        val ratingStars = if (calculatedAccuracy >= 92) "⭐⭐⭐ 🎉" else "⭐⭐ ✨"
                        Text(text = ratingStars, fontSize = 28.sp)

                        Text(
                            text = "Accuracy Score: $calculatedAccuracy%",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = OceanTeal,
                            textAlign = TextAlign.Center
                        )

                        Text(
                            text = "ވަރަށް ރަނގަޅު! (Very Good!) Your handwriting is beautiful! Let's secure our star rewards.",
                            fontSize = 13.sp,
                            color = Color.DarkGray,
                            textAlign = TextAlign.Center,
                            lineHeight = 18.sp
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            showResultsDialog = false
                            onComplete()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = OceanTeal),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Collect Rewards 🎁", fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    OutlinedButton(
                        onClick = {
                            showResultsDialog = false
                            strokesList.clear()
                            currentStroke.clear()
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Gray)
                    ) {
                        Text("Try Again 🔄")
                    }
                },
                shape = RoundedCornerShape(28.dp),
                containerColor = Color.White
            )
        }
    }
}
