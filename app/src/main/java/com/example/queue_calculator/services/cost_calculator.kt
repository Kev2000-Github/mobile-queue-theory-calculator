package com.example.queue_calculator.services

import com.example.queue_calculator.utils.toFixedIfNecessary

class CostCalculator{
    fun calculateCosts(costData: CostCalculationProps): List<CostResult> {
        val result: MutableList<CostResult> = mutableListOf()
        val calculator = QueueModel()
        for(item in costData.variableData) {
            val props = ICalculationProps(
                n = 1,
                maxCapacity = costData.fixedData.maxCapacity,
                numberServers = item.numberServers,
                inOutAvg = listOf(
                    InOutAvg(
                        type = InOutType.ALL,
                        numberAnchor = 0,
                        lambda = costData.fixedData.lambda,
                        miu = item.miu
                    )
                )
            )
            val modelResult = calculator.calculateCumulativeProbabilities(props)
            if (modelResult != null){
                val ewc = costData.fixedData.waitCost * modelResult.systemLength
                val eoc = item.cost * item.numberServers

                result.add(
                    CostResult(
                        l = toFixedIfNecessary(modelResult.systemLength, DECIMAL_PLACES),
                        eoc = toFixedIfNecessary(eoc, DECIMAL_PLACES),
                        ewc = toFixedIfNecessary(ewc, DECIMAL_PLACES),
                        etc = toFixedIfNecessary(item.cost + ewc, DECIMAL_PLACES)
                    )
                )
            }
        }

        return result
    }
}
