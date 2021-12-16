package com.nir.utils.math

typealias kFunc = (f: F, x: X, y: Y) -> k
typealias k = Array<Double>
/** Количество точек в решении системы уравнения. */
typealias N = Int

/** Размерность системы */
typealias D = Int

typealias X = Double
typealias X0 = X
typealias dX = X
typealias Xs = DoubleArray

/** Y = [y1,y2,y3...yn] */
typealias Y = Array<Double>

typealias Ys = Array<Array<Double>>
typealias Y0 = Y

/** Точка в фазовом пространстве: одно значение x и соотвествующее решение - вектор значений y. */
typealias XY = Pair<X,Y>

/** Набот точек в фазовом пространстве: ряд значений x[<SUB>i</SUB>] и соотвествующие каждому из этих значений вектора y[<SUB>i</SUB>]. */
typealias XsYs = Pair<DoubleArray, Array<DoubleArray>>

/**
 * @param n количество Y-векторов.
 * @param d размерность Y-векторов.
 */
fun zeros(n: N, d: D): Array<Y> {
    return Array(n) { Array(d) { 0.0 } }
}

fun zeros(size: Int): Array<Double> {
    return Array(size) { 0.0 }
}



