package com.example.queue_calculator.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

//val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
//val Pink80 = Color(0xFFEFB8C8)

//val Purple40 = Color(0xFF6650a4)
//val PurpleGrey40 = Color(0xFF625b71)
//val Pink40 = Color(0xFF7D5260)

val backgroundColor = Color(0xFF1E201E)

val BgTop = Color(0xFF571954)
val BgBottom = Color(0xFF2C003C)
val BgMiddle = Color(0xFF3D0B45)
val Gradient = Brush.verticalGradient(
    0.0f to BgTop,
    0.5f to BgMiddle,
    1.0f to BgBottom,
    startY = 0.0f,
    endY = 1500.0f
)