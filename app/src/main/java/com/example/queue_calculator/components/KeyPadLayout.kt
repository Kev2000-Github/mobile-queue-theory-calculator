package com.example.queue_calculator.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.queue_calculator.R
import com.example.queue_calculator.keypads.KeyPad
import com.example.queue_calculator.keypads.KeyTheme
import com.example.queue_calculator.utils.KeyDirections
import com.example.queue_calculator.utils.KeyPadDirectionals

val keyDirectionsAll = listOf(
    KeyDirections.LEFT,
    KeyDirections.UP,
    KeyDirections.DOWN,
    KeyDirections.RIGHT
)

val keyDirectionsVertical = listOf(
    KeyDirections.UP,
    KeyDirections.DOWN
)

@Composable
fun KeyPadLayout(
    modifier: Modifier = Modifier,
    onKeyPress: (String) -> Unit = {},
    keyPads: List<KeyPad>,
    columns: Int = 4,
    textStyle: TextStyle = MaterialTheme.typography.titleLarge,
    directional: KeyPadDirectionals = KeyPadDirectionals.VERTICAL,
    disabledDirections: List<KeyDirections> = listOf()
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        (if (directional == KeyPadDirectionals.VERTICAL) keyDirectionsVertical else keyDirectionsAll).forEach { item ->
            Button(
                modifier = Modifier
                    .height(50.dp)
                    .padding(vertical = 2.dp, horizontal = 10.dp)
                    .weight(1f),
                onClick = { onKeyPress(item.name) },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                enabled = !disabledDirections.contains(item)
            ) {
                Icon(
                    modifier = Modifier.rotate(
                        when (item) {
                            KeyDirections.UP -> 0f
                            KeyDirections.LEFT -> 270f
                            KeyDirections.RIGHT -> 90f
                            else -> 180f
                        }
                    ),
                    painter = painterResource(id = R.drawable.up_arrow),
                    tint = MaterialTheme.colorScheme.onBackground,
                    contentDescription = "$item arrow"
                )
            }
        }

    }
    LazyVerticalStaggeredGrid(
        modifier = modifier,
        columns = StaggeredGridCells.Fixed(columns),
        verticalItemSpacing = 0.dp,
        horizontalArrangement = Arrangement.spacedBy(space = 2.dp)
    ) {
        items(keyPads) {
            keypad -> Button(
                modifier = Modifier
                    .height(90.dp * keypad.verticalSpan)
                    .padding(10.dp),
                onClick = {onKeyPress(keypad.value)},
                shape = RoundedCornerShape(8.dp),
                colors = when(keypad.theme) {
                    KeyTheme.NUMERIC -> ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    KeyTheme.OPTION -> ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                    KeyTheme.NONE -> ButtonDefaults.buttonColors(containerColor = Color.Transparent, disabledContainerColor = Color.Transparent)
                },
                enabled = keypad.label != ""
            ) {
            when(keypad.label) {
                "DEL" -> Icon(painter = painterResource(id = R.drawable.backspace), tint = MaterialTheme.colorScheme.onBackground, contentDescription = "backspace", modifier = Modifier.size(30.dp))
                "<" -> Icon(painter = painterResource(id = R.drawable.fa_team_fontawesome_fontawesome_less_than_equal), tint = MaterialTheme.colorScheme.onBackground, contentDescription = "less than or equal", modifier = Modifier.size(30.dp))
                ">" -> Icon(painter = painterResource(id = R.drawable.fa_team_fontawesome_fontawesome_less_than_equal), tint = MaterialTheme.colorScheme.onBackground, contentDescription = "greater than or equal", modifier = Modifier.size(30.dp).scale(-1f, 1f))
                else -> Text(text = keypad.label, fontWeight = FontWeight(800), style = textStyle, color = MaterialTheme.colorScheme.onBackground)
            }
        }
        }
    }
}