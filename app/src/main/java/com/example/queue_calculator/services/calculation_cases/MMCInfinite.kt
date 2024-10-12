package com.example.queue_calculator.services.calculation_cases

import com.example.queue_calculator.services.CumulativeProbabilityData
import com.example.queue_calculator.services.DECIMAL_PLACES
import com.example.queue_calculator.services.ICalculationProps
import com.example.queue_calculator.services.ICalculator
import com.example.queue_calculator.services.QueueProbability
import com.example.queue_calculator.utils.factorial
import com.example.queue_calculator.utils.toFixedIfNecessary
import kotlin.math.pow

class MMCInfinite: ICalculator {
    override fun calculateCumulativeProbabilities(props: ICalculationProps): CumulativeProbabilityData {
        val (n, _, numberServers, inOutAvg) = props
        val lambda = inOutAvg[0].lambda
        val miu = inOutAvg[0].miu

        val rho = lambda / miu

        var p0 = 0.0
        for (i in 0 until numberServers) {
            p0 += rho.pow(i) / factorial(i.toDouble())
        }
        p0 +=
            (rho.pow(numberServers) / factorial(numberServers.toDouble())) * (1 / (1 - rho / numberServers))
        p0 = 1 / p0

        val queueLength =
        (p0 * rho.pow(numberServers + 1)) /
                (factorial(numberServers - 1.0) * (numberServers - rho).pow(2))
        val systemLength = queueLength + rho
        val sysWaitingTime = systemLength / lambda
        val queueWaitingTime = queueLength / lambda
        val probN: MutableList<QueueProbability> = mutableListOf(
            QueueProbability(p0, p0)
        )
        for (i in 1 .. n) {
            val currentProb = when {
                i < numberServers -> (rho.pow(i.toDouble()) * p0) / factorial(i.toDouble())
                else -> (rho.pow(i.toDouble()) * p0) /
                        (factorial(numberServers.toDouble()) * numberServers.toDouble().pow(i - numberServers))
            }

            probN.add(
                QueueProbability(currentProb, probN[i - 1].cumulativeProbability + currentProb)
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