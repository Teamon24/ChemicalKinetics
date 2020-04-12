package com.nir.utils.math.method

import com.nir.utils.math.ComputationConfigs
import com.nir.utils.math.InitialData

val emptyFunc = { _: X, _: Y -> 0.0 }

/**
 * Метод решения системы уравнений.
 */
abstract class Method {
    abstract val name: String

    abstract fun init(initialData: InitialData, computationConfig: ComputationConfigs): Method

    abstract operator fun invoke(f: F,
                                 y0: Y,
                                 x0: X,
                                 dx: dX,
                                 n: N): Array<Y>

    abstract operator fun invoke(f: F,
                                 y: Y,
                                 x: X,
                                 dx: dX): Y
}
