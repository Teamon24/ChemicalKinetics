package com.nir.utils.math

interface RungeKuttaCore {
    operator fun invoke(k: Array<Array<Double>>,
                        f: F,
                        t: Double,
                        dt: Double,
                        coeffs: Array<Double>,
                        r: R): Array<Double>
}

class RungeKutta4Core : RungeKuttaCore {
    override operator fun invoke(k: Array<Array<Double>>,
                                 f: F,
                                 t: Double,
                                 dt: Double,
                                 coeffs: Array<Double>,
                                 r: R): Array<Double> {
        k[0] = f(r, t)
        k[1] = f(r + coeffs[1] * k[0], t + coeffs[1])
        k[2] = f(r + coeffs[1] * k[1], t + coeffs[1])
        k[3] = f(r + dt * k[2], t + dt)

        val kSum = coeffs[1] * (k[0] + 2 * k[1] + 2 * k[2] + k[3])
        return kSum
    }
}


