package com.nir.utils

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

operator fun Double.times(doubles: DoubleArray): DoubleArray {
    return doubles.map { it * this}.toDoubleArray()
}

operator fun Double.times(doubles: List<Double>): List<Double> {
    return doubles.map { it * this}
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

operator fun Array<Double>.times(array: DoubleArray): Array<Double> {
    val thisSize = size
    val arraySize = array.size
    if (thisSize != arraySize) throw RuntimeException("Two arrays could not be timed in case of sizes inequality")
    val result = Array(thisSize) {0.0}
    for (i in 0 until arraySize) {
        result[i] = this[i] * array[i]
    }
    return result
}

operator fun DoubleArray.times(array: DoubleArray): Array<Double> {
    val thisSize = size
    val arraySize = array.size
    if (thisSize != arraySize) throw RuntimeException("Two arrays could not be timed in case of sizes inequality")
    val result = Array(thisSize) {0.0}
    for (i in 0 until arraySize) {
        result[i] = this[i] * array[i]
    }
    return result
}

operator fun Array<Double>.times(array: Array<Array<Double>>): Array<Double> {
    val thisSize = size
    val arraySize = array.size
    if (thisSize != arraySize) throw RuntimeException("Array and Arrays could not be timed in case of sizes inequality")

    val arrays =
            this.zip(array)
                .map { it.first * it.second }
                .reduce { acc, doubleArray -> acc + doubleArray }

    return arrays
}

/**
 * a1...a5 - vectors
 * Times: [1.0, 2.0, 3.0, 4.0] * [a1, a2, a3, a4] -> [1.0 * a1 + 2.0 * a2 + 3.0 * a3 + 4.0 * a4] -> a5
 */
operator fun DoubleArray.times(arrays: Array<DoubleArray>): DoubleArray {

    val thisSize = size
    val arraysSize = arrays.size

    if (arraysSize == 0) {
        throw RuntimeException("Arrays should not be empty")
    }

    if (thisSize != arraysSize) {
        throw RuntimeException("Array and Arrays could not be timed in case of sizes inequality")
    }

    for (i in 1 until arraysSize) {
        if (arrays[i-1].size != arrays[i].size) {
            throw RuntimeException("Double Arrays should be all the same size")
        }
    }

    val arraySize = arrays[0].size

    val result = Array(arraysSize) { DoubleArray(arraySize) }

    for (i in 0 until arraysSize) {
        result[i] = this[i] * arrays[i]
    }

    val arrays = result.reduce { acc, doubleArray -> acc + doubleArray }

    return arrays
}

/**
 * a1...a5 - vectors
 * Times: [1.0, 2.0, 3.0, 4.0] * [a1, a2, a3, a4] -> [1.0 * a1 + 2.0 * a2 + 3.0 * a3 + 4.0 * a4] -> a5
 */
operator fun List<Double>.times(arrays: List<Array<Double>>): Array<Double> {

    val thisSize = size
    val arraysSize = arrays.size

    if (arraysSize == 0) {
        throw RuntimeException("Arrays should not be empty")
    }

    if (thisSize != arraysSize) {
        throw RuntimeException("Array and Lists could not be timed in case of sizes inequality")
    }

    for (i in 1 until arraysSize) {
        if (arrays[i-1].size != arrays[i].size) {
            throw RuntimeException("Arrays of doubles should be all the same size")
        }
    }

    val result = ArrayList<Array<Double>>(arraysSize)

    for (i in 0 until arraysSize) {
        result.add(this[i] * arrays[i])
    }

    val arrays = result.reduce { acc, doubles -> acc + doubles }

    return arrays
}

