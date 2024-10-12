package com.example.queue_calculator.services.calculation_cases

import com.example.queue_calculator.services.CumulativeProbabilityData
import com.example.queue_calculator.services.DECIMAL_PLACES
import com.example.queue_calculator.services.ICalculationProps
import com.example.queue_calculator.services.ICalculator
import com.example.queue_calculator.services.InOutType
import com.example.queue_calculator.services.QueueProbability
import com.example.queue_calculator.utils.toFixedIfNecessary

class MM1General: ICalculator {
    override fun calculateCumulativeProbabilities(props: ICalculationProps): CumulativeProbabilityData? {
        //Probabilities with Po multiplication pending
        val partialProbabilities: MutableList<Double> = mutableListOf(1.0)

        var index = 1
        for (item in props.inOutAvg) {
            val (type, numberAnchor, lambda, miu) = item
            if (type === InOutType.LESS_THAN_EQUAL) {
                while (index <= numberAnchor) {
                    val prevPartialProbability = partialProbabilities[index - 1]
                    partialProbabilities.add(prevPartialProbability * (lambda / miu))
                    index++
                }
            } else if (type === InOutType.REST) {
                val rho = lambda / miu
                if (rho >= 1) return null
                val prevPartialProbability = partialProbabilities[index - 1]
                val partialProbRest = prevPartialProbability * (1 / (1 - rho))
                partialProbabilities.removeLast()
                partialProbabilities.add(partialProbRest)
                break
            }
        }

        val p0 = 1 / partialProbabilities.reduce {acc, curr -> acc + curr}

        val probN: MutableList<QueueProbability> = mutableListOf(
            QueueProbability(p0, p0)
        )

        //Complete probabilities
        val lessLimits = props.inOutAvg.filter {
            item -> item.type === InOutType.LESS_THAN_EQUAL
        }
        val restLimit = props.inOutAvg.find{item -> item.type === InOutType.REST}

        index = 1
        for (item in lessLimits) {
            val (_, numberAnchor, lambda, miu) = item

            while (index <= numberAnchor) {
                val prevProbability = probN[index - 1].probability
                val probability = prevProbability * (lambda / miu)
                probN.add(
                    QueueProbability(
                        probability,
                        probN[index - 1].cumulativeProbability + probability
                    )
                )
                index++
            }
        }
        if (restLimit != null) {
            while (index < props.n) {
                val prevProbability = probN[index - 1].probability
                val probability =
                (prevProbability * restLimit.lambda) / restLimit.miu
                probN.add(
                    QueueProbability(
                        probability,
                        probN[index - 1].cumulativeProbability + probability
                    )
                )
                index++
            }
        }

        val systemLength = probN.foldIndexed(0.0) { idx, acc, curr -> acc + curr.probability * idx }
        val sysWaitingTime = 0.0
        val queueWaitingTime = 0.0
        val queueLength = 0.0

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