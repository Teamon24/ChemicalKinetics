package com.nir.beans.reaction

import com.nir.ChemicalReaction
import com.nir.ui.pojos.Compounds
import com.nir.ui.pojos.Reaction
import com.nir.ui.pojos.ReactionStage
import com.nir.ui.pojos.ReactionStagesRates
import com.nir.utils.InitUtils
import com.nir.utils.math.Integers
import com.nir.utils.math.Matrix
import com.nir.utils.math.F
import com.nir.utils.math.Y
import com.nir.beans.method.emptyFunc
import kotlin.math.pow

/** Константы скорости реакции. */
typealias k = Array<Double>

/** Время */
typealias T = Double

/** Вектор концентраций */
typealias C = Array<Double>

typealias StageAndItsReagentsIndices = Pair<Int, List<Int>>
typealias StagesAndItsReagentsIndices = ArrayList<StageAndItsReagentsIndices>

/**
 * Пример системы для документации методов ниже:
 * [
 *  <br/>Br<SUB>2</SUB> -><SUB>k1</SUB> 2Br,
 *  <br/>Br+H<SUB>2</SUB> -><SUB>k2</SUB> HBr+H,
 *  <br/>H+Br<SUB>2</SUB> -><SUB>k3</SUB> HBr+Br,
 *  <br/>H+HBr -><SUB>k4</SUB> Br+H2,
 *  <br/>2Br -><SUB>k5</SUB> Br<SUB>2</SUB>
 * ]
 */
object ChemicalReactionComponent {

    /**
     * Общий вид системы
     * [dC<SUP> -></SUP>/dt = f<SUP> -></SUP> = G<SUP>T</SUP>*w<SUP> -></SUP>]
     */
    @JvmStatic
    fun getSystem(chemicalReaction: ChemicalReaction): F {
        val reactionStages = chemicalReaction.reactionStages
        val k = chemicalReaction.k
        val matrix = this.getStehiometricMatrix(reactionStages)
        val rates: ReactionStagesRates = this.getRates(reactionStages, k)
        return system(matrix.transpose(), rates)
    }

    @JvmStatic
    fun system(stehiomatrix: Matrix<Int>, reactionStagesRates: ReactionStagesRates): F {
        val ratesAmount = reactionStagesRates.size
        if (stehiomatrix.columns != ratesAmount) {
            throw RuntimeException("Columns amount of matrix and rates vector size should be equal.")
        }

        val f = Array(stehiomatrix.rows) { emptyFunc }
        for (row in stehiomatrix.rowsRange) {
            f[row] = f(row, stehiomatrix, reactionStagesRates)
        }

        return F(*f)
    }

    /**
     * Получение стехиометрической матрицы.
     * Для системы, указанной в kotlin doc'е текущего класса
     * матрица будет иметь вид
     *  [
     *   <br/> -1  2  0  0  0
     *   <br/>  0 -1 -1  1  1
     *   <br/> -1  1  0 -1  1
     *   <br/>  0  1  1 -1 -1
     *   <br/>  1 -2  0  0  0
     *  ]
     */
    @JvmStatic
    fun getStehiometricMatrix(reaction: Reaction): Matrix<Int> {
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

    /**
     * Получает все соединения из реакции.
     * Например, результатом для системы, указанной в kotlin doc'е текущего класса
     * будет список: [<br/> { Br<SUB>2</SUB>, Br, H<SUB>2</SUB>, HBr, H }.]
     */
    @JvmStatic
    fun getCompounds(reaction: Reaction): List<String> {
        val uniqueReagents = reaction.getUniques { stage -> stage.reagents }
        val uniqueProducts = reaction.getUniques { stage -> stage.products }
        return (uniqueReagents + uniqueProducts).distinct()
    }

    /**
     * Получение вектора скоростей стадий.
     * Для системы, указанной в kotlin doc'е текущего класса вектор скоростей будет иметь вид:
     *
     * [
     * <br/> w<SUB>Br2</SUB> = k1*C<SUB>Br2</SUB>
     * <br/> w<SUB>Br</SUB> = k2*C<SUB>Br</SUB> C<SUB>H2</SUB>
     * <br/> w<SUB>H2</SUB> = k3*C<SUB>H</SUB> C<SUB>Br2</SUB>
     * <br/> w<SUB>H</SUB> = k4*C<SUB>H</SUB> C<SUB>HBr</SUB>
     * <br/> w<SUB>HBr</SUB> = k5*C<SUB>Br</SUB>
     * ]
     */
    @JvmStatic
    fun getRates(reaction: Reaction, k: k): ReactionStagesRates {
        val compounds = this.getCompounds(reaction)
        val stageNumberAndReagentsIndexes = StagesAndItsReagentsIndices()
        reaction.withIndex().forEach { (index, stage) ->
            val reagents = stage.reagents
            val indexes = reagents.getIndexesIn(compounds)
            stageNumberAndReagentsIndexes.add(index to indexes)
        }
        return getRates(stageNumberAndReagentsIndexes, k)
    }

    private fun f(rowNumber: Int, stehiomatrix: Matrix<Int>, reactionStagesRates: ReactionStagesRates): (T, C) -> Double {
        val row = stehiomatrix[rowNumber]
        val sum = ArrayList<(T, C)-> Double>()
        for (column in stehiomatrix.columnsRange) {
            val rate = reactionStagesRates[column]
            val coeff = row[column]
            sum.add { t, C -> coeff * rate(t, C) }
        }

        return { t, C -> sum.fold(0.0) { acc, next -> acc + next(t, C) } }
    }

    private fun getRates(stagesAndItsReagentsIndices: StagesAndItsReagentsIndices, k: k): ReactionStagesRates {
        assert(k.size == stagesAndItsReagentsIndices.size)
        val rates =
                stagesAndItsReagentsIndices
                        .map { (i, indices) -> { _: T, c: C -> rate(k, c, i, indices) } }
                        .toTypedArray()

        return ReactionStagesRates(rates)
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
