package com.example.queue_calculator.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.queue_calculator.services.QueueProbability


@Composable
fun ProbabilityTable(
    probabilities: List<QueueProbability>,
    modifier: Modifier = Modifier
) {
    val column1Weight = .2f // 20%
    val column2Weight = .4f // 40%
    val column3Weight = .4f // 40%

    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.height(40.dp)
        ) {
            TableCell(fontWeight = FontWeight.Bold, text = "N", weight = column1Weight)
            TableCell(fontWeight = FontWeight.Bold, text = "Probabilidad", weight = column2Weight)
            TableCell(fontWeight = FontWeight.Bold, text = "% Acumulado", weight = column3Weight)
        }
        HorizontalDivider(
            modifier = Modifier
                .height(1.dp)
                .fillMaxWidth(),
        )
        LazyColumn{
            itemsIndexed(probabilities) { id, it ->
                Row(Modifier.fillMaxWidth().padding(vertical = 8.dp).height(40.dp)) {
                    TableCell(color = MaterialTheme.colorScheme.onSurface, text = id.toString(), weight = column1Weight)
                    TableCell(color = MaterialTheme.colorScheme.onSurface, text = it.probability.toString(), weight = column2Weight)
                    TableCell(color = MaterialTheme.colorScheme.onSurface, text = it.cumulativeProbability.toString(), weight = column3Weight)
                }
                HorizontalDivider(
                    modifier = Modifier
                        .height(1.dp)
                        .fillMaxWidth(),
                )
            }
        }
    }
}

@Preview
@Composable
fun TablePreview() {
    val probabilities = listOf(
        QueueProbability(0.1, 0.1),
        QueueProbability(0.2, 0.3),
        QueueProbability(0.3, 0.6),
        QueueProbability(0.4, 0.9),
    )
    ProbabilityTable(probabilities)
}