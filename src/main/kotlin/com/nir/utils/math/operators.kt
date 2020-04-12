package com.nir.utils.math

/***       ***/
/*** MINUS ***/
/***       ***/
operator fun Array<Double>.minus(array: Array<Double>): Array<Double> {
    val thisSize = size
    val arraySize = array.size
    if (thisSize != arraySize) throw RuntimeException("Two arrays could not be summed in case of sizes inequality")
    return this.zip(array).map { it.first - it.second }.toTypedArray()
}

/***      ***/
/*** PLUS ***/
/***      ***/
operator fun Array<Double>.plus(array: Array<Double>): Array<Double> {
    val thisSize = size
    val arraySize = array.size
    if (thisSize != arraySize) throw RuntimeException("Two arrays could not be summed in case of sizes inequality")
    return this.zip(array).map { it.first + it.second }.toTypedArray()
}

/***       ***/
/*** TIMES ***/
/***       ***/
operator fun Double.times(other: Array<Double>): Array<Double> {
    return other.map { it * this }.toTypedArray()
}

operator fun Int.times(other: Array<Double>): Array<Double> {
    return other.map { it * this }.toTypedArray()
}

operator fun Array<Double>.times(array: Array<Double>): Array<Double> {
    val thisSize = size
    val arraySize = array.size
    if (thisSize != arraySize) throw RuntimeException("Two arrays could not be timed in case of sizes inequality")
    return this.zip(array).map { it.first * it.second }.toTypedArray()
}

operator fun Array<Double>.times(array: Array<Array<Double>>): Array<Double> {
    val thisSize = size
    val arraySize = array.size
    if (thisSize != arraySize) throw RuntimeException("Array and Arrays could not be timed in case of sizes inequality")

    val arrays = this.zip(array)
                    .map { it.first * it.second }
                    .reduce { acc, doubleArray -> acc + doubleArray }
    return arrays
}