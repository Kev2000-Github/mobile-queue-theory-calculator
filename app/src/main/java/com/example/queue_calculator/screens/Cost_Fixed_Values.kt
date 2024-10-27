package com.example.queue_calculator.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.queue_calculator.components.InputField
import com.example.queue_calculator.components.KeyPadGeneral
import com.example.queue_calculator.components.TopBar
import com.example.queue_calculator.keypads.ArrowKey
import com.example.queue_calculator.keypads.CommonKeyPads
import com.example.queue_calculator.keypads.NoCommaKeyPad
import com.example.queue_calculator.navigation.COST_VARIABLE_VALUES
import com.example.queue_calculator.schemas.validateFixedCostCalculationProps
import com.example.queue_calculator.services.FixedCostCalculationProps
import com.example.queue_calculator.ui.theme.QueueCalculatorTheme
import com.example.queue_calculator.utils.KeyDirections
import com.example.queue_calculator.utils.appendNumber
import com.example.queue_calculator.utils.popNumber
import com.google.gson.Gson
import kotlinx.coroutines.launch

val inputsDataCostFixedValues = listOf(
    mapOf(
        "label" to "Tasa de llegada λ",
        "key" to "lambda"
    ),
    mapOf(
        "label" to "Costo de espera $",
        "key" to "waitCost"
    ),
    mapOf(
        "label" to "Capacidad maxima N",
        "key" to "N"
    ),
)

@Composable
fun Cost_Fixed_Values_Screen(navController: NavController) {
    QueueCalculatorTheme {
        Scaffold(
            topBar = {
                TopBar(
                    navController = navController,
                    title = "Costos parte 1"
                )
            }
        ) { innerPadding ->
            Cost_Fixed_Values(innerPadding, navController)
        }
    }
}

@Composable
fun Cost_Fixed_Values(innerPadding: PaddingValues, navController: NavController) {
    val context = LocalContext.current
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    var activeInput by remember {
        mutableIntStateOf(0)
    }
    var entries by remember {
        mutableStateOf(
            listOf(
                Pair("lambda", "0"),
                Pair("waitCost","0"),
                Pair("N", "0"),
            )
        )
    }
    var disabledDirections by remember {
        mutableStateOf(
            listOf(
                KeyDirections.UP
            )
        )
    }

    fun appendValue(key: String) {
        val currentValue = entries[activeInput]
        val newEntries = entries.toMutableList()
        newEntries[activeInput] = Pair(currentValue.first, appendNumber(currentValue.second, key))
        entries = newEntries
    }

    fun clearValue() {
        val currentValue = entries[activeInput]
        val newEntries = entries.toMutableList()
        newEntries[activeInput] = Pair(currentValue.first, "0")
        entries = newEntries
    }

    fun popValue() {
        val currentValue = entries[activeInput]
        val newEntries = entries.toMutableList()
        val newValue = popNumber(currentValue.second)
        newEntries[activeInput] = Pair(currentValue.first, newValue)
        entries = newEntries
    }

    fun updateDirectionsAvailability() {
        val newDisabledDirections: MutableList<KeyDirections> = mutableListOf()
        if(activeInput == 0) {
            newDisabledDirections.add(KeyDirections.UP)
        }
        if(activeInput == entries.size - 1) {
            newDisabledDirections.add(KeyDirections.DOWN)
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
                }
            }
            ArrowKey.DOWN -> {
                if(activeInput < entries.size - 1) {
                    coroutineScope.launch {
                        listState.animateScrollToItem(activeInput)
                    }
                    activeInput += 1
                }
            }
            else -> {}
        }
        updateDirectionsAvailability()
    }

    fun submit() {
        activeInput = 0
        coroutineScope.launch {
            listState.animateScrollToItem(activeInput)
        }
        val lambda = entries.find { it.first == "lambda" }!!.second
        val n = entries.find { it.first == "N" }!!.second
        val waitCost = entries.find {it.first == "waitCost"}!!.second
        val props = FixedCostCalculationProps(
            lambda.toDouble(),
            n.toInt(),
            waitCost.toDouble()
        )
        val result = validateFixedCostCalculationProps(props)
        when (result.isValid) {
            true -> {
                val fixedCostCalculationData = Gson().toJson(props)
                navController.navigate("$COST_VARIABLE_VALUES/$fixedCostCalculationData")
            }
            false -> {
                result.errors.forEach {
                    Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    Column(modifier = Modifier
        .padding(innerPadding)
        .padding(horizontal = 10.dp)) {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .padding(horizontal = 18.dp)
                .weight(1f)
                .fillMaxWidth(),
        ) {
            items(inputsDataCostFixedValues) { data ->
                InputField(
                    modifier = Modifier.height(80.dp),
                    value = entries.find { item -> item.first == data.getValue("key") }!!.second,
                    label = data.getValue("label"),
                    isActive = entries[activeInput].first == data.getValue("key"),
                    onClick = {
                        activeInput = entries.indexOfFirst { it.first == data.getValue("key") }
                        updateDirectionsAvailability()
                    }
                )
            }
        }
        HorizontalDivider(thickness = 1.dp, modifier = Modifier.padding(vertical = 10.dp))
        KeyPadGeneral(
            keypadData = when {
                listOf("N", "C").contains(entries[activeInput].first) -> NoCommaKeyPad
                else -> CommonKeyPads
            },
            modifier = Modifier.padding(bottom = 10.dp),
            appendValue = ::appendValue,
            clearValue = ::clearValue,
            popValue = ::popValue,
            submit = ::submit,
            handleArrow = ::handleArrow,
            disabledDirections = disabledDirections
        )
    }
}

@Preview(showBackground = true)
@Composable
fun Cost_Fixed_Values_Preview() {
    Cost_Fixed_Values_Screen(rememberNavController())
}