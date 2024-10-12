package com.example.queue_calculator.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
fun MetricList(
    L: Double,
    Lq: Double,
    W: Double,
    Wq: Double
) {
    val column1Weight = .1f // 20%
    val column2Weight = .4f // 30%
    val column3Weight = .2f // 20%
    val column4Weight = .3f // 30%

    val metrics = listOf(Pair(Pair("L", L), Pair("Lq", Lq)), Pair(Pair("W", W), Pair("Wq", Wq)))

    LazyColumn {
        items(metrics) { it ->
            Row(Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                TableCell(
                    fontWeight = FontWeight.Bold,
                    text = it.first.first,
                    weight = column1Weight
                )
                TableCell(
                    color = MaterialTheme.colorScheme.onBackground,
                    text = it.first.second.toString(),
                    weight = column2Weight
                )
                TableCell(
                    fontWeight = FontWeight.Bold,
                    text = it.second.first,
                    weight = column3Weight
                )
                TableCell(
                    color = MaterialTheme.colorScheme.onBackground,
                    text = it.second.second.toString(),
                    weight = column4Weight
                )
            }
        }
    }
}

@Preview
@Composable
fun MetricListPreview() {
    MetricList(1.0,2.0,3.0,4.0)
}