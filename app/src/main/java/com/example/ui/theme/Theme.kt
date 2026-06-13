package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = ReefCoral,
    secondary = OceanTeal,
    tertiary = SandyGold,
    background = MidnightDeepBlue,
    surface = MidnightOceanNavy,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.Black,
    onBackground = Color.White,
    onSurface = Color.White,
    primaryContainer = LagoonAqua,
    onPrimaryContainer = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = ReefCoral,
    secondary = OceanTeal,
    tertiary = SandyGold,
    background = LagoonSkyLight,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.Black,
    onBackground = MidnightDeepBlue,
    onSurface = MidnightDeepBlue,
    primaryContainer = PaleCoralAccent,
    onPrimaryContainer = ReefCoral
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Set to false to keep our Maldives tropical branding consistent!
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
