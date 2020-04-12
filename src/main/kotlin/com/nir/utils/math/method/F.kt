package com.nir.utils.math.method

/**
 * dy/dx = F - правая часть системы обыкновенных дифференциальных уравнений.
 */
open class F (vararg val f: (X, Y) -> Double) {
    val size: Int = f.size
    operator fun invoke(x: X, y: Y): Array<Double> {
        return (f.indices).map { i -> f[i](x, y) }.toTypedArray()
    }
}