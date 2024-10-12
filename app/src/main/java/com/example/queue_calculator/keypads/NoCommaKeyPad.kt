package com.example.queue_calculator.keypads

val NoCommaKeyPad: List<KeyPad> = listOf(
    KeyPad("1", "1", theme = KeyTheme.NUMERIC, 1),
    KeyPad("2", "2", theme = KeyTheme.NUMERIC, 1),
    KeyPad("3", "3", theme = KeyTheme.NUMERIC, 1),
    KeyPad("C", "C", theme = KeyTheme.OPTION, 1),
    KeyPad("4", "4", theme = KeyTheme.NUMERIC, 1),
    KeyPad("5", "5", theme = KeyTheme.NUMERIC, 1),
    KeyPad("6", "6", theme = KeyTheme.NUMERIC, 1),
    KeyPad("DEL", "DEL", theme = KeyTheme.OPTION, 1),
    KeyPad("7", "7", theme = KeyTheme.NUMERIC, 1),
    KeyPad("8", "8", theme = KeyTheme.NUMERIC, 1),
    KeyPad("9", "9", theme = KeyTheme.NUMERIC, 1),
    KeyPad("=", "ENTER", theme = KeyTheme.OPTION, 2),
    KeyPad("", "", theme = KeyTheme.NONE, 1),
    KeyPad("0", "0", theme = KeyTheme.NUMERIC, 1),
    KeyPad("", "", theme = KeyTheme.NONE, 1),
)
