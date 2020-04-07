package com.nir.ui.pojos

typealias Reaction = Collection<ReactionStage>
class Compounds(): ArrayList<Compound>() {
    constructor(vararg compounds: Compound): this() {
        this.addAll(compounds)
    }

    constructor(compounds: ArrayList<Compound>): this() {
        this.addAll(compounds)
    }

}
typealias ElementSymbol = String

/**
 * Стадия химической реакции.
 */
data class ReactionStage
constructor(
        val reagents: Compounds = Compounds(),
        val type: String,
        val products: Compounds = Compounds()
) {
    /**
     * Содержит ли стадия химисческое соединение.
     * @param compound соединение, наличие которого в стадии необходимо проверить.
     */
    fun contains(compound: String): Boolean {
        if (compound[0].isDigit()) {
            throw RuntimeException("Compound should starts with letter.")
        }
        return this.getAmountOf(compound) != 0
    }

    /**
     * Какое количество молекул химического соединения содержит стадия.
     * @param soughtCompound химическое соединение, количество молекул которого необходимо найти.
     * @return количество молекул химичесого соединения в стадии.
     */
    fun getAmountOf(soughtCompound: String): Int {
        this.reagents.map {
            (amount, elementsAndAmounts) ->
            val compound = elementsAndAmounts.toString()
            if (compound == soughtCompound)
                return -amount
        }

        this.products.map {
            (amount, elementsAndAmounts) ->
            val compound = elementsAndAmounts.toString()
            if (compound == soughtCompound)
                return amount
        }

        return 0
    }
}


/**
 * Химическое соединение.
 */
data class Compound
constructor(val amount: Int,
            val elements: ElementsAndAmounts = ElementsAndAmounts())


/**
 * Химическик элементы и количество их атомов в химическом соединении.
 */
class ElementsAndAmounts() : LinkedHashMap<ElementSymbol, Int>() {
    constructor(source: Map<String, Int>): this() {
        super.putAll(source)
    }

    override fun toString() =
            this.entries.joinToString(separator = "", truncated = "") {
                (element, amount) -> "$element${ifOneThenEmpty(amount)}"
            }

    private fun ifOneThenEmpty(amount: Int): Any {
        return when (amount) {
            1 -> ""
            else -> amount
        }
    }
}


/**
 * Тип химической реакции.
 */
enum class ReactionType(val symbol: String) {
    COMPLEX_DIRECT("=>"),
    SIMPLE_DIRECT("->"),
    COMPLEX_REVERSABLE("<=>"),
    SIMPLE_REVERSABLE("<->");

    companion object {
        @JvmStatic
        fun stageSymbols(): List<String> {
            return values().map { it.symbol }
        }
    }
}
