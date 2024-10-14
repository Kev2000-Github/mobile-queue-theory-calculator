package com.example.queue_calculator.services.calculation_cases

import com.example.queue_calculator.services.CumulativeProbabilityData
import com.example.queue_calculator.services.DECIMAL_PLACES
import com.example.queue_calculator.services.ICalculationProps
import com.example.queue_calculator.services.ICalculator
import com.example.queue_calculator.services.QueueProbability
import com.example.queue_calculator.utils.factorial
import com.example.queue_calculator.utils.toFixedIfNecessary
import kotlin.math.pow

class MMCFinite: ICalculator {
    override fun calculateCumulativeProbabilities(props: ICalculationProps): CumulativeProbabilityData {
        val (n, maxCapacity, numberServers, inOutAvg) = props
        val lambda = inOutAvg[0].lambda
        val miu = inOutAvg[0].miu

        val rho = lambda / miu

        val rhoC = rho / numberServers
        val helper = maxCapacity - numberServers + 1.0

        val lastOperandP0 = if (rhoC == 1.0) {
            helper
        } else {
            (1 - rhoC.pow(helper)) / (1 - rhoC)
        }
        var p0 = 0.0
        for (i in 0 until numberServers) {
            p0 += rho.pow(i) / factorial(i.toDouble())
        }
        p0 +=
            (rho.pow(numberServers) / factorial(numberServers.toDouble())) *
                    lastOperandP0
        p0 = 1 / p0
        val probN: MutableList<QueueProbability> = mutableListOf(
            QueueProbability(p0, p0)
        )
        for (i in 1 .. n) {
            val currentProb = when {
                i < numberServers -> (rho.pow(i) / factorial(i.toDouble())) * p0
                i in numberServers .. maxCapacity -> (rho.pow(i) /
                        (factorial(numberServers.toDouble()) * numberServers.toDouble().pow(i - numberServers))) * p0
                else -> 0.0
            }

            probN.add(
                QueueProbability(currentProb, probN[i - 1].cumulativeProbability + currentProb)
            )
        }

        val lambdaPer = lambda * probN[maxCapacity].probability
        val lambdaEff = lambda - lambdaPer
        val rhoEff = lambdaEff / miu
        val queueLength = when(rhoC) {
            1.0 -> (rho.pow(numberServers) * (maxCapacity - numberServers) * helper * p0) / factorial(2.0 * numberServers)
            else -> (rho.pow(numberServers + 1) /
                    (factorial(numberServers - 1.0) * (numberServers - rho).pow(2))) *
                    (1 - rhoC.pow(helper) - helper * (1 - rhoC) * rhoC.pow(maxCapacity - numberServers)) * p0
        }
        val systemLength = queueLength + rhoEff
        val sysWaitingTime = systemLength / lambdaEff
        val queueWaitingTime = queueLength / lambdaEff

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