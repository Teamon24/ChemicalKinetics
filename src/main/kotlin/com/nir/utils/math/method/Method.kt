package com.nir.utils.math.method

/** Количество точек в решении системы уравнения. */
typealias N = Int

/** Размерность системы */
typealias D = Int

typealias dX = X
typealias X = Double
typealias X0 = X

/** Y = [y1,y2,y3...yn] */
typealias Y = Array<Double>
typealias Y0 = Y

/**
 * dy/dx = F
 */
open class F (vararg val f: (X, Y) -> Double) {
    val size: Int = f.size
    operator fun invoke(x: X, y: Y): Array<Double> {
        return (f.indices).map { i -> f[i](x, y) }.toTypedArray()
    }
}

val emptyFunc = { _: X, _: Y -> 0.0 }
val emptyKFunc = { f: F, x: X, y: Y -> Array(y.size) { 0.0 } }

/**
 * Метод решения системы уравнений.
 */
abstract class Method {
    abstract val name: String

    abstract fun set(dx: dX)

    abstract operator fun invoke(f: F,
                                 y0: Y,
                                 x0: X,
                                 dx: dX,
                                 n: N): Array<Y>

    abstract operator fun invoke(f: F,
                                 y: Y,
                                 x: X,
                                 dx: dX): Y

    companion object {
        fun from(explicit: ExplicitRungeKutta): Method {
            return object : Method() {

                override val name: String get() = explicit.name

                override fun set(dx: dX) {
                    explicit.set(dx)
                }

                override fun invoke(f: F, y0: Y0, x0: X0, dx: dX, n: N): Array<Y> {
                    return explicit(f, y0, x0, dx, n)
                }

                override fun invoke(f: F, y: Y, x: X, dx: dX): Y {
                    return explicit(f, y, x, dx)
                }
            }
        }
    }
}

abstract class GeneralMethod : Method() {
    abstract fun setParamsValues(paramsAndValues: HashMap<String, Double>)
}


