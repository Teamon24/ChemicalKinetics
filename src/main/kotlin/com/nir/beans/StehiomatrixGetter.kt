package com.nir.beans

import com.nir.ui.dto.Compounds
import com.nir.ui.dto.Reaction
import com.nir.ui.dto.Stage
import com.nir.utils.math.F
import com.nir.utils.math.Stehiomatrix
import com.nir.utils.math.R
import com.nir.utils.math.StageRates
import com.nir.utils.math.T
import com.nir.utils.math.zeroReturn
import kotlin.math.pow

/**
 * Константы скорости реакции.
 */
typealias K = Array<Double>


typealias StageAndItsReagentsIndices = Pair<Int, List<Int>>
typealias StagesAndItsReagentsIndices = ArrayList<StageAndItsReagentsIndices>

object StehiomatrixGetter {

    @JvmStatic
    fun times(stehiomatrix: Stehiomatrix, stageRates: StageRates): F {
        val ratesAmount = stageRates.size
        if (stehiomatrix.columns != ratesAmount) {
            throw RuntimeException("Columns amount of matrix and rates vector size should be equal.")
        }

        val f = Array(stehiomatrix.rows) { zeroReturn }
        for (row in stehiomatrix.rowsRange) {
            f[row] = f(row, stehiomatrix, stageRates)
        }

        return F(*f)
    }

    private fun f(row: Int, stehiomatrix: Stehiomatrix, stageRates: StageRates): (R, T) -> Double {
        val row = stehiomatrix[row]
        val res = ArrayList<(R,T)-> Double>()
        for (column in stehiomatrix.columnsRange) {
            val rate = stageRates[column]
            val coeff = row[column]
            res.add { r, t -> coeff * rate(r, t) }
        }

        return { r, t -> res.fold(0.0) {acc, next -> acc + next(r,t) } }
    }

    @JvmStatic
    fun getMatrix(reaction: Reaction): Stehiomatrix {
        val compounds = getCompounds(reaction)
        val m = reaction.size
        val n = compounds.size
        val elements = Array(m) { Array(n) { 0 } }

        reaction.withIndex().forEach { (i, stage) ->
            compounds.withIndex().forEach { (j, compound) ->
                elements[i][j] = stage.getAmountOf(compound)
            }
        }

        return Stehiomatrix(elements)
    }

    @JvmStatic
    fun getCompounds(reaction: Reaction): List<String> {
        val uniqueReagents = reaction.getUniques { stage -> stage.reagents }
        val uniqueProducts = reaction.getUniques { stage -> stage.products }
        return (uniqueReagents + uniqueProducts).distinct()
    }

    @JvmStatic
    fun getRates(reaction: Reaction, k: K): StageRates {
        val compounds = this.getCompounds(reaction)
        val stageNumberAndReagentsIndexes = StagesAndItsReagentsIndices()
        reaction.withIndex().forEach { (index, stage) ->
            val reagents = stage.reagents
            val indexes = reagents.getIndexesIn(compounds) 
            stageNumberAndReagentsIndexes.add(index to indexes)
        }
        return getRates(stageNumberAndReagentsIndexes, k)
    }

    private fun getRates(stagesAndItsReagentsIndices: StagesAndItsReagentsIndices, k: K): StageRates {
        assert(k.size == stagesAndItsReagentsIndices.size)
        val rates =
                stagesAndItsReagentsIndices
                        .map { (i, indices) -> { r: R, _: T -> rate(k, r, i, indices) } }
                        .toTypedArray()

        return StageRates(rates)
    }

    private fun rate(k: K, r: R, i: Int, indices: List<Int>) = k[i] * r.filterBy(indices).productAs(indices)

    private fun Reaction.getUniques(getCompounds: (Stage) -> Compounds): List<String> {
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

    private fun R.filterBy(indices: List<Int>): List<Double> {
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