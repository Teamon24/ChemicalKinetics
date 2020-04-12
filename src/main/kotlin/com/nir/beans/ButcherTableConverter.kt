package com.nir.beans

import com.nir.utils.math.method.ButchersTable
import com.nir.utils.math.method.jsonPojos.ButchersTableJsonPojo
import com.nir.utils.math.Doubles
import com.nir.utils.math.Matrix

object ButcherTableConverter {

    @JvmStatic
    fun convert(butchersTableJsonPojo: ButchersTableJsonPojo): ButchersTable {
        val c = butchersTableJsonPojo.c
        val A = butchersTableJsonPojo.A
        val b = butchersTableJsonPojo.b
        val b2 = butchersTableJsonPojo.b2
        val doubledA = toDouble(A)
        val doubledB = b.map { ExpressionParser.toDouble(it!!) }.toTypedArray()
        val doubledC = c.map { ExpressionParser.toDouble(it!!) }.toTypedArray()
        val doubledBCorrect = b2.map { ExpressionParser.toDouble(it!!) }.toTypedArray()
        return ButchersTable(
                doubledC,
                Matrix(Doubles, doubledA),
                doubledB,
                doubledBCorrect
        )
    }

    private fun toDouble(A: List<*>): Array<Array<Double>> {
        return A.map { row ->
            row as List<*>
            row.map {ExpressionParser.toDouble(it!!)}.toTypedArray()
        }.toTypedArray()
    }
}