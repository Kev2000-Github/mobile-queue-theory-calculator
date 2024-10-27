package com.example.queue_calculator.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.queue_calculator.screens.Cost_Fixed_Values_Screen
import com.example.queue_calculator.screens.Cost_Variable_Values_Screen
import com.example.queue_calculator.screens.HomeScreen
import com.example.queue_calculator.screens.MM1_Finite_Screen
import com.example.queue_calculator.screens.MM1_General_Screen
import com.example.queue_calculator.screens.MM1_Infinite_Screen
import com.example.queue_calculator.screens.MMC_Finite_Screen
import com.example.queue_calculator.screens.MMC_Infinite_Screen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = HOME,
    ) {
        composable(route = HOME) {
            HomeScreen(navController)
        }
        composable(route = MM1_INFINITE) {
            MM1_Infinite_Screen(navController = navController)
        }
        composable(route = MM1_FINITE) {
            MM1_Finite_Screen(navController = navController)
        }
        composable(route = MM1_GENERAL) {
            MM1_General_Screen(navController = navController)
        }
        composable(route = MMC_INFINITE) {
            MMC_Infinite_Screen(navController = navController)
        }
        composable(route = MMC_FINITE) {
            MMC_Finite_Screen(navController = navController)
        }
        composable(route = COST_FIXED_VALUES) {
            Cost_Fixed_Values_Screen(navController = navController)
        }
        composable(route = "$COST_VARIABLE_VALUES/{data}") { backStackEntry ->
            Cost_Variable_Values_Screen(navController = navController, backStackEntry.arguments?.getString("data") )
        }
    }
}