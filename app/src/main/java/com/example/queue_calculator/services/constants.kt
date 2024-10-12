package com.example.queue_calculator.services

enum class InOutType {
    ALL,
    LESS_THAN_EQUAL,
    REST,
}

enum class QueueType {
    MM1_FINITE,
    MM1_INFINITE,
    MMC_FINITE,
    MMC_INFINITE,
    MM1_GENERAL,
    MMC_GENERAL,
}

const val DECIMAL_PLACES = 6

data class QueueProbability (
    val probability: Double,
    val cumulativeProbability: Double
    )

data class CumulativeProbabilityData(
    val sysWaitingTime: Double,
    val queueWaitingTime: Double,
    val systemLength: Double,
    val queueLength: Double,
    val probN: List<QueueProbability>,
    val iterations: Int
)

data class ICalculationProps(
    val n: Int,
    val maxCapacity: Int,
    val numberServers: Int,
    val inOutAvg: List<InOutAvg>
)

data class InOutAvg(
    val type: InOutType,
    val numberAnchor: Int,
    val lambda: Double,
    val miu: Double
)
