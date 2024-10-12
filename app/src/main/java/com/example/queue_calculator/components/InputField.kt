package com.example.queue_calculator.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.queue_calculator.R

@Composable
fun InputField(
    modifier: Modifier = Modifier,
    value: String,
    label: String,
    isActive: Boolean,
    onClick: () -> Unit,
    labelStyle: TextStyle = MaterialTheme.typography.titleLarge,
    textStyle: TextStyle = MaterialTheme.typography.displaySmall
) {
    Row( modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = label,
            style = labelStyle,
            fontWeight = FontWeight(500)
        )
        Spacer(modifier = Modifier.weight(1f))
        when(value) {
            "LEFT" ->  Icon(
                painter = painterResource(id = R.drawable.fa_team_fontawesome_fontawesome_less_than_equal),
                tint = if (isActive) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.secondary,
                contentDescription = "less than or equal",
                modifier = Modifier.size(30.dp).clickable { onClick() }
            )
            "RIGHT" -> Icon(
                painter = painterResource(id = R.drawable.fa_team_fontawesome_fontawesome_less_than_equal),
                tint = if (isActive) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.secondary,
                contentDescription = "greater than or equal",
                modifier = Modifier.size(30.dp).scale(-1f, 1f).clickable { onClick() }
            )
            else -> Text(
                text = value,
                color = if (isActive) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.secondary,
                style = textStyle,
                fontWeight = FontWeight(500),
                modifier = Modifier.clickable { onClick() }
            )
        }
    }
}