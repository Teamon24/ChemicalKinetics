package com.nir.utils.math.method

import com.nir.beans.k

typealias kFunc = (f: F, x: X, y: Y) -> k
typealias k = Array<Double>
/** Количество точек в решении системы уравнения. */
typealias N = Int

/** Размерность системы */
typealias D = Int

typealias dX = X
typealias X = Double
typealias Xs = DoubleArray
typealias X0 = X

/** Y = [y1,y2,y3...yn] */
typealias Y = Array<Double>
typealias Ys = Array<Array<Double>>

typealias Y0 = Y

/**
 * @param n количество Y-векторов
 * @param d размерность Y-векторов.
 */
fun zeros(n: N, d: D): Array<Y> {
    return Array(n) { Array(d) { 0.0 } }
}

fun zeros(size: Int): Array<Double> {
    return Array(size) { 0.0 }
}



