package com.example.queue_calculator.services

import com.example.queue_calculator.services.calculation_cases.MM1Finite
import com.example.queue_calculator.services.calculation_cases.MM1General
import com.example.queue_calculator.services.calculation_cases.MM1Infinite
import com.example.queue_calculator.services.calculation_cases.MMCFinite
import com.example.queue_calculator.services.calculation_cases.MMCInfinite

class QueueModel {
    private fun getQueueType(
        n: Int,
        c: Int,
        inOutAvg: List<InOutAvg>
    ): QueueType? {
        val specializedCondition =
        inOutAvg.size == 1 && inOutAvg[0].type === InOutType.ALL

        if (
        // specialized case MM1 infinite
            specializedCondition && c == 1 && n == 0
        ) {
            return QueueType.MM1_INFINITE
        } else if (
        // specialized case MM1 finite
            specializedCondition && c == 1 && n > 0
        ) {
            return QueueType.MM1_FINITE
        } else if (
        // specialized case MMC infinite
            specializedCondition && c > 1 && n == 0
        ) {
            return QueueType.MMC_INFINITE
        } else if (
        // specialized case MMC finite
            specializedCondition && c > 1 && n > 0
        ) {
            return QueueType.MMC_FINITE
        } else if (
        // general case MM1
            this.isValidInOutAvgGeneral(inOutAvg) && c == 1
        ) {
            return QueueType.MM1_GENERAL
        } else if (
        // general case MMC
            this.isValidInOutAvgGeneral(inOutAvg) && c > 1
        ) {
            return QueueType.MMC_GENERAL
        } else {
            return null
        }
    }

    fun calculateCumulativeProbabilities(
    props: ICalculationProps
    ): CumulativeProbabilityData? {
        val (_, maxCapacity, numberServers, inOutAvg) = props
        val type = this.getQueueType(maxCapacity, numberServers, inOutAvg)
        val calculator: ICalculator?

        when (type) {
            QueueType.MM1_INFINITE -> {
                calculator = MM1Infinite()
                return calculator.calculateCumulativeProbabilities(props)
            }
            QueueType.MM1_FINITE -> {
                calculator = MM1Finite()
                return calculator.calculateCumulativeProbabilities(props)
            }
            QueueType.MMC_INFINITE -> {
                calculator = MMCInfinite()
                return calculator.calculateCumulativeProbabilities(props)
            }
            QueueType.MMC_FINITE -> {
                calculator = MMCFinite()
                return calculator.calculateCumulativeProbabilities(props)
            }
            QueueType.MM1_GENERAL -> {
                calculator = MM1General()
                return calculator.calculateCumulativeProbabilities(props)
            }
            else -> {
                return null
            }
        }
    }

    private fun countTypes(types: List<InOutType>, type: InOutType): Int {
        return types.filter { item -> item == type }.size
    }

    private fun isValidInOutAvgGeneral(inOutAvg: List<InOutAvg>): Boolean {
        val inOutAvgTypes = inOutAvg.map { item -> item.type }

        val numberOfALL = this.countTypes(inOutAvgTypes, InOutType.ALL)
        val numberOfRest = this.countTypes(inOutAvgTypes, InOutType.REST)
        val numberOfLess = this.countTypes(inOutAvgTypes, InOutType.LESS_THAN_EQUAL)

        return (numberOfALL == 0 && numberOfLess > 0 && numberOfRest < 2)
    }
}
