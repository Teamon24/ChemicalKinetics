package com.nir

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.nir.ReactionType.Companion.stageSymbols
import java.util.regex.Matcher
import java.util.regex.Pattern

typealias Amount = Int
typealias RawStageType = String
typealias RawElement = String

@JsonDeserialize data class RawCompound(
        val amount: Int,
        val compound: LinkedHashMap<RawElement, Amount>
)

@JsonDeserialize data class RawStage(
        val reagents: List<RawCompound>,
        val stageType: RawStageType,
        val products: List<RawCompound>
)

object StageParser {
    val separateByCapital = Regex("(?<=.)(?=\\p{Lu})")
    val separateDigitsAndLetters = Regex("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)")

    @JvmStatic
    fun parse(stage: String): RawStage {
        val stageTypes = stageSymbols()
        val symbol = stageTypes.first { isContain(stage, it) }

        val leftAndRightPart = stageTypes
                .filter { isContain(stage, it) }
                .map { stage.split(it) }
                .firstOrNull { it.size == 2 }

        if (leftAndRightPart != null) {
            if (leftAndRightPart.size != 2) {
                throw RuntimeException("Result of split by stage type should has 2 parts: result has ")
            }

            val leftCompounds = separateAmountFromCompound(leftAndRightPart[0])
            val rightCompounds = separateAmountFromCompound(leftAndRightPart[1])

            val reagents = separateAmountFromCompoundElements(leftCompounds)
            val products = separateAmountFromCompoundElements(rightCompounds)

            return RawStage(reagents, symbol, products)
        } else {
            throw RuntimeException("Reaction stage after split by type has no parts")
        }
    }

    @JvmStatic
    fun convert(rawStage: RawStage): Stage {
        val leftCompounds = ArrayList<Compound>()
        val rightCompounds = ArrayList<Compound>()

        val lefts = rawStage.reagents.map { convert(it) }.toCollection(leftCompounds)
        val rights = rawStage.products.map { convert(it) }.toCollection(rightCompounds)
        val stageTypeSymbol = rawStage.stageType

        val reagents = Compounds(lefts)
        val products = Compounds(rights)

        return Stage(reagents, stageTypeSymbol, products)
    }

    private fun separateAmountFromCompound(amountsAndCompounds: String): Map<String, Int> {
        return amountsAndCompounds
                .split("+")
                .map(StageParser::compoundAndAmount)
                .toMap()
    }

    private fun compoundAndAmount(amountAndCompound: String): Pair<String, Int> {
        var i = 0
        val amountSymbols = amountAndCompound.takeWhile { i++; it.isDigit(); }
        val compound = if (amountSymbols.isEmpty()) amountAndCompound else amountAndCompound.drop(i - 1)
        val amount = if (amountSymbols.isEmpty()) 1 else amountSymbols.toInt()
        return Pair(compound, amount)
    }

    private fun separateAmountFromCompoundElements(compounds: Map<String, Int>): List<RawCompound> {
        val rightElements = compounds.entries
                .map { (compound, amount) -> compound.split(separateByCapital) to amount }
                .map { divideElementAndAmount(it) }
        return rightElements
    }

    private fun amount(list: List<String>, toReturn: () -> Int): Int {
        return when (list.size) {
            1 -> 1
            else -> toReturn()
        }
    }

    private fun isContain(source: String, subItem: String): Boolean {
        val pattern = "\\b$subItem\\b"
        val p: Pattern = Pattern.compile(pattern)
        val m: Matcher = p.matcher(source)
        return m.find()
    }

    private fun divideElementAndAmount(elementsAndAmounts: Pair<List<String>, Int>): RawCompound {
        val destination = LinkedHashMap<RawElement, Amount>()
        val rawElements = elementsAndAmounts.first.map { elementAndAmount: String ->
            val split = elementAndAmount.split(separateDigitsAndLetters)
            val element = split[0]
            val amount = amount(split) { split[1].toInt() }
            Pair(element, amount)
        }.toMap(destination)
        return RawCompound(elementsAndAmounts.second, rawElements)
    }

    private fun convert(compound: RawCompound): Compound {
        return Compound(compound.amount, ElementsAndAmounts(compound.compound))
    }
}
