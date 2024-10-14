package com.example.queue_calculator.schemas

import com.example.queue_calculator.services.ICalculationProps
import io.konform.validation.Validation
import io.konform.validation.jsonschema.minimum

val validateMMCInfiniteProps = Validation<ICalculationProps> {
    ICalculationProps::inOutAvg onEach {
        addConstraint("lambda debe ser mayor a 0") {
            it.lambda > 0
        }
        addConstraint("miu debe ser mayor a 0") {
            it.miu > 0
        }
    }

    ICalculationProps::numberServers {
        minimum(1) hint "Debe haber al menos un servidor"
    }

    addConstraint("La tasa de servicio * numero de servidores debe ser mayor a la tasa de llegada") {
        val isValidRate = it.inOutAvg.all { inout -> inout.lambda < inout.miu * it.numberServers }
        return@addConstraint isValidRate
    }
}
