package com.nir.utils.math.method

operator fun dX.times(other: Array<Double>): Array<Double> {
    return other.map { it * this }.toTypedArray()
}

