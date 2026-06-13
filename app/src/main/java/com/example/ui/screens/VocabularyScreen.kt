package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import com.example.data.VocabItem
import com.example.ui.theme.*
import com.example.ui.viewmodel.DhivehiViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VocabularyScreen(viewModel: DhivehiViewModel) {
    val selectedCategory by viewModel.selectedVocabCategory.collectAsState()
    val availableCategories = listOf("Animals", "Fruits", "Sea Creatures")
    val filteredVocab = DhivehiContent.vocabulary.filter { it.category == selectedCategory }

    var gameModeActive by remember { mutableStateOf(false) }
    var currentWordIndex by remember { mutableStateOf(0) }

    LaunchedEffect(selectedCategory, gameModeActive) {
        currentWordIndex = 0
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Vocabulary Beach / ބަސްކޮށާރު") },
                navigationIcon = {
                    IconButton(
                        onClick = { viewModel.setScreen("dashboard") },
                        modifier = Modifier.testTag("vocab_back_button")
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
            // Mode Select Switch Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .background(Color.White, RoundedCornerShape(16.dp))
                    .padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { gameModeActive = false },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (!gameModeActive) OceanTeal else Color.Transparent,
                        contentColor = if (!gameModeActive) Color.White else Color.Gray
                    ),
                    modifier = Modifier.weight(1f).height(40.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Flashcards 📚", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = {
                        gameModeActive = true
                        viewModel.speakText("Spelling Quest Mode activated! Let's arrange letters in correct order.")
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (gameModeActive) OceanTeal else Color.Transparent,
                        contentColor = if (gameModeActive) Color.White else Color.Gray
                    ),
                    modifier = Modifier.weight(1f).height(40.dp).testTag("vocab_toggle_game_mode"),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Spelling Quest 🎮", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }

            // Category Slider Filter Row
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp, start = 16.dp, end = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(availableCategories) { cat ->
                    val isSelected = selectedCategory == cat
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(if (isSelected) OceanTeal else Color.White)
                            .clickable { viewModel.selectVocabCategory(cat) }
                            .padding(horizontal = 18.dp, vertical = 10.dp)
                            .testTag("vocab_category_$cat"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = cat,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) Color.White else Color.Black
                        )
                    }
                }
            }

            if (!gameModeActive) {
                // Word cards grid
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                        .padding(horizontal = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(filteredVocab) { word ->
                        VocabCard(word = word, viewModel = viewModel)
                    }
                }

                // Global Submit Button at base to record XP completion
                Button(
                    onClick = {
                        viewModel.awardCompletion("vocabulary", "category_$selectedCategory", 3)
                        viewModel.speakText("Incredible! You learned the $selectedCategory vocabulary list!")
                        viewModel.setScreen("dashboard")
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ReefCoral),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .height(52.dp)
                        .testTag("complete_vocab_button")
                ) {
                    Icon(Icons.Filled.Star, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Complete Category! / ނިމުނީ!", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            } else {
                // Interactive Spelling Quest Game Mode View
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentAlignment = Alignment.TopCenter
                ) {
                    SpellingQuestGame(
                        vocabList = filteredVocab,
                        currentIndex = currentWordIndex,
                        onNext = {
                            currentWordIndex++
                        },
                        onFinish = { bonusXp ->
                            viewModel.awardCompletion("vocabulary_quest", "category_spelling_$selectedCategory", 5)
                            viewModel.speakText("Unbelievable! You completed the entire spelling challenge list!")
                            viewModel.setScreen("dashboard")
                        },
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}

@Composable
fun SpellingQuestGame(
    vocabList: List<VocabItem>,
    currentIndex: Int,
    onNext: () -> Unit,
    onFinish: (xpEarned: Int) -> Unit,
    viewModel: DhivehiViewModel
) {
    val activeWord = vocabList.getOrNull(currentIndex) ?: return
    
    val fullSpelling = activeWord.dhivehi
    val rawChars = remember(activeWord) { fullSpelling.map { it.toString() } }
    val scrambledChars = remember(activeWord) { rawChars.shuffled() }
    
    val selectedChars = remember { mutableStateListOf<Int>() } 
    var isCorrect by remember { mutableStateOf<Boolean?>(null) }
    
    LaunchedEffect(activeWord) {
        selectedChars.clear()
        isCorrect = null
    }
    
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(28.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .testTag("spelling_quest_card"),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Spelling Quest ${currentIndex + 1} of ${vocabList.size}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray
                )
                Text(
                    text = "🏆 Bonus Stars!",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = SandyGold
                )
            }
            
            val wordEmoji = when (activeWord.translation) {
                "Cat" -> "🐈"
                "Dog" -> "🐕"
                "Bird" -> "🦜"
                "Butterfly" -> "🦋"
                "Banana" -> "🍌"
                "Lime" -> "🍋"
                "Apple" -> "🍎"
                "Mango" -> "🥭"
                "Turtle" -> "🐢"
                "Tuna" -> "🐟"
                "Ocean Fish" -> "🐠"
                "Dolphin" -> "🐬"
                else -> "🐟"
            }
            
            Text(wordEmoji, fontSize = 72.sp, modifier = Modifier.testTag("spelling_emoji"))
            
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = activeWord.translation,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MidnightDeepBlue
                )
                Text(
                    text = "Reads as: [${activeWord.phonics}]",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Medium
                )
            }
            
            Divider(color = Color.LightGray.copy(alpha = 0.3f))
            
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                val sortedConstructed = selectedChars.map { scrambledChars[it] }
                
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    if (sortedConstructed.isEmpty()) {
                        Text(
                            text = "Tap letters below to spell RTL",
                            fontSize = 12.sp,
                            color = Color.LightGray,
                            modifier = Modifier.padding(vertical = 12.dp)
                        )
                    } else {
                        sortedConstructed.forEachIndexed { i, letter ->
                            Box(
                                modifier = Modifier
                                    .size(46.dp)
                                    .background(OceanTeal.copy(alpha = 0.12f), RoundedCornerShape(12.dp))
                                    .border(1.5.dp, OceanTeal, RoundedCornerShape(12.dp))
                                    .clickable {
                                        selectedChars.removeAt(i)
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = letter,
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = OceanTeal
                                )
                            }
                        }
                    }
                }
            }
            
            Text(
                text = "Scrambled Letters Bank:",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Gray,
                modifier = Modifier.align(Alignment.Start)
            )
            
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    scrambledChars.forEachIndexed { idx, char ->
                        val isUsed = selectedChars.contains(idx)
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isUsed) Color(0xFFF1F5F9) else OceanTeal)
                                .clickable(enabled = !isUsed) {
                                    selectedChars.add(idx)
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = char,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isUsed) Color.LightGray else Color.White
                            )
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            isCorrect?.let { correct ->
                if (correct) {
                    Text(
                        text = "🎉 ވަރަށް ރަނގަޅު! Spelled Correctly!",
                        color = PalmLeafGreen,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 15.sp,
                        textAlign = TextAlign.Center
                    )
                } else {
                    Text(
                        text = "❌ Almost! Dhivehi letters write RTL (right-to-left)!",
                        color = ReefCoral,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = { selectedChars.clear(); isCorrect = null },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Gray)
                ) {
                    Text("Clear")
                }
                
                Button(
                    onClick = {
                        val constructedString = selectedChars.map { scrambledChars[it] }.joinToString("")
                        if (constructedString == fullSpelling) {
                            isCorrect = true
                            viewModel.speakText("Superb spelling! $fullSpelling is indeed ${activeWord.translation}!")
                        } else {
                            isCorrect = false
                            viewModel.speakText("Oops! Try again.")
                        }
                    },
                    modifier = Modifier.weight(1.5f),
                    colors = ButtonDefaults.buttonColors(containerColor = ReefCoral),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text("Verify 🔍", fontWeight = FontWeight.Bold)
                }
            }
            
            if (isCorrect == true) {
                Button(
                    onClick = {
                        if (currentIndex + 1 < vocabList.size) {
                            onNext()
                        } else {
                            onFinish(20)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = PalmLeafGreen),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text(
                        text = if (currentIndex + 1 < vocabList.size) "Next Spelling Word ➡️" else "Finish Quest & Collect XP! 🏆",
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
fun VocabCard(word: VocabItem, viewModel: DhivehiViewModel) {
    var isFlipped by remember { mutableStateOf(false) }

    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                isFlipped = !isFlipped
                viewModel.speakText(word.translation + " is: " + word.dhivehi)
            }
            .testTag("vocab_card_${word.translation}")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Illustrated dynamic emoji match
            val wordEmoji = when (word.translation) {
                "Cat" -> "🐈"
                "Dog" -> "🐕"
                "Bird" -> "🦜"
                "Butterfly" -> "🦋"
                "Banana" -> "🍌"
                "Lime" -> "🍋"
                "Apple" -> "🍎"
                "Mango" -> "🥭"
                "Turtle" -> "🐢"
                "Tuna" -> "🐟"
                "Ocean Fish" -> "🐠"
                "Dolphin" -> "🐬"
                else -> "🐟"
            }

            Text(text = wordEmoji, fontSize = 48.sp, modifier = Modifier.padding(bottom = 8.dp))

            if (!isFlipped) {
                // Front Side: Beautiful RTL Dhivehi script block
                Text(
                    text = word.dhivehi,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = OceanTeal,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Tap to translate",
                    fontSize = 10.sp,
                    color = Color.LightGray
                )
            } else {
                // Back Side: Translation detail
                Text(
                    text = word.translation,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = ReefCoral,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "[${word.phonics}]",
                    fontSize = 11.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = word.sentenceDhivehi,
                    fontSize = 11.sp,
                    color = MidnightDeepBlue,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(LagoonSkyLight, CircleShape)
                    .padding(vertical = 4.dp, horizontal = 8.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.PlayArrow,
                    contentDescription = "Speak",
                    tint = OceanTeal,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("Listen Sound", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = OceanTeal)
            }
        }
    }
}
