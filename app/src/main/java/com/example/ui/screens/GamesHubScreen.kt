package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
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
import com.example.ui.theme.*
import com.example.ui.viewmodel.DhivehiViewModel
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GamesHubScreen(viewModel: DhivehiViewModel) {
    var activeGame by remember { mutableStateOf<String?>(null) } // null, "balloon", "memory"

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Game Arcade / ކުޅިވަރު") },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            if (activeGame != null) activeGame = null
                            else viewModel.setScreen("dashboard")
                        },
                        modifier = Modifier.testTag("games_back_button")
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
            if (activeGame == null) {
                // Game selection list
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Choose a Game Stop to play & win stars!",
                        fontWeight = FontWeight.Bold,
                        color = MidnightDeepBlue,
                        fontSize = 15.sp
                    )

                    // Card 1: Balloon Pop Spelling
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(24.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                activeGame = "balloon"
                                viewModel.speakText("Balloon Pop! Lets find and pop the correct letters")
                            }
                            .testTag("game_balloon_btn")
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "🎈", fontSize = 48.sp)
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text("Balloon Pop Spelling", fontWeight = FontWeight.Bold, color = MidnightDeepBlue, fontSize = 16.sp)
                                Text("Listen to target sounds and pop correct floating letters!", fontSize = 11.sp, color = Color.Gray)
                            }
                        }
                    }

                    // Card 2: Memory card match
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(24.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                activeGame = "memory"
                                viewModel.speakText("Memory Games! Flip and match the Maldives buddies")
                            }
                            .testTag("game_memory_btn")
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "🃏", fontSize = 48.sp)
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text("Mascot Memory Match", fontWeight = FontWeight.Bold, color = MidnightDeepBlue, fontSize = 16.sp)
                                Text("Flip cards over to find matching buddy mascots!", fontSize = 11.sp, color = Color.Gray)
                            }
                        }
                    }
                }
            } else {
                when (activeGame) {
                    "balloon" -> BalloonPopGame(viewModel) {
                        viewModel.awardCompletion("games", "balloon_pop_spelling", 5)
                        viewModel.speakText("Incredible popping action! You won five stars!")
                        activeGame = null
                    }
                    "memory" -> MemoryMatchGame(viewModel) {
                        viewModel.awardCompletion("games", "memory_matching", 5)
                        viewModel.speakText("Super memory brain! Activity completed!")
                        activeGame = null
                    }
                }
            }
        }
    }
}

@Composable
fun BalloonPopGame(viewModel: DhivehiViewModel, onFinish: () -> Unit) {
    var score by remember { mutableStateOf(0) }
    var targetLetter by remember { mutableStateOf("ހ") }
    val alphabetSample = listOf("ހ", "ށ", "ނ", "ރ", "ބ", "ޅ")
    
    // Setup balloon values
    var balloonsList = remember { mutableStateListOf<Pair<String, String>>() } // value to colorHex

    fun resetRound() {
        targetLetter = alphabetSample.random()
        viewModel.speakText("Find and pop the letter: " + targetLetter)
        
        balloonsList.clear()
        val generated = mutableListOf<String>()
        generated.add(targetLetter)
        repeat(5) {
            generated.add(alphabetSample.random())
        }
        generated.shuffle()

        generated.forEach { item ->
            val color = listOf("#FF6F59", "#00A896", "#FBCF33", "#8338EC", "#FF007F").random()
            balloonsList.add(item to color)
        }
    }

    LaunchedEffect(Unit) {
        resetRound()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color.White, RoundedCornerShape(24.dp))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Score: $score / 5", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = MidnightDeepBlue)
            IconButton(onClick = { resetRound() }) {
                Icon(Icons.Filled.Refresh, contentDescription = "Reset round", tint = OceanTeal)
            }
        }

        // Target Board Bubble
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(LagoonSkyLight, RoundedCornerShape(16.dp))
                .padding(18.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("POP THIS LETTER / ހޯދާށެވެ:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = ReefCoral)
                Text(targetLetter, fontSize = 48.sp, fontWeight = FontWeight.Bold, color = MidnightDeepBlue)
            }
        }

        // Grid of floating Balloons
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            itemsIndexed(balloonsList) { idx, balloon ->
                val isCorrect = balloon.first == targetLetter
                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .clip(CircleShape)
                        .background(Color(android.graphics.Color.parseColor(balloon.second)))
                        .clickable {
                            if (isCorrect) {
                                score++
                                viewModel.speakText("Boop! Correct popping.")
                                if (score >= 5) {
                                    onFinish()
                                } else {
                                    resetRound()
                                }
                            } else {
                                viewModel.speakText("Oops! Try again.")
                            }
                        }
                        .testTag("balloon_idx_$idx"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = balloon.first, fontSize = 28.sp, color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }

        Text(
            "Quickly look, find the letter $targetLetter and pop!",
            fontSize = 11.sp,
            color = Color.LightGray
        )
    }
}

@Composable
fun MemoryMatchGame(viewModel: DhivehiViewModel, onFinish: () -> Unit) {
    val items = listOf("🐬", "🐢", "🦀", "🦜", "🐬", "🐢", "🦀", "🦜")
    val cards = remember { mutableStateListOf<MemoryCardItem>() }
    var firstSelectedIdx by remember { mutableStateOf<Int?>(null) }
    var isCheckingMatch by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    fun setupCards() {
        cards.clear()
        val shuffled = items.shuffled()
        shuffled.forEach { value ->
            cards.add(MemoryCardItem(value = value, isFlipped = false, isMatched = false))
        }
        firstSelectedIdx = null
        isCheckingMatch = false
    }

    LaunchedEffect(Unit) {
        setupCards()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color.White, RoundedCornerShape(24.dp))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Memory Match Up", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = MidnightDeepBlue)
            IconButton(onClick = { setupCards() }) {
                Icon(Icons.Filled.Refresh, contentDescription = "Restart", tint = OceanTeal)
            }
        }

        // cards grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            itemsIndexed(cards) { index, card ->
                val showAsset = card.isFlipped || card.isMatched
                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(16.dp))
                        .background(if (showAsset) SoftBeachSand else OceanTeal)
                        .border(
                            width = 2.dp,
                            color = if (showAsset) OceanTeal else Color.Transparent,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .clickable(enabled = !card.isMatched && !isCheckingMatch) {
                            if (index == firstSelectedIdx) return@clickable
                            
                            cards[index] = card.copy(isFlipped = true)
                            
                            if (firstSelectedIdx == null) {
                                firstSelectedIdx = index
                            } else {
                                val firstIdx = firstSelectedIdx!!
                                isCheckingMatch = true
                                viewModel.speakText("Flipped card!")
                                
                                // Check match
                                if (cards[firstIdx].value == cards[index].value) {
                                    // Matched
                                    cards[firstIdx] = cards[firstIdx].copy(isMatched = true)
                                    cards[index] = cards[index].copy(isMatched = true)
                                    firstSelectedIdx = null
                                    isCheckingMatch = false
                                    viewModel.speakText("Got a buddy Match!")
                                    
                                    // check win
                                    if (cards.all { it.isMatched }) {
                                        onFinish()
                                    }
                                } else {
                                    // No match: delay then flip back
                                    coroutineScope.launch {
                                        delay(1000)
                                        cards[firstIdx] = cards[firstIdx].copy(isFlipped = false)
                                        cards[index] = cards[index].copy(isFlipped = false)
                                        firstSelectedIdx = null
                                        isCheckingMatch = false
                                    }
                                }
                            }
                        }
                        .testTag("memory_card_$index"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (showAsset) card.value else "❓",
                        fontSize = 28.sp
                    )
                }
            }
        }

        Text(
            "Flip and find matching buddy cards!",
            fontSize = 11.sp,
            color = Color.LightGray
        )
    }
}

data class MemoryCardItem(
    val value: String,
    val isFlipped: Boolean,
    val isMatched: Boolean
)
