package com.nir.ui.utils

/** Time */
typealias T = Double

/** R = [x1,x2,x3...xn] */
typealias R = Array<Double>

open class F (private vararg val f: (R, T) -> Double) {
    operator fun invoke(r: R, t: T): Array<Double> {
        return (f.indices).map {
            i -> f[i](r, t)
        }.toTypedArray()
    }
}
