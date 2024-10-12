package com.example.queue_calculator.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import com.example.queue_calculator.keypads.ArrowKey
import com.example.queue_calculator.keypads.CommonKeyPads
import com.example.queue_calculator.keypads.KeyPad
import com.example.queue_calculator.services.InOutType
import com.example.queue_calculator.utils.KeyDirections
import com.example.queue_calculator.utils.KeyPadDirectionals

@Composable
fun KeyPadGeneral(
    keypadData: List<KeyPad>,
    modifier: Modifier = Modifier,
    appendValue: (String) -> Unit = {},
    clearValue: () -> Unit = {},
    popValue: () -> Unit = {},
    submit: () -> Unit = {},
    handleArrow: (key: ArrowKey) -> Unit = {},
    handleConstraint: (key: InOutType) -> Unit = {},
    columns: Int = 4,
    textStyle: TextStyle = MaterialTheme.typography.titleLarge,
    directional: KeyPadDirectionals = KeyPadDirectionals.VERTICAL,
    disabledDirections: List<KeyDirections> = listOf()
) {
    fun onKeyPress(key: String) {
        when {
            key.matches(Regex("[0-9.]")) -> {
                appendValue(key)
            }
            key == "C" -> {
                clearValue()
            }
            key == "DEL" -> {
                popValue()
            }
            key == "ENTER" -> {
                submit()
            }
            key == "UP" || key == "DOWN" || key == "LEFT" || key == "RIGHT" -> {
                val pressedKey = when(key) {
                    "UP" -> ArrowKey.UP
                    "DOWN" -> ArrowKey.DOWN
                    "LEFT" -> ArrowKey.LEFT
                    else -> ArrowKey.RIGHT
                }
                handleArrow(pressedKey)
            }
            key == "LESS_THAN_OR_EQUAL" || key == "GREATER_THAN_OR_EQUAL" -> {
                val pressedKey = if(key == "LESS_THAN_OR_EQUAL") InOutType.LESS_THAN_EQUAL else InOutType.REST
                handleConstraint(pressedKey)
            }
        }
    }

    KeyPadLayout(
        keyPads = keypadData,
        onKeyPress = ::onKeyPress,
        modifier = modifier,
        columns = columns,
        textStyle = textStyle,
        directional = directional,
        disabledDirections = disabledDirections
    )
}

@Preview
@Composable
fun NumericNoCommaKeyPadPreview() {
    KeyPadGeneral(keypadData = CommonKeyPads)
}