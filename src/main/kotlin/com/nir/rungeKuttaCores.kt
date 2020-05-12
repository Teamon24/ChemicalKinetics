package com.nir

interface RungeKuttaCore {
    operator fun invoke(mySystem: MySystem,
                        r: R,
                        t: Double,
                        dt: Double): Array<Double>
}

class RungeKutta4Core
constructor(initialData: InitialData) : RungeKuttaCore {
    private val methodOrder = 4

    private var k: Array<K>
    private var sixth: Double
    private var half: Double

    init {
        val dimension = initialData.r0.size
        val dt = initialData.dt
        k = init(methodOrder, dimension)
        half = dt / 2.0
        sixth = dt / 6.0
    }

    override operator fun invoke(mySystem: MySystem,
                                 r: R,
                                 t: Double,
                                 dt: Double): Array<Double> {
        k[0] = mySystem(t, r)
        k[1] = mySystem(t + half, r + half * k[0])
        k[2] = mySystem(t + half, r + half * k[1])
        k[3] = mySystem(t + dt, r + dt * k[2])

        val kSum = sixth * (k[0] + 2 * k[1] + 2 * k[2] + k[3])
        return kSum
    }
}
private fun init(N: Int, D: Int) = ArrayUtils.twoDimArray(N to D)



