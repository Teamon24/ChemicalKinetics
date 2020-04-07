package com.nir.beans

import com.nir.ui.pojos.Compounds
import com.nir.ui.pojos.Reaction
import com.nir.ui.pojos.ReactionStage
import com.nir.utils.math.Matrix
import com.nir.ui.pojos.StageRates
import com.nir.utils.InitUtils
import com.nir.utils.math.Integers
import com.nir.utils.math.method.F
import com.nir.utils.math.method.Y
import com.nir.utils.math.method.emptyFunc
import kotlin.math.pow

/** Константы скорости реакции. */
typealias k = Array<Double>

/** Время */
typealias T = Double

/** Вектор концентраций */
typealias C = Array<Double>

typealias StageAndItsReagentsIndices = Pair<Int, List<Int>>
typealias StagesAndItsReagentsIndices = ArrayList<StageAndItsReagentsIndices>

object StehiomatrixGetter {

    @JvmStatic
    fun times(stehiomatrix: Matrix<Int>, stageRates: StageRates): F {
        val ratesAmount = stageRates.size
        if (stehiomatrix.columns != ratesAmount) {
            throw RuntimeException("Columns amount of matrix and rates vector size should be equal.")
        }

        val f = Array(stehiomatrix.rows) { emptyFunc }
        for (row in stehiomatrix.rowsRange) {
            f[row] = f(row, stehiomatrix, stageRates)
        }

        return F(*f)
    }

    private fun f(row: Int, stehiomatrix: Matrix<Int>, stageRates: StageRates): (T, C) -> Double {
        val row = stehiomatrix[row]
        val sum = ArrayList<(T, C)-> Double>()
        for (column in stehiomatrix.columnsRange) {
            val rate = stageRates[column]
            val coeff = row[column]
            sum.add { t, C -> coeff * rate(t, C) }
        }

        return { t, C -> sum.fold(0.0) { acc, next -> acc + next(t, C) } }
    }

    @JvmStatic
    fun getMatrix(reaction: Reaction): Matrix<Int> {
        val compounds = getCompounds(reaction)
        val rows = reaction.size
        val columns = compounds.size

        val elements = InitUtils.arrayLists(rows, columns) { 0 }

        reaction.withIndex().forEach { (i, stage) ->
            compounds.withIndex().forEach { (j, compound) ->
                elements[i][j] = stage.getAmountOf(compound)
            }
        }

        return Matrix(Integers, elements)
    }

    @JvmStatic
    fun getCompounds(reaction: Reaction): List<String> {
        val uniqueReagents = reaction.getUniques { stage -> stage.reagents }
        val uniqueProducts = reaction.getUniques { stage -> stage.products }
        return (uniqueReagents + uniqueProducts).distinct()
    }

    @JvmStatic
    fun getRates(reaction: Reaction, k: k): StageRates {
        val compounds = this.getCompounds(reaction)
        val stageNumberAndReagentsIndexes = StagesAndItsReagentsIndices()
        reaction.withIndex().forEach { (index, stage) ->
            val reagents = stage.reagents
            val indexes = reagents.getIndexesIn(compounds) 
            stageNumberAndReagentsIndexes.add(index to indexes)
        }
        return getRates(stageNumberAndReagentsIndexes, k)
    }

    private fun getRates(stagesAndItsReagentsIndices: StagesAndItsReagentsIndices, k: k): StageRates {
        assert(k.size == stagesAndItsReagentsIndices.size)
        val rates =
                stagesAndItsReagentsIndices
                        .map { (i, indices) -> { _: T, c: C -> rate(k, c, i, indices) } }
                        .toTypedArray()

        return StageRates(rates)
    }

    private fun rate(k: k, c: C, i: Int, indices: List<Int>) = k[i] * c.filterBy(indices).productAs(indices)

    private fun Reaction.getUniques(getCompounds: (ReactionStage) -> Compounds): List<String> {
        return this.flatMap(getCompounds).map { it.elements.toString() }
    }


    private fun Compounds.getIndexesIn(compounds: List<String>): List<Int> {
        val indexes = ArrayList<Int>()
        this.forEach { compound ->
            val compoundStr = compound.elements.toString()
            compounds.withIndex().forEach { (index, vectorCompound) ->
                if (compoundStr == vectorCompound) {
                    (0 until compound.amount).forEach { _ ->
                        indexes.add(index)
                    }
                }
            }
        }

        return indexes
    }

    private fun Y.filterBy(indices: List<Int>): List<Double> {
        val filteredByIndices = this.withIndex().filter { it.index in indices }.map { it.value }
        return filteredByIndices
    }

    private fun List<Double>.productAs(indices: List<Int>): Double {
        val repeats = indices.sorted().groupingBy { it }.eachCount().map { it.value }
        if(repeats.size != this.size) {
            throw RuntimeException("Repeats sizes should be eqauls to this size")
        }
        return this.zip(repeats).fold(initial = 1.0) { acc, pair ->
            val r = pair.first
            val repeat = pair.second
            acc * r.pow(repeat)
        }
    }
}
