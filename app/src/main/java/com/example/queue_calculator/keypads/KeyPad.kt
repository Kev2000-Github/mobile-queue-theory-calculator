package com.example.queue_calculator.keypads

data class KeyPad(
    val label: String,
    val value: String,
    val theme: KeyTheme,
    val verticalSpan: Int,
)

enum class KeyTheme {
    OPTION,
    NUMERIC,
    NONE
}

enum class ArrowKey {
    UP,
    DOWN,
    LEFT,
    RIGHT
}