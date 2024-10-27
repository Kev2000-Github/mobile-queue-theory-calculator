package com.example.queue_calculator.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.queue_calculator.components.CostCalculationResult
import com.example.queue_calculator.components.InputField
import com.example.queue_calculator.components.KeyPadGeneral
import com.example.queue_calculator.components.TopBar
import com.example.queue_calculator.keypads.ArrowKey
import com.example.queue_calculator.keypads.CommonKeyPads
import com.example.queue_calculator.schemas.validateCostCalculationProps
import com.example.queue_calculator.services.CostCalculationProps
import com.example.queue_calculator.services.CostCalculator
import com.example.queue_calculator.services.CostResult
import com.example.queue_calculator.services.FixedCostCalculationProps
import com.example.queue_calculator.services.VariableCostCalculationProps
import com.example.queue_calculator.ui.theme.QueueCalculatorTheme
import com.example.queue_calculator.utils.KeyDirections
import com.example.queue_calculator.utils.KeyPadDirectionals
import com.example.queue_calculator.utils.appendNumber
import com.example.queue_calculator.utils.popNumber
import com.google.gson.Gson
import kotlinx.coroutines.launch

val inputsDataCostVariable = listOf(
    mapOf(
        "label" to "Tasa de servicio μ",
        "key" to "miu"
    ),
    mapOf(
        "label" to "Costo de servicio $",
        "key" to "serviceCost"
    ),
    mapOf(
        "label" to "Numero de servidores",
        "key" to "C"
    ),
)

fun generateDefaultCostVariableEntries(): List<Pair<String, String>> {
    return listOf(
        Pair("miu", "0"),
        Pair("serviceCost", "0"),
        Pair("C", "1")
    )
}

@Composable
fun Cost_Variable_Values_Screen(navController: NavController, data: String?) {
    QueueCalculatorTheme {
        Scaffold(
            topBar = {
                TopBar(
                    navController = navController,
                    title = "Costos Parte 2"
                )
            }
        ) { innerPadding ->
            Cost_Variable_Values(innerPadding, data)
        }
    }
}

@Composable
fun Cost_Variable_Values(innerPadding: PaddingValues, data: String?) {
    val context = LocalContext.current
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    var costResult by remember {
        mutableStateOf<List<CostResult>>(
            listOf()
        )
    }
    var activeInput by remember {
        mutableIntStateOf(0)
    }
    var disabledDirections by remember {
        mutableStateOf(
            listOf(
                KeyDirections.UP
            )
        )
    }
    var entries by remember {
        mutableStateOf(
            listOf(generateDefaultCostVariableEntries())
        )
    }
    var costProps by remember {
        mutableStateOf(
            CostCalculationProps(
            variableData = listOf(),
            fixedData = FixedCostCalculationProps(1.0, 0, 1.0)
        )
        )
    }
    val pagerState = rememberPagerState(pageCount = {
        entries.size
    })
    val currentPage by remember {
        derivedStateOf { pagerState.currentPage }
    }

    fun appendValue(key: String) {
        val currentForm = entries[currentPage].toMutableList()
        val currentValue = currentForm[activeInput]
        currentForm[activeInput] = Pair(currentValue.first, appendNumber(currentValue.second, key))
        val newEntries = entries.toMutableList()
        newEntries[currentPage] = currentForm
        entries = newEntries
    }

    fun clearValue() {
        val currentForm = entries[currentPage].toMutableList()
        val currentValue = currentForm[activeInput]
        currentForm[activeInput] = Pair(currentValue.first, "0")
        val newEntries = entries.toMutableList()
        newEntries[currentPage] = currentForm
        entries = newEntries
    }

    fun popValue() {
        val currentForm = entries[currentPage].toMutableList()
        val currentValue = currentForm[activeInput]
        val newValue = popNumber(currentValue.second)
        currentForm[activeInput] = Pair(currentValue.first, newValue)
        val newEntries = entries.toMutableList()
        newEntries[currentPage] = currentForm
        entries = newEntries
    }

    fun updateDirectionsAvailability() {
        val newDisabledDirections: MutableList<KeyDirections> = mutableListOf()
        if(activeInput == 0) {
            newDisabledDirections.add(KeyDirections.UP)
        }
        if(activeInput == entries[currentPage].size - 1) {
            newDisabledDirections.add(KeyDirections.DOWN)
        }
        if(currentPage == 0) {
            newDisabledDirections.add(KeyDirections.LEFT)
        }
        if(currentPage == pagerState.pageCount - 1) {
            newDisabledDirections.add(KeyDirections.RIGHT)
        }
        disabledDirections = newDisabledDirections
    }

    fun handleArrow(key: ArrowKey) {
        when(key) {
            ArrowKey.UP -> {
                if(activeInput > 0) {
                    coroutineScope.launch {
                        listState.animateScrollToItem(activeInput)
                    }
                    activeInput -= 1
                    updateDirectionsAvailability()
                }
            }
            ArrowKey.DOWN -> {
                if(activeInput < entries[currentPage].size - 1) {
                    coroutineScope.launch {
                        listState.animateScrollToItem(activeInput)
                    }
                    activeInput += 1
                    updateDirectionsAvailability()
                }
            }
            ArrowKey.LEFT -> {
                if(currentPage > 0) {
                    activeInput = 0
                    coroutineScope.launch {
                        listState.animateScrollToItem(activeInput)
                    }
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage - 1)
                    }
                }
            }
            ArrowKey.RIGHT -> {
                activeInput = 0
                coroutineScope.launch {
                    listState.animateScrollToItem(activeInput)
                }
                if(currentPage < pagerState.pageCount - 1) {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    }
                }
            }
        }
    }

    LaunchedEffect(key1 = pagerState.currentPage) {
        updateDirectionsAvailability()
    }

    fun submit() {
        activeInput = 0
        coroutineScope.launch {
            listState.animateScrollToItem(activeInput)
        }
        coroutineScope.launch {
            pagerState.animateScrollToPage(0)
        }
        val fixedCostCalculationProps = Gson().fromJson(data, FixedCostCalculationProps::class.java)
        val variableCostCalculationProps: MutableList<VariableCostCalculationProps> = mutableListOf()
        entries.forEach { item ->
            val miu = item.find { it.first == "miu" }!!.second
            val serviceCost = item.find {it.first == "serviceCost"}!!.second
            val c = item.find { it.first == "C" }!!.second
            variableCostCalculationProps.add(
                VariableCostCalculationProps(miu.toDouble(), c.toInt(), serviceCost.toDouble())
            )
        }

        val costCalculationProps = CostCalculationProps(variableCostCalculationProps, fixedCostCalculationProps)
        val result = validateCostCalculationProps(costCalculationProps)
        when (result.isValid) {
            true -> {
                costProps = costCalculationProps
                val calculator = CostCalculator()
                costResult = calculator.calculateCosts(costCalculationProps)
            }
            false -> {
                result.errors.forEach {
                    Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    fun addPage() {
        activeInput = 0
        coroutineScope.launch {
            listState.animateScrollToItem(activeInput)
        }
        val newEntries = entries.toMutableList()
        newEntries.add(generateDefaultCostVariableEntries())
        entries = newEntries
        coroutineScope.launch {
            pagerState.animateScrollToPage(pagerState.pageCount - 1)
        }
    }

    fun removePage() {
        val newEntries = entries.toMutableList()
        newEntries.removeAt(currentPage)
        entries = newEntries
    }

    if (costResult.isNotEmpty()) {
        CostCalculationResult(
            costResult = costResult,
            innerPadding = innerPadding,
            onReturn = {
                costResult = listOf()
                updateDirectionsAvailability()
            },
            costCalculationProps = costProps
        )
    }
    else {
        Column(modifier = Modifier
            .padding(innerPadding)
            .padding(horizontal = 10.dp)) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.Top
            ) { idx ->
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .padding(horizontal = 18.dp)
                        .weight(1f)
                        .fillMaxWidth(),
                ) {
                    items(inputsDataCostVariable) { data ->
                        InputField(
                            modifier = Modifier.height(80.dp),
                            value = entries[idx].find { item -> item.first == data.getValue("key") }!!.second,
                            label = data.getValue("label"),
                            isActive = currentPage == idx && entries[currentPage][activeInput].first == data.getValue("key"),
                            onClick = {
                                activeInput = entries[currentPage].indexOfFirst { it.first == data.getValue("key") }
                                updateDirectionsAvailability()
                            }
                        )
                    }
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(15.dp)
                        ) {
                            Button(
                                modifier = Modifier
                                    .height(50.dp)
                                    .weight(1f),
                                onClick = { removePage() },
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Red, disabledContainerColor = Color.Transparent),
                                enabled = entries.size > 1 && idx < pagerState.pageCount - 1
                            ) {
                                Text(
                                    text = if(entries.size > 1 && idx < pagerState.pageCount - 1) "-" else "",
                                    fontWeight = FontWeight(800),
                                    style = MaterialTheme.typography.displaySmall,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                            }
                            Button(
                                modifier = Modifier
                                    .height(50.dp)
                                    .weight(1f),
                                onClick = { addPage() },
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                            ) {
                                Text(
                                    text = "+",
                                    fontWeight = FontWeight(800),
                                    style = MaterialTheme.typography.displaySmall,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                            }
                        }
                    }
                }
            }
            HorizontalDivider(thickness = 1.dp, modifier = Modifier.padding(vertical = 10.dp))
            KeyPadGeneral(
                keypadData = CommonKeyPads,
                modifier = Modifier.padding(bottom = 10.dp),
                appendValue = ::appendValue,
                clearValue = ::clearValue,
                popValue = ::popValue,
                submit = ::submit,
                handleArrow = ::handleArrow,
                columns = when (entries[currentPage][activeInput].first) {
                    "constraint" -> 2
                    else -> 4
                },
                textStyle = when (entries[currentPage][activeInput].first) {
                    "constraint" -> MaterialTheme.typography.displayLarge
                    else -> MaterialTheme.typography.titleLarge
                },
                directional = KeyPadDirectionals.ALL,
                disabledDirections = disabledDirections
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Cost_Variable_Values_Preview() {
    Cost_Variable_Values_Screen(rememberNavController(), null)
}