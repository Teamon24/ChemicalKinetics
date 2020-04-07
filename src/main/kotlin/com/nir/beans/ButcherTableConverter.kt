package com.nir.beans

import com.nir.utils.math.method.ButchersTable
import com.nir.utils.math.method.ButchersTableJsonPojo
import com.nir.utils.math.Doubles
import com.nir.utils.math.Matrix

object ButcherTableConverter {
    fun convert(butchersTableJsonPojo: ButchersTableJsonPojo): ButchersTable {
        val c = butchersTableJsonPojo.c
        val A = butchersTableJsonPojo.A
        val b = butchersTableJsonPojo.b
        val bCorrect = butchersTableJsonPojo.bCorrect
        val doubledA = toDouble(A)
        val doubledB = b.map { toDouble(it) }.toTypedArray()
        val doubledC = c.map { toDouble(it) }.toTypedArray()
        val doubledBCorrect = bCorrect.map { toDouble(it) }.toTypedArray()
        return ButchersTable(
                doubledC,
                Matrix(Doubles, doubledA),
                doubledB,
                doubledBCorrect
        )
    }
    fun toDouble(A: List<*>): Array<Array<Double>> {
        val toTypedArray = A.map { row ->
            row as List<*>
            row.map { element ->
                toDouble(element)
            }.toTypedArray()
        }.toTypedArray()
        return toTypedArray
    }

    fun toDouble(any: Any?) : Double {
        return if (any is String) {
            MethodCreator.ExpressionParser.parse(any)
        } else {
            return when (any) {
                is Int -> any.toDouble()
                is Double -> any
                is Float -> any.toDouble()
                else -> throw RuntimeException("element: $any should be primitive")
            }
        }
    }
}