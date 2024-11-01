package com.example.queue_calculator.schemas

import com.example.queue_calculator.services.ICalculationProps
import io.konform.validation.Validation

val validateMM1InfiniteProps = Validation {
    ICalculationProps::inOutAvg onEach {
        addConstraint("La tasa de servicio debe ser mayor a la tasa de llegada") {
            it.lambda < it.miu
        }
        addConstraint("lambda debe ser mayor a 0") {
            it.lambda > 0
        }
        addConstraint("miu debe ser mayor a 0") {
            it.miu > 0
        }
    }
}
