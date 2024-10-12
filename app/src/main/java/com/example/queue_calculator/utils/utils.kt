package com.example.queue_calculator.utils

fun appendNumber(content: String, key: String): String {
    when(key) {
        "." -> {
            if (!content.contains(".")) {
                return content + key
            }
            return content
        }
        else -> {
            if (content == "0" && key != "0") {
                return key
            }
            else if (content != "0") {
                return content + key
            }

        }
    }
    return content
}

fun popNumber(content: String): String {
    when {
        content == "0" -> {}
        content.length == 1 -> {
            return "0"
        }
        else -> {
            val newValue = content.dropLast(1)
            return newValue
        }
    }
    return content
}

fun toFixedIfNecessary(value: Double, decimals: Int): Double {
    val decimalParsed = "%.${decimals}f"
    return String.format(decimalParsed, value).toDouble()
}

fun factorial(n: Double): Double {
    if (n == 0.0) {
        return 1.0
    }
    return n * factorial(n - 1)
}