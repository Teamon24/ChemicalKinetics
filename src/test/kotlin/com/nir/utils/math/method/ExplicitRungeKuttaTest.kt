package com.nir.utils.math.method

import com.nir.beans.Methods
import com.nir.utils.AssertUtils
import com.nir.utils.math.ArrayUtils
import com.nir.utils.math.minus
import com.nir.utils.math.plus
import org.junit.jupiter.api.Test
import com.nir.utils.math.times

class ExplicitRungeKuttaTest {
    init {
        Methods.getAll()
    }

    private var k = ArrayUtils.twoDimArray(6 to f.size)

    @Test
    fun testRK4th1v() {
        val method = Methods.getByName("Runge-Kutta 4th-order: v.1")
        val expectedY = expctedY1(f, x0, y0, dx)
        val actualY = method.init(initialPoint, computationConfigs)(f, x0, y0, dx)
        AssertUtils.assertWithPrecision(expectedY, actualY, 1E-15)
    }

    @Test
    fun testRK4th2v() {
        val method = Methods.getByName("Runge-Kutta 4th-order: v.2")
        val expectedY = expectedY2(f, x0, y0, dx)
        val actualY = method.init(initialPoint, computationConfigs)(f, x0, y0, dx)
        AssertUtils.assertWithPrecision(expectedY, actualY, 1E-16)
    }

    @Test
    fun testRKThreeEightRule() {
        val method = Methods.getByName("3/8-rule 4th-order method")
        val expectedY = expectedY3(f, x0, y0, dx)
        val actualY = method.init(initialPoint, computationConfigs)(f, x0, y0, dx)
        AssertUtils.assertWithPrecision(expectedY, actualY, 1E-16)
    }

    @Test
    fun testRK5thOrder() {
        val method = Methods.getByName("Runge-Kutta 5th-order method")
        val expectedY = expectedY4(f, x0, y0, dx)
        val actualY = method.init(initialPoint, computationConfigs)(f, x0, y0, dx)
        AssertUtils.assertWithPrecision(expectedY, actualY, 1E-16)
    }

    private fun expctedY1(f: F, x: X, y: Y, dx: dX): Array<Double> {
        val half = dx / 2.0
        val sixth = dx / 6.0
        k[0] = f(x,          y)
        k[1] = f(x + half,   y + half * k[0])
        k[2] = f(x + half,   y + half * k[1])
        k[3] = f(x + dx,     y + dx * k[2])

        val kSum = sixth * (k[0] + 2 * k[1] + 2 * k[2] + k[3])
        return y0 + kSum
    }

    private fun expectedY2(f: F, x: X, y: Y, dx: dX): Array<Double> {
        val half = dx / 2.0
        val qaurter = 0.25 * dx
        k[0] = f(x,             y)
        k[1] = f(x + qaurter,   y + qaurter * k[0])
        k[2] = f(x + half,      y + half * k[1])
        k[3] = f(x + dx,        y + dx * k[0] - 2 * dx * k[1] + 2*dx*k[2])

        val sixth = dx / 6.0
        val kSum = sixth * (k[0] + 4 * k[2] + k[3])
        return y0 + kSum
    }

    private fun expectedY3(f: F, x: X, y: Y, dx: dX): Array<Double> {
        k[0] = f(x + 0     * dx,    y)
        k[1] = f(x + 1/3.0 * dx,    y + dx *( 1/3.0 * k[0]))
        k[2] = f(x + 2/3.0 * dx,    y + dx *(-1/3.0 * k[0] + 1 * k[1]           ))
        k[3] = f(x + 1     * dx,    y + dx *( 1     * k[0] - 1 * k[1] + 1 * k[2]))

        val kSum = dx  * (1/8.0 * k[0] + 3/8.0 * k[1] + 3/8.0 * k[2] +1/8.0 * k[3])
        return y0 + kSum
    }

    private fun expectedY4(f: F, x: X, y: Y, dx: dX): Array<Double> {
        k[0] = f(x + 0    * dx,  y + dx * ( 0.0    * k[0] + 0      * k[1] + 0.0    * k[2] + 0.0     * k[3] + 0.0   * k[4] + 0.0 * k[5]))
        k[1] = f(x + 0.25 * dx,  y + dx * ( 0.25   * k[0] + 0      * k[1] + 0.0    * k[2] + 0.0     * k[3] + 0.0   * k[4] + 0.0 * k[5]))
        k[2] = f(x + 0.25 * dx,  y + dx * ( 1/8.0  * k[0] + 1/8.0  * k[1] + 0.0    * k[2] + 0.0     * k[3] + 0.0   * k[4] + 0.0 * k[5]))
        k[3] = f(x + 0.5  * dx,  y + dx * ( 0      * k[0] + 0      * k[1] + 0.5    * k[2] + 0.0     * k[3] + 0.0   * k[4] + 0.0 * k[5]))
        k[4] = f(x + 0.75 * dx,  y + dx * ( 3/16.0 * k[0] + -3/8.0 * k[1] + 3/8.0  * k[2] + 9/16.0  * k[3] + 0.0   * k[4] + 0.0 * k[5]))
        k[5] = f(x + 1    * dx,  y + dx * ( 3/7.0  * k[0] + 8/7.0  * k[1] + 6/7.0  * k[2] - 12/7.0  * k[3] + 8/7.0 * k[4] + 0.0 * k[5]))

        val kSum = dx  * (7/90.0 * k[0] + 0.0 * k[1] + 32/90.0 * k[2] + 12/90.0 * k[3] + 32/90.0 * k[4] + 7/90.0 * k[5])
        return y0 + kSum
    }
}
