package com.nir.utils.math.method.hardcoded

import com.nir.utils.math.method.D
import com.nir.utils.math.method.F
import com.nir.utils.math.method.X
import com.nir.utils.math.method.Y
import com.nir.utils.math.method.dX
import com.nir.utils.math.minus
import com.nir.utils.math.plus
import com.nir.utils.math.times

class RungeKutta5Core: RungeKuttaCore {
    private val stages = 6

    private lateinit var k: Array<Array<Double>>
    private var `1-2`      = 0.0
    private var `1-4`    = 0.0
    private var `1-6`    = 0.0
    private var `1-7`    = 0.0
    private var `1-8`    = 0.0
    private var `1-16`   = 0.0
    private var `1-90`   = 0.0
    private var `3-4`  = 0.0
    private var `3-7`  = 0.0
    private var `3-8`  = 0.0
    private var `3-16` = 0.0
    private var `6-7` = 0.0
    private var `8-7` = 0.0
    private var `12-7` = 0.0
    private var `9-16`  = 0.0
    private var `7-90`  = 0.0
    private var `12-90`  = 0.0
    private var `32-90`  = 0.0


    override fun setUp(d: D, dx: dX) {
        k = init(stages, d)
        `1-2` =  1 / 2.0 * dx
        `1-4`  = 1 / 4.0 * dx
        `1-6`  = 1 / 6.0 * dx
        `1-7`  = 1 / 7.0 * dx
        `1-8`  = 1 / 8.0 * dx
        `1-16` = 1 / 16.0 * dx
        `1-90` = 1 / 90.0 * dx

        `3-4`  = 3 * `1-4`
        `3-7`  = 3 * `1-7`
        `3-8`  = 3 * `1-8`
        `3-16` = 3 * `1-16`

        `6-7`   = 6 * `1-7`
        `8-7` = 8 * `1-7`

        `12-7` = 12 * `1-7`

        `9-16` = 9 * `1-16`
        `7-90`  = 7 * `1-90`
        `12-90`  = 12 * `1-90`
        `32-90`  = 32 * `1-90`
    }

    override operator fun invoke(f: F, y: Y, x: X, dx: dX): Array<Double> {
        k[0] = f(x,         y)
        k[1] = f(x + `1-4`, y + `1-4`  * k[0])
        k[2] = f(x + `1-4`, y + `1-8`  * (k[0] + k[1] ))
        k[3] = f(x + `1-2`, y + `1-2`  * k[2])
        k[4] = f(x + `3-4`, y + `3-16` * k[0] - `3-8` * k[1] + `3-8` * k[2] + `9-16` * k[3])
        k[5] = f(x + dx,    y + `3-7`  * k[0] + `8-7` * k[1] + `6-7`  * k[2] - `12-7`  * k[3] + `8-7` * k[4])

        val kSum = `7-90` * k[0] + `32-90` * k[2] + `12-90` * k[3] + `32-90` * k[4] + `7-90` * k[5]
        return kSum
    }
}