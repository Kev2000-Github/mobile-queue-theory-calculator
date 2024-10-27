package com.example.queue_calculator.schemas

import com.example.queue_calculator.services.FixedCostCalculationProps
import io.konform.validation.Validation
import io.konform.validation.jsonschema.minimum

val validateFixedCostCalculationProps = Validation {
    FixedCostCalculationProps::maxCapacity required {
        minimum(0) hint "La capacidad maxima no puede ser menor a 0"
    }
    FixedCostCalculationProps::lambda required {
        addConstraint("lambda debe ser mayor a 0") {
            it > 0
        }
    }
    FixedCostCalculationProps::waitCost required {
        addConstraint("El costo de espera debe ser mayor a 0") {
            it > 0
        }
    }
}