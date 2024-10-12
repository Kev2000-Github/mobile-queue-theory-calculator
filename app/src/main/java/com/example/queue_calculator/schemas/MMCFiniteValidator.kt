package com.example.queue_calculator.schemas

import com.example.queue_calculator.services.ICalculationProps
import io.konform.validation.Validation
import io.konform.validation.jsonschema.minimum

val validateMMCFiniteProps = Validation {
    ICalculationProps::maxCapacity required {
        minimum(1) hint "La capacidad maxima no puede ser menor a 1"
    }
    ICalculationProps::numberServers {
        minimum(1) hint "Debe haber al menos un servidor"
    }
    ICalculationProps::inOutAvg onEach {
        addConstraint("lambda debe ser mayor a 0") {
            it.lambda > 0
        }
        addConstraint("miu debe ser mayor a 0") {
            it.miu > 0
        }
    }
}