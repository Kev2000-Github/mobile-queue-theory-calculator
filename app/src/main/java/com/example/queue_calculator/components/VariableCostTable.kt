package com.example.queue_calculator.components

import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.queue_calculator.services.VariableCostCalculationProps


@Composable
fun VariableCostTable(
    variableCosts: List<VariableCostCalculationProps>,
    modifier: Modifier = Modifier
) {
    val column0Weight = .15f
    val column1Weight = .25f
    val column2Weight = .3f
    val column3Weight = .3f

    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.height(40.dp)
        ) {
            TableCell(fontWeight = FontWeight.Bold, text = "N", weight = column0Weight)
            TableCell(fontWeight = FontWeight.Bold, text = "miu", weight = column1Weight)
            TableCell(fontWeight = FontWeight.Bold, text = "servidores", weight = column2Weight)
            TableCell(fontWeight = FontWeight.Bold, text = "costo", weight = column3Weight)
        }
        HorizontalDivider(
            modifier = Modifier
                .height(1.dp)
                .fillMaxWidth(),
        )
        LazyColumn{
            itemsIndexed(variableCosts) { id, it ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .height(40.dp)
                        .background(Color.Transparent)
                ) {
                    TableCell(color = MaterialTheme.colorScheme.onSurface, text = (id + 1).toString(), weight = column0Weight)
                    TableCell(color = MaterialTheme.colorScheme.onSurface, text = it.miu.toString(), weight = column1Weight)
                    TableCell(color = MaterialTheme.colorScheme.onSurface, text = it.numberServers.toString(), weight = column2Weight)
                    TableCell(color = MaterialTheme.colorScheme.onSurface, text = it.cost.toString(), weight = column3Weight)
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
fun VariableCostTablePreview() {
    val variableCosts = listOf(
        VariableCostCalculationProps(1.0, 2,3.0),
        VariableCostCalculationProps(1.0, 2,3.0),
    )
    VariableCostTable(variableCosts)
}