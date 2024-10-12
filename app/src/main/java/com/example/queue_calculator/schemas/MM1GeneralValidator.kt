package com.example.queue_calculator.schemas

import com.example.queue_calculator.services.ICalculationProps
import com.example.queue_calculator.services.InOutType
import io.konform.validation.Validation
import io.konform.validation.jsonschema.minItems

val validateMM1GeneralProps = Validation {
    ICalculationProps::inOutAvg onEach  {
        addConstraint("lambda debe ser mayor a 0") {
            return@addConstraint it.lambda > 0
        }
        addConstraint("miu debe ser mayor a 0") {
            return@addConstraint it.miu > 0
        }
    }

    ICalculationProps::inOutAvg required {
        minItems(1)
        addConstraint("el numero de la restriccion no puede repetirse") {
            var number = -1
            it.forEach { inOutAvg ->
                if (number == inOutAvg.numberAnchor) {
                    return@addConstraint false
                }
                number = inOutAvg.numberAnchor
            }
            return@addConstraint true
        }
        addConstraint("Solo puede haber una restriccion mayor o igual") {
            val greaterThanEqual = it.filter { inOutAvg -> inOutAvg.type == InOutType.REST }
            return@addConstraint greaterThanEqual.size <= 1
        }
        addConstraint("miu debe ser mayor a lambda en la restriccion mayor o igual que") {
            val greaterThanEqual = it.find { inOutAvg -> inOutAvg.type == InOutType.REST }
            if (greaterThanEqual == null) {
                return@addConstraint true
            }
            else {
                return@addConstraint greaterThanEqual.miu > greaterThanEqual.lambda
            }
        }
        addConstraint("la restriccion mayor o igual que debe ser el ultimo") {
            val greaterThanEqual = it.find { inOutAvg -> inOutAvg.type == InOutType.REST }
            if (greaterThanEqual == null) {
                return@addConstraint true
            }
            else {
                return@addConstraint it.last() == greaterThanEqual
            }
        }
    }
}