package com.example.queue_calculator.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.queue_calculator.services.CumulativeProbabilityData
import kotlinx.coroutines.launch

val iterationOptions = listOf(
    Pair("5 estados", 5),
    Pair("10 estados", 10),
    Pair("15 estados", 15),
    Pair("20 estados", 20),
    Pair("30 estados", 30),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculationResult(
    queueResult: CumulativeProbabilityData,
    innerPadding: PaddingValues,
    onReturn: () -> Unit = {},
    updateIterations: (iterations: Int) -> Unit = {}
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }

    fun hideSheet() {
        scope.launch { sheetState.hide() }.invokeOnCompletion {
            if (!sheetState.isVisible) {
                showBottomSheet = false
            }
        }
    }

    Column(
        modifier = Modifier
            .padding(innerPadding)
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.onBackground,
            ),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.onBackground),
            onClick = { showBottomSheet = true }
        ) {
            Text(
                text = "Mostrando hasta ${queueResult.iterations} estados",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        MetricList(
            L = queueResult.systemLength,
            Lq = queueResult.queueLength,
            W = queueResult.sysWaitingTime,
            Wq = queueResult.queueWaitingTime
        )
        ProbabilityTable(
            probabilities = queueResult.probN,
            modifier = Modifier.weight(1f)
        )
        Button(
            onClick = onReturn,
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            shape = RoundedCornerShape(0),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
        ) {
            Text(
                text = "Volver a calcular",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showBottomSheet = false
                },
                sheetState = sheetState,
                containerColor = MaterialTheme.colorScheme.background
            ) {
                Column(
                    modifier = Modifier
                        .height(400.dp)
                        .padding(bottom = 15.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    LazyColumn {
                        items(iterationOptions) {
                            Button(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(0),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.Transparent,
                                ),
                                onClick = {
                                    updateIterations(it.second)
                                    hideSheet()
                                }
                            ) {
                                Text(
                                    text = it.first,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onBackground,
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        onClick = { hideSheet() }) {
                        Text(
                            text = "Cancelar",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.padding(vertical = 6.dp)
                        )
                    }
                }
            }
        }
    }
}