package com.nir.ui.beans

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.nir.ui.utils.Compound
import com.nir.ui.utils.Reaction
import com.nir.ui.utils.ReactionType.Companion.reactionSymbols
import java.util.regex.Matcher
import java.util.regex.Pattern

typealias Amount = Int
typealias RawReactionType = String
typealias RawElement = String

@JsonDeserialize data class RawCompound(
        val amount: Int,
        val compound: LinkedHashMap<RawElement, Amount>
)

@JsonDeserialize data class RawReaction(
        val reagents: List<RawCompound>,
        val reactionSymbol: RawReactionType,
        val products: List<RawCompound>
)

object ReactionParser {
    val separateByCapital = Regex("(?<=.)(?=\\p{Lu})")
    val separateDigitsAndLetters = Regex("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)")

    fun parse(reaction: String): RawReaction {
        val values = reactionSymbols()
        val symbol = values.first { isContain(reaction, it) }

        val leftAndRightPart = values
                .filter { isContain(reaction, it) }
                .map { reaction.split(it) }
                .firstOrNull { it.size == 2 }

        if (leftAndRightPart != null) {
            if (leftAndRightPart.size != 2) {
                throw RuntimeException("Result of split by reaction symbol should has 2 parts: result has ")
            }

            val leftCompounds = separateAmountFromCompound(leftAndRightPart[0])
            val rightCompounds = separateAmountFromCompound(leftAndRightPart[1])

            val leftElements = separateAmountFromCompoundElements(leftCompounds)
            val rightElements = separateAmountFromCompoundElements(rightCompounds)

            return RawReaction(leftElements, symbol, rightElements)
        } else {
            throw RuntimeException("Reaction after split by reaction symbol has no parts")
        }
    }

    fun convert(rawReaction: RawReaction): Reaction {
        val leftCompounds = ArrayList<Compound>()
        val rightCompounds = ArrayList<Compound>()
        val lefts = rawReaction.reagents.map { convert(it) }.toCollection(leftCompounds)
        val rights = rawReaction.products.map { convert(it) }.toCollection(rightCompounds)
        val reactionTypeSymbol = rawReaction.reactionSymbol
        return Reaction(lefts, reactionTypeSymbol, rights)
    }

    private fun separateAmountFromCompound(amountsAndCompounds: String): Map<String, Int> {
        return amountsAndCompounds
                .split("+")
                .map(::compoundAndAmount)
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
        return Compound(compound.amount, compound.compound)
    }
}
