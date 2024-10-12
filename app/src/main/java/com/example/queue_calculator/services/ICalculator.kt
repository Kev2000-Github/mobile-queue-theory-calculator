package com.example.queue_calculator.services

interface ICalculator {
    fun calculateCumulativeProbabilities(
    props: ICalculationProps
    ): CumulativeProbabilityData?
}
