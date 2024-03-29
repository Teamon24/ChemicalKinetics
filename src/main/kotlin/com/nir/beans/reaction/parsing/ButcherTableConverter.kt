package com.nir.beans.reaction.parsing

import com.nir.beans.method.ButchersTable
import com.nir.beans.method.jsonPojos.ButchersTableJsonPojo
import com.nir.utils.math.Doubles
import com.nir.utils.math.Matrix2

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
                Matrix2(Doubles, doubledA),
                doubledB,
                doubledBCorrect
        )
    }

    private fun toDouble(A: List<*>): Array<Array<Double>> {
        return A.map { row ->
            row as List<*>
            row.map { ExpressionParser.toDouble(it!!) }.toTypedArray()
        }.toTypedArray()
    }
}