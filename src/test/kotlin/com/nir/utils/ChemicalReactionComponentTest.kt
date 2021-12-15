package com.nir.utils

import com.nir.beans.reaction.ChemicalReactionComponent
import com.nir.ui.pojos.Compound
import com.nir.ui.pojos.Compounds
import com.nir.ui.pojos.ElementsAndAmounts
import com.nir.ui.pojos.Reaction
import com.nir.ui.pojos.ReactionStage
import com.nir.utils.math.Integers
import com.nir.utils.math.Matrix
import com.nir.utils.math.Y
import org.junit.Assert
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import kotlin.math.pow

/**
 * Тест для класса [ChemicalReactionComponent].
 */
class ChemicalReactionComponentTest {

    /** Тест для метода [ChemicalReactionComponent.getStehiometricMatrix]. */
    @Test
    fun testGetMatrix() {
        datasForTestGetMatrix().forEach { data ->
            val expectedCompounds = data[0] as List<String>
            val expectedStehiomatrix = data[1] as Matrix<Integer>
            val stages = data[2] as Reaction
            val actualCompounds = ChemicalReactionComponent.getCompounds(stages)
            val actualMatrix = ChemicalReactionComponent.getStehiometricMatrix(stages)

            Assertions.assertArrayEquals(expectedCompounds.toTypedArray(), actualCompounds.toTypedArray())
            Assertions.assertIterableEquals(expectedStehiomatrix.elements, actualMatrix.elements)
        }
    }

    @Test
    fun testGetRates() {
        datasForTestGetRates().forEach {
            val reaction = it[0] as Reaction
            val k = it[1] as Array<Double>
            val r = it[2] as Y
            val expectedRatesValues = it[3] as Array<Double>
            val rates = ChemicalReactionComponent.getRates(reaction, k)
            val result = rates.elements.map { action -> action(0.0, r) }
            Assertions.assertArrayEquals(expectedRatesValues, result.toTypedArray())
        }
    }

    @Test
    fun testSystemCreation() {
        datasForCheck().forEach {
            val (stages, k, r0, expectedValues) = it
            val stehiomatrix = ChemicalReactionComponent.getStehiometricMatrix(stages)
            val rates = ChemicalReactionComponent.getRates(stages, k)
            val transposed = stehiomatrix.transpose()
            val system = ChemicalReactionComponent.times(transposed, rates)
            Assert.assertEquals(r0.size, system.size)
            Assert.assertEquals(r0.size, system.size)
            val actualValues = system(0.0, r0)
            val equalsResults = expectedValues.zip(actualValues).map { (expected, actual) ->
                Triple(expected, actual, expected.equals(actual))
            }
            Assertions.assertTrue(equalsResults.fold(true) { acc, triple -> acc && triple.third })
        }
    }

    private fun datasForCheck(): Array<Fourfold<Reaction, Array<Double>, Array<Double>, Array<Double>>> {
        initW1()
        initW2()

        val (a, be, c, d, e) = stehCoeff1
        val expectedValues1 = arrayOf(
                -a * w1[0]             + d * w1[2]             + a  * w1[4],
                be * w1[0] - a * w1[1] - c * w1[2] + a * w1[3] - be * w1[4],
                           - a * w1[1] + be* w1[2] + a * w1[3],
                           + a * w1[1] - e * w1[2] - a * w1[3],
                           + a * w1[1] - be* w1[2] - a * w1[3]
        )

        val (a1, a2, b, q, z) = stehCoeff2
        val expectedValues2 = arrayOf(
                -a1 * w2[0] - a2 * w2[1],
                -b * w2[0],
                q * (w2[0] - w2[1]),
                z * w2[1]
        )

        return arrayOf(
                Fourfold(reaction2(), k2.array(), r20.array(), expectedValues2),
                Fourfold(reaction1(), k1.array(), r10.array(), expectedValues1)
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

        val expectedRatesValues1 = initW1()
        val expectedRatesValues2 = initW2()

        return arrayOf(
                arrayOf(reaction1(), k1.array(), r10.array(), expectedRatesValues1),
                arrayOf(reaction2(), k2.array(), r20.array(), expectedRatesValues2)
        )
    }

    val stehCoeff1 = Fivefold(1, 2, 3, 4, 5)
    private val k1 = Fivefold(1.0, 1.0, 1.0, 1.0, 1.0)
    lateinit var w1: Array<Double>
    private val r10 = Fivefold(2.0, 3.0, 4.0, 5.0, 6.0)

    private fun initW1(): Array<Double> {
        val (Br2, Br, H2, HBr, H) = r10
        val (a, b, c, d, e) = stehCoeff1
        val w11 = Br2.pow(a)
        val w12 = Br.pow(a) * H2.pow(a)
        val w13 = HBr.pow(e) * H.pow(b) * Br.pow(c)
        val w14 = H.pow(a) * HBr.pow(a)
        val w15 = Br.pow(b)
        w1 = k1.array() * arrayOf(w11, w12, w13, w14, w15)
        return w1
    }

    private val stehCoeff2 = Fivefold(5, 4, 3, 2, 1)
    private val k2 = Pair(5.0, 11.0)
    lateinit var w2: Array<Double>
    private val r20 = Fourfold(2.0, 3.0, 5.0, 7.0)

    private fun initW2(): Array<Double> {
        val (A0, B0, Q0, Z0) = r20
        val (a1, a2, b, q, z) = stehCoeff2
        val w21 = A0.pow(a1) * B0.pow(b)
        val w22 = A0.pow(a2) * Q0.pow(q)
        w2 = k2.array() * arrayOf(w21, w22)
        return w2
    }

    private fun createDatas(reactionStages: List<ReactionStage>): Pair<List<String>, Matrix<Int>> {
        val expectedCompounds: List<String> = reactionStages
                .flatMap { it.reagents + it.products }
                .distinctBy { it.elements.toString() }
                .map { it.elements.toString() }

        val rows = reactionStages.size
        val columns = expectedCompounds.size

        val expectedStehiomatrix = Matrix(Integers, InitUtils.arrayLists(rows, columns) { 0 })

        reactionStages.withIndex().forEach { (i, stage) ->
            expectedCompounds.withIndex().forEach { (j, compound) ->
                expectedStehiomatrix[i][j] = stage.getAmountOf(compound)
            }
        }

        return expectedCompounds to expectedStehiomatrix
    }

    fun reaction1(): List<ReactionStage> {
        val Br2 = mapOf("Br" to 2)
        val Br = mapOf("Br" to 1)
        val H2 = mapOf("H" to 2)
        val HBr = mapOf("H" to 1, "Br" to 1)
        val H = mapOf("H" to 1)

        val (a, b, c, d, e) = stehCoeff1

        /*** НЕ МЕНЯЙТЕ РЕАКЦИИ ***/
        val stage1 = ReactionStage(Compounds(c(a, Br2)), "->", Compounds(c(b, Br)))
        val stage2 = ReactionStage(Compounds(c(a, Br), c(a, H2)), "->", Compounds(c(a, HBr), c(a, H)))
        val stage3 = ReactionStage(Compounds(c(e, HBr), c(b, H), c(c, Br)), "->", Compounds(c(b, H2), c(d, Br2)))
        val stage4 = ReactionStage(Compounds(c(a, H), c(a, HBr)), "->", Compounds(c(a, Br), c(a, H2)))
        val stage5 = ReactionStage(Compounds(c(b, Br)), "->", Compounds(c(a, Br2)))
        /*** НЕ МЕНЯЙТЕ РЕАКЦИИ ***/
        val stages = listOf(stage1, stage2, stage3, stage4, stage5)

        return stages
    }

    fun reaction2(): List<ReactionStage> {
        val A = mapOf("A" to 1)
        val B = mapOf("B" to 1)
        val Q = mapOf("Q" to 1)
        val Z = mapOf("Z" to 1)

        val (a1, a2, b, q, z) = stehCoeff2
        /*** НЕ МЕНЯЙТЕ РЕАКЦИИ ***/
        val stage1 = ReactionStage(Compounds(c(a1, A), c(b, B)), "->", Compounds(c(q, Q)))
        val stage2 = ReactionStage(Compounds(c(a2, A), c(q, Q)), "->", Compounds(c(z, Z)))
        /*** НЕ МЕНЯЙТЕ РЕАКЦИИ ***/
        val stages = listOf(stage1, stage2)

        return stages
    }

}

private fun c(i: Int, H: Map<String, Int>) = Compound(i, ElementsAndAmounts(H))



