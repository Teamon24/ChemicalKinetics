package com.nir.utils.math.method

import com.nir.beans.Methods
import com.nir.utils.Fivefold
import com.nir.utils.array
import com.nir.utils.math.ComputationConfigs
import com.nir.utils.math.InitialPoint
import com.nir.utils.math.minus
import com.nir.utils.math.plus
import com.nir.utils.math.times
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class AdamsBashforthMethodsTest {

    init {
        Methods.getAll()
    }

    private val f = F(
            { _, y -> y[0] * y[0] + y[1] + y[2] },
            { _, y -> y[1] * y[1] + y[0] + y[2] },
            { _, y -> y[2] * y[2] + y[0] + y[1] }
    )

    private val y0 = arrayOf(1.00, 1.00, 1.00)
    private val x0 = 5.0
    private val dx = 0.01
    private val initialPoint = InitialPoint(x0, y0)
    private val computationConfigs = ComputationConfigs(dx, 1)

    private val adamBashforth_1st = adamsBashforth(1)
    private val adamBashforth_2nd = adamsBashforth(2)
    private val adamBashforth_3rd = adamsBashforth(3)
    private val adamBashforth_4th = adamsBashforth(4)
    private val adamBashforth_5th = adamsBashforth(5)

    @Test
    fun test1stOrder() {
        val y1 = getExpectedValues(dx, x0, y0).array()[0]
        val actualY1 = adamBashforth_1st(f, x0, y0, dx)
        Assertions.assertArrayEquals(y1, actualY1)
    }

    @Test
    fun test2ndOrder() {
        val y2 = getExpectedValues(dx, x0, y0).array()[1]
        val actualY2 = adamBashforth_2nd(f, x0, y0, dx)
        Assertions.assertArrayEquals(y2, actualY2)
    }

    @Test
    fun test3rdOrder() {
        val y3 = getExpectedValues(dx, x0, y0).array()[2]
        val actualY3 = adamBashforth_3rd(f, x0, y0, dx)
        Assertions.assertArrayEquals(y3, actualY3)
    }

    @Test
    fun test4rdOrder() {
        val y4 = getExpectedValues(dx, x0, y0).array()[3]
        val actualY4 = adamBashforth_4th(f, x0, y0, dx)
        Assertions.assertArrayEquals(y4, actualY4)
    }

    @Test
    fun test5rdOrder() {
        val y5 = getExpectedValues(dx, x0, y0).array()[4]
        val actualY5 = adamBashforth_5th(f, x0, y0, dx)
        Assertions.assertArrayEquals(y5, actualY5)
    }


    private fun adamsBashforth(order: Int) = AdamsBashforthMethods[order].init(initialPoint, computationConfigs)

    private fun getExpectedValues(dx: dX, x0: X0, y0: Y0): Fivefold<Y, Y, Y, Y, Y> {
        fun x(i: Int) = x0 + i * dx
        val f0 = f(x0, y0)
        val y1 = y0 + dx * f0
        val f1 = f(x(1), y1)
        val y2 = y1 + dx / 2.0 * (3 * f1 - f0)
        val f2 = f(x(2), y2)
        val y3 = y2 + dx * (23.0 / 12.0 * f2 - 4.0 / 3.0 * f1 + 5.0 / 12.0 * f0)
        val f3 = f(x(3), y3)
        val y4 = y3 + dx * (55 / 24.0 * f3 - 59 / 24.0 * f2 + 37 / 24.0 * f1 - 9 / 24.0 * f0)
        val f4 = f(x(4), y4)
        val y5 = y4 + dx * (1901 / 720.0 * f4 - 2774 / 720.0 * f3 + 2616 / 720.0 * f2 - 1274 / 720.0 * f1 + 251 / 720.0 * f0)
        return Fivefold(y1, y2, y3, y4, y5)
    }
}
