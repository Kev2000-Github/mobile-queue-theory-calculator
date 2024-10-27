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
import com.example.queue_calculator.services.CostResult


@Composable
fun CostTable(
    costResults: List<CostResult>,
    modifier: Modifier = Modifier
) {
    val column0Weight = .2f
    val column1Weight = .2f
    val column2Weight = .2f
    val column3Weight = .2f
    val column4Weight = .2f

    fun getBGColor(etc: Double): Color {
        val minETC =costResults.minBy { it.etc }
        if (etc == minETC.etc) return Color(0xFF3C3D37)
        return Color.Transparent
    }

    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.height(40.dp)
        ) {
            TableCell(fontWeight = FontWeight.Bold, text = "N", weight = column0Weight)
            TableCell(fontWeight = FontWeight.Bold, text = "L", weight = column1Weight)
            TableCell(fontWeight = FontWeight.Bold, text = "EOC", weight = column2Weight)
            TableCell(fontWeight = FontWeight.Bold, text = "EWC", weight = column3Weight)
            TableCell(fontWeight = FontWeight.Bold, text = "ETC", weight = column4Weight)
        }
        HorizontalDivider(
            modifier = Modifier
                .height(1.dp)
                .fillMaxWidth(),
        )
        LazyColumn{
            itemsIndexed(costResults) { id, it ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .height(40.dp)
                        .background(getBGColor(it.etc))
                ) {
                    TableCell(color = MaterialTheme.colorScheme.onSurface, text = (id + 1).toString(), weight = column0Weight)
                    TableCell(color = MaterialTheme.colorScheme.onSurface, text = it.l.toString(), weight = column1Weight)
                    TableCell(color = MaterialTheme.colorScheme.onSurface, text = it.eoc.toString(), weight = column2Weight)
                    TableCell(color = MaterialTheme.colorScheme.onSurface, text = it.ewc.toString(), weight = column3Weight)
                    TableCell(color = MaterialTheme.colorScheme.onSurface, text = it.etc.toString(), weight = column4Weight)
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
fun CostTablePreview() {
    val costResults = listOf(
        CostResult(1.0, 2.0,3.0,4.0),
        CostResult(1.0, 2.0,3.0,6.0),
    )
    CostTable(costResults)
}