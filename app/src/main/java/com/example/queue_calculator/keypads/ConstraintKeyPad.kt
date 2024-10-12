package com.example.queue_calculator.keypads

val ConstraintKeyPads: List<KeyPad> = listOf(
    KeyPad("<", "LESS_THAN_OR_EQUAL", theme = KeyTheme.NUMERIC, 2),
    KeyPad("=", "ENTER", theme = KeyTheme.OPTION, 4),
    KeyPad(">", "GREATER_THAN_OR_EQUAL", theme = KeyTheme.NUMERIC, 2),
    )
