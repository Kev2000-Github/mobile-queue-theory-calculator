package com.example.queue_calculator.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.queue_calculator.navigation.COST_FIXED_VALUES
import com.example.queue_calculator.navigation.MM1_FINITE
import com.example.queue_calculator.navigation.MM1_GENERAL
import com.example.queue_calculator.navigation.MM1_INFINITE
import com.example.queue_calculator.navigation.MMC_FINITE
import com.example.queue_calculator.navigation.MMC_INFINITE
import com.example.queue_calculator.ui.theme.AppFont
import com.example.queue_calculator.ui.theme.QueueCalculatorTheme
import com.example.queue_calculator.utils.MenuOption

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    QueueCalculatorTheme {
        Scaffold(
            topBar = {
                TopAppBar(title = {Text(text = "")}, colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                ))
            }
        ) { innerPadding ->
            HomeContent(innerPadding, navController)
        }
    }
}

@Composable
fun HomeContent(innerPadding: PaddingValues, navController: NavController) {
    Column(modifier = Modifier.padding(innerPadding)) {
        MenuGridView(navController)
    }
}

@Composable
fun MenuGridView(navController: NavController) {
    val menuOptions: List<MenuOption> = listOf(
        MenuOption(MM1_INFINITE,"infinito", "MM1"),
        MenuOption(MM1_FINITE,"finito", "MM1"),
        MenuOption(MM1_GENERAL,"general", "MM1"),
        MenuOption(MMC_INFINITE,"infinito", "MMC"),
        MenuOption(MMC_FINITE,"finito", "MMC"),
        MenuOption(COST_FIXED_VALUES,"costos", "MMC"),
    )
    LazyVerticalGrid(columns = GridCells.Fixed(3), modifier = Modifier.padding(10.dp)) {
        items(menuOptions) { option ->
            Card(
                onClick = {
                    navController.navigate(option.href)
                },
                modifier = Modifier.padding(all = 10.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.Transparent
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = 15.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = option.icon,
                        fontFamily = AppFont.RubikMonoOne,
                        fontSize = MaterialTheme.typography.headlineMedium.fontSize,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.padding(vertical = 4.dp))
                    Text(
                        text = option.label,
                        style = MaterialTheme.typography.titleMedium,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    HomeScreen(rememberNavController())
}