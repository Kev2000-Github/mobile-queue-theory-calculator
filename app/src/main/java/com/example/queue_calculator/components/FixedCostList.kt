package com.example.queue_calculator.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.queue_calculator.services.FixedCostCalculationProps


@Composable
fun FixedCostList(props: FixedCostCalculationProps) {
    val column1Weight = .3f // 20%
    val column2Weight = .7f // 30%

    val metrics = listOf(
        Pair("Lambda", props.lambda),
        Pair("Capacidad maxima", props.maxCapacity),
        Pair("Costo de espera", props.waitCost),
    )

    LazyColumn {
        items(metrics) { it ->
            Row(Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                TableCell(
                    fontWeight = FontWeight.Bold,
                    text = it.first,
                    weight = column1Weight
                )
                TableCell(
                    color = MaterialTheme.colorScheme.onBackground,
                    text = it.second.toString(),
                    weight = column2Weight
                )
            }
        }
    }
}

@Preview
@Composable
fun FixedCostListPreview() {
    FixedCostList(
        FixedCostCalculationProps(
            lambda = 1.0,
            maxCapacity = 10,
            waitCost = 10.0
        )
    )
}