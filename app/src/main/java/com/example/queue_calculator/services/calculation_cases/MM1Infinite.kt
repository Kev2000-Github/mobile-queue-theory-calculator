package com.example.queue_calculator.services.calculation_cases

import com.example.queue_calculator.services.CumulativeProbabilityData
import com.example.queue_calculator.services.DECIMAL_PLACES
import com.example.queue_calculator.services.ICalculationProps
import com.example.queue_calculator.services.ICalculator
import com.example.queue_calculator.services.QueueProbability
import com.example.queue_calculator.utils.toFixedIfNecessary
import kotlin.math.pow

class MM1Infinite: ICalculator {
    override fun calculateCumulativeProbabilities(props: ICalculationProps): CumulativeProbabilityData {
        val (n, _, _, inOutAvg) = props
        val lambda = inOutAvg[0].lambda
        val miu = inOutAvg[0].miu

        val rho = lambda / miu
        val p0 = 1 - rho
        val systemLength = rho / p0
        val sysWaitingTime = 1 / (miu - lambda)
        val queueWaitingTime = sysWaitingTime - 1 / miu
        val queueLength = rho.pow(2) / p0
        val probN: MutableList<QueueProbability> = mutableListOf(
            QueueProbability(p0, p0)
        )
        for (i in 1..n) {
            val probI = p0 * rho.pow(i)
            probN.add(
                QueueProbability(probI, probN[i - 1].cumulativeProbability + probI)
            )
        }

        return CumulativeProbabilityData(
            toFixedIfNecessary(sysWaitingTime, DECIMAL_PLACES),
            toFixedIfNecessary(queueWaitingTime, DECIMAL_PLACES),
            toFixedIfNecessary(systemLength, DECIMAL_PLACES),
            toFixedIfNecessary(queueLength, DECIMAL_PLACES),
            probN.map { p ->
                QueueProbability(
                    toFixedIfNecessary(p.probability, DECIMAL_PLACES),
                    toFixedIfNecessary(p.cumulativeProbability, DECIMAL_PLACES)
                )
            },
            props.n
        )
    }
}