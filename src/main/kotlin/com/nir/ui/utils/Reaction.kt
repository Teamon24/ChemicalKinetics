package com.nir.ui.utils


typealias ElementSymbol = String
typealias ElementsAndAmounts = LinkedHashMap<ElementSymbol, Int>

data class Compound
constructor(
        val amount: Int,
        val elements: ElementsAndAmounts = LinkedHashMap()
) {
    fun getString(): String {
        return elements.entries.joinToString(separator = "", truncated = "") { (element, amount) -> "$element$amount" }
    }
}

typealias Compounds = ArrayList<Compound>

data class Reaction
constructor(
        val reagents: Compounds = ArrayList(),
        val type: String,
        val products: Compounds = ArrayList()
)

enum class ReactionType(val symbol: String) {
    COMPLEX_DIRECT("=>"),
    SIMPLE_DIRECT("->"),
    COMPLEX_REVERSABLE("<=>"),
    SIMPLE_REVERSABLE("<->");

    companion object {
        @JvmStatic
        fun reactionSymbols(): List<String> {
            return values().map { it.symbol }
        }
    }
}
