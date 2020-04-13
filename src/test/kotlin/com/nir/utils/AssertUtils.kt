package com.nir.utils

import org.junit.jupiter.api.Assertions

object AssertUtils {
    fun assertWithPrecision(expectedDouble: Array<Double>,
                            actualDoubles: Array<Double>,
                            precision: Double
    ) {
        expectedDouble.zip(actualDoubles).forEach {
            val expected = it.first
            val actual = it.second
            Assertions.assertEquals(expected, actual, precision)
        }
    }
}
