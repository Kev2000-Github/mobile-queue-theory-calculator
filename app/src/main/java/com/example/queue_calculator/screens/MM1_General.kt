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
import com.example.queue_calculator.components.CalculationResult
import com.example.queue_calculator.components.InputField
import com.example.queue_calculator.components.KeyPadGeneral
import com.example.queue_calculator.components.TopBar
import com.example.queue_calculator.keypads.ArrowKey
import com.example.queue_calculator.keypads.CommonKeyPads
import com.example.queue_calculator.keypads.ConstraintKeyPads
import com.example.queue_calculator.keypads.NoCommaKeyPad
import com.example.queue_calculator.schemas.validateMM1GeneralProps
import com.example.queue_calculator.services.CumulativeProbabilityData
import com.example.queue_calculator.services.ICalculationProps
import com.example.queue_calculator.services.ICalculator
import com.example.queue_calculator.services.InOutAvg
import com.example.queue_calculator.services.InOutType
import com.example.queue_calculator.services.calculation_cases.MM1General
import com.example.queue_calculator.ui.theme.QueueCalculatorTheme
import com.example.queue_calculator.utils.DEFAULT_ITERATIONS
import com.example.queue_calculator.utils.KeyDirections
import com.example.queue_calculator.utils.KeyPadDirectionals
import com.example.queue_calculator.utils.appendNumber
import com.example.queue_calculator.utils.popNumber
import kotlinx.coroutines.launch

val inputsDataGeneral = listOf(
    mapOf(
        "label" to "Tasa de llegada λ",
        "key" to "lambda"
    ),
    mapOf(
        "label" to "Tasa de servicio μ",
        "key" to "miu"
    ),
    mapOf(
        "label" to "Tipo de restriccion",
        "key" to "constraint"
    ),
    mapOf(
        "label" to "Numero de la restriccion",
        "key" to "numberAnchor"
    ),
)

fun generateDefault(numberAnchor: String): List<Pair<String, String>> {
    return listOf(
        Pair("lambda", "0"),
        Pair("miu", "0"),
        Pair("constraint", "LEFT"),
        Pair("numberAnchor", numberAnchor)
    )
}

@Composable
fun MM1_General_Screen(navController: NavController) {
    QueueCalculatorTheme {
        Scaffold(
            topBar = {
                TopBar(
                    navController = navController,
                    title = "MM1 General"
                )
            }
        ) { innerPadding ->
            MM1_General(innerPadding)
        }
    }
}

@Composable
fun MM1_General(innerPadding: PaddingValues) {
    val context = LocalContext.current
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    var queueResult by remember {
        mutableStateOf<CumulativeProbabilityData?>(null)
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
    var iterations by remember {
        mutableIntStateOf(DEFAULT_ITERATIONS)
    }
    var entries by remember {
        mutableStateOf(
            listOf(generateDefault("1"))
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

    fun handleConstraint(constraint: InOutType) {
        val currentForm = entries[currentPage].toMutableList()
        val currentValue = currentForm[activeInput]
        when(constraint) {
            InOutType.LESS_THAN_EQUAL -> currentForm[activeInput] = Pair(currentValue.first, "LEFT")
            InOutType.REST -> currentForm[activeInput] = Pair(currentValue.first, "RIGHT")
            else -> {}
        }
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

    fun orderEntries() {
        val newEntries = entries.toMutableList()
        newEntries.sortBy { it.find { item -> item.first == "numberAnchor" }!!.second.toInt() }
        entries = newEntries
    }

    fun submit() {
        activeInput = 0
        coroutineScope.launch {
            listState.animateScrollToItem(activeInput)
        }
        coroutineScope.launch {
            pagerState.animateScrollToPage(0)
        }
        orderEntries()
        val inOutAverages: MutableList<InOutAvg> = mutableListOf()
        entries.forEach { item ->
            val lambda = item.find { it.first == "lambda" }!!.second
            val miu = item.find { it.first == "miu" }!!.second
            val constraint = item.find { it.first == "constraint" }!!.second
            val numberAnchor = item.find { it.first == "numberAnchor" }!!.second
            val type = when(constraint) {
                "LEFT" -> InOutType.LESS_THAN_EQUAL
                "RIGHT" -> InOutType.REST
                else -> InOutType.LESS_THAN_EQUAL
            }
            inOutAverages.add(
                InOutAvg(
                    type = type,
                    lambda = lambda.toDouble(),
                    miu = miu.toDouble(),
                    numberAnchor = numberAnchor.toInt(),
                )
            )
        }
        val calculator: ICalculator = MM1General()
        val props = ICalculationProps(
            iterations,
            0,
            1,
            inOutAverages,
        )
        val result = validateMM1GeneralProps(props)
        when (result.isValid) {
            true -> {
                queueResult = calculator.calculateCumulativeProbabilities(props)
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
        newEntries.add(generateDefault((newEntries.size + 1).toString()))
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

    fun updateIterations(newIterations: Int) {
        iterations = newIterations
        submit()
    }

    if (queueResult != null) {
        CalculationResult(
            queueResult = queueResult!!,
            innerPadding = innerPadding,
            onReturn = { queueResult = null },
            updateIterations = ::updateIterations
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
                    items(inputsDataGeneral) { data ->
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
                keypadData = when (entries[currentPage][activeInput].first) {
                    "iterations" -> NoCommaKeyPad
                    "constraint" -> ConstraintKeyPads
                    else -> CommonKeyPads
                },
                modifier = Modifier.padding(bottom = 10.dp),
                appendValue = ::appendValue,
                clearValue = ::clearValue,
                popValue = ::popValue,
                submit = ::submit,
                handleArrow = ::handleArrow,
                handleConstraint = ::handleConstraint,
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
fun MM1_General_Preview() {
    MM1_General_Screen(rememberNavController())
}