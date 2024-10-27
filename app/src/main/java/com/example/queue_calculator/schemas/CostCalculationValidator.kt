package com.example.queue_calculator.schemas

import com.example.queue_calculator.services.CostCalculationProps
import io.konform.validation.Validation

val validateCostCalculationProps = Validation<CostCalculationProps> {
    CostCalculationProps::fixedData required {
        addConstraint("El costo fijo debe ser mayor a 0") {
            it.waitCost > 0
        }
        addConstraint("La capacidad maxima debe ser 0 o mayor") {
            it.maxCapacity >= 0
        }
        addConstraint("La tasa de llegada debe ser mayor a 0") {
            it.lambda > 0
        }
    }

    CostCalculationProps::variableData onEach {
        addConstraint("El costo de servicio debe ser mayor a 0") {
            it.cost > 0
        }
        addConstraint("La tasa de servicio debe ser mayor a 0") {
            it.miu > 0
        }
        addConstraint("El numero de servidores debe ser mayor a 0") {
            it.numberServers > 0
        }
    }

    addConstraint("La tasa de servicio * numero de servidores debe ser mayor a la tasa de llegada") {
        val isValidRate = it.variableData.all { item -> it.fixedData.lambda < item.miu * item.numberServers }
        return@addConstraint isValidRate
    }
}