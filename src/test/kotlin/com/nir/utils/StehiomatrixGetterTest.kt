package com.nir.utils

import com.nir.beans.k
import com.nir.beans.StehiomatrixGetter
import com.nir.ui.pojos.Compound
import com.nir.ui.pojos.Compounds
import com.nir.ui.pojos.ElementsAndAmounts
import com.nir.ui.pojos.Reaction
import com.nir.ui.pojos.Stage
import com.nir.utils.math.Integers
import com.nir.utils.math.R
import com.nir.utils.math.Matrix
import org.junit.Assert
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import kotlin.math.pow

/**
 * Тест для класса [StehiomatrixGetter].
 */
class StehiomatrixGetterTest {

    /** Тест для метода [StehiomatrixGetter.getMatrix]. */
    @Test
    fun testGetMatrix() {
        datasForTestGetMatrix().forEach { data ->
            val expectedCompounds = data[0] as List<String>
            val expectedStehiomatrix = data[1] as Matrix<Integer>
            val stages = data[2] as Reaction
            val actualCompounds = StehiomatrixGetter.getCompounds(stages)
            val actualMatrix = StehiomatrixGetter.getMatrix(stages)

            Assert.assertArrayEquals(expectedCompounds.toTypedArray(), actualCompounds.toTypedArray())
            Assertions.assertIterableEquals(expectedStehiomatrix.elements, actualMatrix.elements)
        }
    }

    @Test
    fun testGetRates() {
        datasForTestGetRates().forEach {
            val reaction = it[0] as Reaction
            val k = it[1] as Array<Double>
            val r = it[2] as R
            val expectedRatesValues = it[3] as Array<Double>
            val rates = StehiomatrixGetter.getRates(reaction, k)
            val result = rates.elements.map { action -> action(0.0, r) }
            Assert.assertArrayEquals(expectedRatesValues, result.toTypedArray())
        }
    }


    @Test
    //TODO проверить значения F после подстановки r0 и t0
    fun testSystemCreation() {
        datasForCheck().forEach {
            val stages = it[0] as Reaction
            val k = it[1] as k
            val size = it[2] as Int
            val stehiomatrix = StehiomatrixGetter.getMatrix(stages)
            val rates = StehiomatrixGetter.getRates(stages, k)
            val transposed = stehiomatrix.transpose()
            val system = StehiomatrixGetter.times(transposed, rates)
            Assert.assertEquals(size, system.size)
        }
    }

    val stehCoeff1 = Quintuple(1, 2, 3, 4, 5)
    private val k1 = Quintuple(1.0, 1.0, 1.0, 1.0, 1.0)
    lateinit var w1: Array<Double>
    private val r10 = Quintuple(2.0, 3.0, 4.0, 5.0, 6.0)

    private val stehCoeff2 = Quintuple(5, 4, 3, 2, 1)
    private val k2 = Pair(1.0, 1.0)
    lateinit var w2: Array<Double>
    private val r20 = Quadruple(2.0, 3.0, 5.0, 7.0)


    private fun datasForCheck(): Array<Array<out Any>> {
        datasForTestGetRates()
        val coeff1 = stehCoeff1.array()
        val transpose = StehiomatrixGetter.getMatrix(reaction1()).transpose()
        val expectedValues1 = arrayOf(
                -coeff1[0] * w1[0] + coeff1[3] * w1[2] + coeff1[0]
        )

        val coeff2 = stehCoeff2.array()
        val expectedValues2 = arrayOf(
                -coeff2[0] * w2[0] - coeff2[1] * w2[1],
                -coeff2[2] * w2[0],
                coeff2[3] * w2[0] - coeff2[3] * w2[1],
                coeff2[4] * w2[1]
        )

        return arrayOf(
                arrayOf(reaction1(), k1.array(), r10.array().size, expectedValues1),
                arrayOf(reaction2(), k2.array(), r20.array().size, expectedValues2)
        )
    }

    private fun datasForTestGetMatrix(): List<Array<Any>> {

        val reaction1 = reaction1()
        val (expectedCompounds1, expectedStehiomatrix1) = createDatas(reaction1)
        val reaction2 = reaction2()
        val (expectedCompounds2, expectedStehiomatrix2) = createDatas(reaction2)
        return listOf(
                arrayOf(expectedCompounds1, expectedStehiomatrix1, reaction1),
                arrayOf(expectedCompounds2, expectedStehiomatrix2, reaction2))
    }


    private fun datasForTestGetRates(): Array<Array<Any>> {
        val stg1 = reaction1()
        val (Br2, Br, H2, HBr, H) = r10

        val w11 = Br2.pow(stg1[0].reagents[0].amount)
        val w12 = Br.pow(stg1[1].reagents[0].amount) * H2.pow(stg1[1].reagents[1].amount)
        val w13 = HBr.pow(stg1[2].reagents[0].amount) * H.pow(stg1[2].reagents[1].amount) * Br.pow(stg1[2].reagents[2].amount)
        val w14 = H.pow(stg1[3].reagents[0].amount) * HBr.pow(stg1[3].reagents[1].amount)
        val w15 = Br.pow(stg1[4].reagents[0].amount)

        w1 = arrayOf(w11, w12, w13, w14, w15)
        val expectedRatesValues1: Array<Double> = k1.array() * w1

        val stg2 = reaction2()

        val (A0, B0, Q0, Z0) = r20

        val w21 = A0.pow(stg2[0].reagents[0].amount) * B0.pow(stg2[0].reagents[1].amount)
        val w22 = A0.pow(stg2[1].reagents[0].amount) * Q0.pow(stg2[1].reagents[1].amount)
        w2 = arrayOf(w21, w22)
        val expectedRatesValues2 = k2.array() * w2
        return arrayOf(
                arrayOf(stg1, k1.array(), r10.array(), expectedRatesValues1),
                arrayOf(stg2, k2.array(), r20.array(), expectedRatesValues2)
        )
    }

    private fun createDatas(stages: List<Stage>): Pair<List<String>, Matrix<Int>> {
        val expectedCompounds: List<String> = stages
                .flatMap { it.reagents + it.products }
                .distinctBy { it.elements.toString() }
                .map { it.elements.toString() }

        val rows = stages.size
        val columns = expectedCompounds.size

        val expectedStehiomatrix = Matrix(Integers, ListUtils.arrayLists(rows, columns) { 0 })

        stages.withIndex().forEach { (i, stage) ->
            expectedCompounds.withIndex().forEach { (j, compound) ->
                expectedStehiomatrix[i][j] = stage.getAmountOf(compound)
            }
        }

        return expectedCompounds to expectedStehiomatrix
    }




    fun reaction1(): List<Stage> {
        val Br2 = mapOf("Br" to 2)
        val Br = mapOf("Br" to 1)
        val H2 = mapOf("H" to 2)
        val HBr = mapOf("H" to 1, "Br" to 1)
        val H = mapOf("H" to 1)

        val (a, b, c, d, e) = stehCoeff1

        /*** НЕ МЕНЯЙТЕ РЕАКЦИИ ***/
        val stage1 = Stage(Compounds(c(a, Br2)), "->", Compounds(c(b, Br)))
        val stage2 = Stage(Compounds(c(a, Br), c(a, H2)), "->", Compounds(c(a, HBr), c(a, H)))
        val stage3 = Stage(Compounds(c(e, HBr), c(b, H), c(c, Br)), "->", Compounds(c(b, H2), c(d, Br2)))
        val stage4 = Stage(Compounds(c(a, H), c(a, HBr)), "->", Compounds(c(a, Br), c(a, H2)))
        val stage5 = Stage(Compounds(c(b, Br)), "->", Compounds(c(a, Br2)))
        /*** НЕ МЕНЯЙТЕ РЕАКЦИИ ***/
        val stages = listOf(stage1, stage2, stage3, stage4, stage5)

        return stages
    }

    fun reaction2(): List<Stage> {
        val A = mapOf("A" to 1)
        val B = mapOf("B" to 1)
        val Q = mapOf("Q" to 1)
        val Z = mapOf("Z" to 1)

        val (a1, a2, bi, q, z) = stehCoeff2
        /*** НЕ МЕНЯЙТЕ РЕАКЦИИ ***/
        val stage1 = Stage(Compounds(c(a1, A), c(bi, B)), "->", Compounds(c(q, Q)))
        val stage2 = Stage(Compounds(c(a2, A), c(q, Q)), "->", Compounds(c(z, Z)))
        /*** НЕ МЕНЯЙТЕ РЕАКЦИИ ***/
        val stages = listOf(stage1, stage2)

        return stages
    }

}

private fun c(i: Int, H: Map<String, Int>) = Compound(i, ElementsAndAmounts(H))
private fun c(H: Map<String, Int>) = c(1, ElementsAndAmounts(H))

private operator fun Array<Double>.times(array: Array<Double>): Array<Double> {
    return this.zip(array).map { it.first * it.second }.toTypedArray()
}

