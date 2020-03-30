package com.nir.utils

import com.nir.ui.dto.Compound
import com.nir.ui.dto.Compounds
import com.nir.ui.dto.ElementsAndAmounts
import com.nir.ui.dto.Stage
import com.nir.beans.StehiomatrixGetter
import com.nir.utils.math.Stehiomatrix
import org.junit.Assert
import org.junit.Ignore
import org.junit.jupiter.api.Test

/**
 * Тест для класса [StehiomatrixGetter].
 */
class StehiomatrixGetterTest {

    private lateinit var expectedCompounds: List<String>
    private lateinit var expectedStehiomatrix: Stehiomatrix
    private var expectedRatesValues: Array<Double> = arrayOf(2.0, 12.0, 3037500.0, 30.0, 9.0)

    private fun reaction(): List<Stage> {
        val Br2 = mapOf("Br" to 2)
        val Br = mapOf("Br" to 1)
        val H2 = mapOf("H" to 2)
        val HBr = mapOf("H" to 1, "Br" to 1)
        val H = mapOf("H" to 1)

        val stage1 = Stage(Compounds(c(Br2)),                       "->", Compounds(c(2, Br)))
        val stage2 = Stage(Compounds(c(Br), c(H2)),                 "->", Compounds(c(HBr), c(H)))
        val stage3 = Stage(Compounds(c(5, HBr), c(2, H), c(3, Br)), "->", Compounds(c(2, H2), c(4, Br2)))
        val stage4 = Stage(Compounds(c(H), c(HBr)),                 "->", Compounds(c(Br), c(H2)))
        val stage5 = Stage(Compounds(c(2, Br)),                     "->", Compounds(c(Br2)))

        val stages = listOf(stage1, stage2, stage3, stage4, stage5)

        expectedCompounds = stages
                .flatMap { it.reagents }
                .distinctBy { it.elements.toString() }
                .map { it.elements.toString() }

        val compounds = StehiomatrixGetter.getCompounds(stages)
        val m = stages.size
        val n = compounds.size

        expectedStehiomatrix = Stehiomatrix(Array(m) { Array(n) { 0 } })

        stages.withIndex().forEach { (i, stage) ->
            compounds.withIndex().forEach { (j, compound) ->
                expectedStehiomatrix[i][j] = stage.getAmountOf(compound)
            }
        }

        return stages
    }

    /** Тест для метода [StehiomatrixGetter.getMatrix]. */
    @Test
    fun testGetMatrix() {
        val stages = reaction()
        val actualCompounds = StehiomatrixGetter.getCompounds(stages)
        val actualMatrix = StehiomatrixGetter.getMatrix(stages)

        Assert.assertArrayEquals(expectedCompounds.toTypedArray(), actualCompounds.toTypedArray())
        Assert.assertArrayEquals(expectedStehiomatrix.elements, actualMatrix.elements)
    }

    @Test
    fun testGetRates() {
        val k = arrayOf(1.0, 1.0, 1.0, 1.0, 1.0)
        val r = arrayOf(2.0, 3.0, 4.0, 5.0, 6.0)
        val rates = StehiomatrixGetter.getRates(reaction(), k)
        val result = rates.elements.map { action -> action(r, 0.0) }

        Assert.assertArrayEquals(expectedRatesValues, result.toTypedArray())
    }

    @Ignore
    @Test
    fun check() {
        val k = arrayOf(1.0, 1.0, 1.0, 1.0, 1.0)
        val stages = reaction()
        val stehiomatrix = StehiomatrixGetter.getMatrix(stages)
        val rates = StehiomatrixGetter.getRates(reaction(), k)
        val f = StehiomatrixGetter.times(stehiomatrix.transpose(), rates)
    }


    private fun c(i: Int, H: Map<String, Int>) = Compound(i, ElementsAndAmounts(H))
    private fun c(H: Map<String, Int>) = c(1, ElementsAndAmounts(H))
}