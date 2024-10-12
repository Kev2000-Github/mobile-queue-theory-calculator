package com.example.queue_calculator.utils

import androidx.navigation.NavController
import com.example.queue_calculator.navigation.HOME

/**
 * Attempts to pop the controller's back stack.
 * If the back stack is empty, it will return to HOME
 */
fun NavController.safePopBackStack() {
    if (!popBackStack()) {
        navigate(HOME)
    }
}