package com.example.queue_calculator.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF3C3D37),
    secondary = Color(0xFF697565),
    background = backgroundColor,
    onBackground = Color(0xFFECDFCC),
    onSurface = Color(0xFFD8D2C2),
    onSurfaceVariant = Color(0xFFFFB0B0),
    )

@Composable
fun QueueCalculatorTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = CustomTypography,
        content = content
    )
}