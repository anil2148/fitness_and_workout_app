package com.example.fitnessworkout.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val FitnessGreen = Color(0xFF36D17C)
val DeepGreen = Color(0xFF0E6B3C)
val FitnessBlack = Color(0xFF111814)
val SoftBackground = Color(0xFFF3F7F4)

private val LightColors = lightColorScheme(
    primary = DeepGreen,
    secondary = FitnessGreen,
    background = SoftBackground,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = FitnessBlack,
    onBackground = FitnessBlack,
    onSurface = FitnessBlack
)

private val DarkColors = darkColorScheme(
    primary = FitnessGreen,
    secondary = Color(0xFF8AE8B4),
    background = FitnessBlack,
    surface = Color(0xFF1C2922),
    onPrimary = FitnessBlack,
    onBackground = Color.White,
    onSurface = Color.White
)

@Composable
fun FitnessTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (isSystemInDarkTheme()) DarkColors else LightColors,
        content = content
    )
}
