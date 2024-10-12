package com.example.queue_calculator.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun RowScope.TableCell(
    text: String,
    weight: Float,
    fontWeight: FontWeight = FontWeight.Normal,
    color: Color = Color.White
) {
    Text(
        text = text,
        Modifier
            .weight(weight)
            .padding(8.dp),
        fontWeight = fontWeight,
        color = color
    )
}