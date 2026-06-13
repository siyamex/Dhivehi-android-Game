package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
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
import com.example.data.DhivehiColor
import com.example.data.DhivehiContent
import com.example.data.DhivehiNumber
import com.example.data.DhivehiShape
import com.example.ui.theme.*
import com.example.ui.viewmodel.DhivehiViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NumbersColorsScreen(viewModel: DhivehiViewModel) {
    var activeTab by remember { mutableStateOf("Numbers") } // Numbers, Colors, Shapes
    val progressList by viewModel.lessonProgress.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Lagoon Hub / ހިސާބާއި ކުލަ") },
                navigationIcon = {
                    IconButton(
                        onClick = { viewModel.setScreen("dashboard") },
                        modifier = Modifier.testTag("numbers_back_button")
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
            // Segmented activeTab buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .background(Color.White, RoundedCornerShape(16.dp))
                    .padding(6.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("Numbers", "Colors", "Shapes").forEach { tab ->
                    val isSelected = activeTab == tab
                    Button(
                        onClick = { activeTab = tab },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isSelected) OceanTeal else Color.Transparent,
                            contentColor = if (isSelected) Color.White else Color.Gray
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .testTag("tab_${tab.lowercase()}"),
                        shape = RoundedCornerShape(12.dp),
                        elevation = null
                    ) {
                        Text(text = tab, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Central body
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .padding(horizontal = 16.dp)
            ) {
                when (activeTab) {
                    "Numbers" -> NumbersPanel(viewModel) {
                        viewModel.awardCompletion("numbers", "counting_1to10", 3)
                        viewModel.speakText("Way to go! You completed the Maldives math block!")
                    }
                    "Colors" -> ColorsPanel(viewModel) {
                        viewModel.awardCompletion("colors", "ocean_shades", 3)
                        viewModel.speakText("Spectacular hues! Color lesson completed.")
                    }
                    "Shapes" -> ShapesPanel(viewModel) {
                        viewModel.awardCompletion("shapes", "island_geometry", 3)
                        viewModel.speakText("Fantastic geometry! Shape lesson completed.")
                    }
                }
            }
        }
    }
}

@Composable
fun NumbersPanel(viewModel: DhivehiViewModel, onFinished: () -> Unit) {
    var selectedNum by remember { mutableStateOf<DhivehiNumber?>(DhivehiContent.numbers[0]) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Horizontally swipeable number tokens
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(DhivehiContent.numbers) { num ->
                val isSelected = selectedNum?.numericValue == num.numericValue
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(if (isSelected) ReefCoral else Color.White)
                        .border(
                            width = 2.dp,
                            color = if (isSelected) SandyGold else Color.Transparent,
                            shape = CircleShape
                        )
                        .clickable {
                            selectedNum = num
                            viewModel.speakText("${num.numeralEnglish}. In Dhivehi: ${num.numeralDhivehi}")
                        }
                        .testTag("number_bubble_${num.numericValue}"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${num.numericValue}",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isSelected) Color.White else MidnightDeepBlue
                    )
                }
            }
        }

        // Active Details Card
        selectedNum?.let { num ->
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "${num.numericValue}",
                        fontSize = 72.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = OceanTeal
                    )
                    Text(
                        text = num.numeralDhivehi,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = MidnightDeepBlue
                    )
                    Text(
                        text = num.numeralEnglish,
                        fontSize = 16.sp,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Counting visuals (using emojis matching real Maldives objects!)
                    val visualEmoji = when (num.numericValue) {
                        1 -> "🐠"
                        2 -> "⛵"
                        3 -> "🌴"
                        4 -> "🐡"
                        5 -> "🐚"
                        6 -> "🪶"
                        7 -> "🛶"
                        8 -> "⭐"
                        9 -> "🦋"
                        10 -> "🍋"
                        else -> "🐟"
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(LagoonSkyLight, RoundedCornerShape(16.dp))
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Row(
                                modifier = Modifier.padding(bottom = 8.dp),
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                repeat(num.numericValue) {
                                    Text(text = visualEmoji, fontSize = 28.sp)
                                }
                            }
                            Text(
                                text = "Count / ގުނަމާ: ${num.detailsDhivehi} (${num.detailsEnglish})",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = PalmLeafGreen
                            )
                        }
                    }
                }
            }
        }

        // Submit reward button
        Button(
            onClick = { onFinished() },
            colors = ButtonDefaults.buttonColors(containerColor = ReefCoral),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("complete_numbers_button")
        ) {
            Icon(Icons.Filled.Star, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Finished counting! / ނިމުނީ!", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun ColorsPanel(viewModel: DhivehiViewModel, onFinished: () -> Unit) {
    var selectedColor by remember { mutableStateOf<DhivehiColor?>(DhivehiContent.colors[0]) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Color items listing grid helper
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            DhivehiContent.colors.take(4).forEach { col ->
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(Color(android.graphics.Color.parseColor(col.colorHex)))
                        .border(
                            width = 2.dp,
                            color = if (selectedColor?.colorHex == col.colorHex) SandyGold else Color.LightGray.copy(alpha = 0.5f),
                            shape = CircleShape
                        )
                        .clickable {
                            selectedColor = col
                            viewModel.speakText("Color is: " + col.nameEnglish)
                        }
                        .testTag("color_bubble_${col.nameEnglish}")
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            DhivehiContent.colors.drop(4).forEach { col ->
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(Color(android.graphics.Color.parseColor(col.colorHex)))
                        .border(
                            width = 2.dp,
                            color = if (selectedColor?.colorHex == col.colorHex) SandyGold else Color.LightGray.copy(alpha = 0.5f),
                            shape = CircleShape
                        )
                        .clickable { selectedColor = col }
                )
            }
        }

        selectedColor?.let { col ->
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
                    Box(
                        modifier = Modifier
                            .size(90.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color(android.graphics.Color.parseColor(col.colorHex)))
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = col.nameDhivehi,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = MidnightDeepBlue
                    )
                    Text(
                        text = col.nameEnglish,
                        fontSize = 16.sp,
                        color = Color.Gray,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Tropical Example / މިސާލު: ${col.exampleDhivehi} (${col.exampleEnglish})",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = OceanTeal
                    )
                }
            }
        }

        Button(
            onClick = { onFinished() },
            colors = ButtonDefaults.buttonColors(containerColor = ReefCoral),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("complete_colors_button")
        ) {
            Icon(Icons.Filled.Star, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Colors Mastered! / ނިމުނީ!", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun ShapesPanel(viewModel: DhivehiViewModel, onFinished: () -> Unit) {
    var selectedShape by remember { mutableStateOf<DhivehiShape?>(DhivehiContent.shapes[0]) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            DhivehiContent.shapes.forEach { shape ->
                val isSelected = selectedShape?.nameEnglish == shape.nameEnglish
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(if (isSelected) OceanTeal.copy(alpha = 0.15f) else Color.White)
                        .border(
                            width = 1.dp,
                            color = if (isSelected) OceanTeal else Color.Transparent,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .clickable {
                            selectedShape = shape
                            viewModel.speakText("Shape is: " + shape.nameEnglish)
                        }
                        .padding(14.dp)
                        .testTag("shape_row_${shape.nameEnglish}"),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(OceanTeal),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = when (shape.nameEnglish) {
                                "Circle" -> "🔴"
                                "Square" -> "🟧"
                                "Triangle" -> "🔺"
                                "Rectangle" -> "🟦"
                                "Star" -> "⭐"
                                else -> "🔶"
                            },
                            fontSize = 20.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "${shape.nameDhivehi} (${shape.nameEnglish})",
                            fontWeight = FontWeight.Bold,
                            color = MidnightDeepBlue,
                            fontSize = 15.sp
                        )
                        Text(
                            text = shape.description,
                            fontSize = 11.sp,
                            color = Color.DarkGray
                        )
                    }
                }
            }
        }

        Button(
            onClick = { onFinished() },
            colors = ButtonDefaults.buttonColors(containerColor = ReefCoral),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("complete_shapes_button")
        ) {
            Icon(Icons.Filled.Star, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Shapes Mastered! / ނިމުނީ!", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}
