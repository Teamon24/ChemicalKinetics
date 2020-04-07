package com.nir.utils.math

/** Time */
typealias T = Double

/** R = [x1,x2,x3...xn] */
typealias R = Array<Double>

/**
 * dr/dt = F
 */
open class System (vararg val f: (T, R) -> Double) {
    val size: Int = f.size
    operator fun invoke(t: T, r: R): Array<Double> {
        return (f.indices).map { i -> f[i](t, r) }.toTypedArray()
    }
}

val emptyFunc = { _:T, _: R -> 0.0 }
val emptyKFunc = { system: System, x: X, y: Y -> Array(y.size) { 0.0 } }

