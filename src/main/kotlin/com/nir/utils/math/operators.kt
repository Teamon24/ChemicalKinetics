package com.nir.utils.math

operator fun Double.times(other: Array<Double>): Array<Double> {
    return other.map { it * this }.toTypedArray()
}

operator fun Int.times(other: Array<Double>): Array<Double> {
    return other.map { it * this }.toTypedArray()
}

operator fun Array<Double>.plus(elements: Array<Double>): Array<Double> {
    val thisSize = size
    val arraySize = elements.size
    if (thisSize != arraySize) throw RuntimeException("Two arrays could not be summed in case of sizes inequality")
    return this.zip(elements).map { it.first + it.second }.toTypedArray()
}