package com.nir.utils.math.method

import com.nir.utils.math.ComputationConfigs
import com.nir.utils.math.InitialPoint

val emptyFunc = { _: X, _: Y -> 0.0 }

/**
 * Метод решения системы уравнений.
 */
abstract class Method {
    abstract val name: String

    abstract fun init(initialPoint: InitialPoint, computationConfig: ComputationConfigs): Method

    abstract operator fun invoke(f: F,
                                 x0: X,
                                 y0: Y,
                                 dx: dX,
                                 n: N): Array<Y>

    abstract operator fun invoke(f: F,
                                 x: X,
                                 y: Y,
                                 dx: dX): Y
}
