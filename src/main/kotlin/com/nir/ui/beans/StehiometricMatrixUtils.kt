package com.nir.ui.beans

import com.nir.ui.utils.Compound
import com.nir.ui.utils.Compounds
import com.nir.ui.utils.ElementsAndAmounts
import com.nir.ui.utils.Reaction

object StehiometricMatrixUtils {
    fun unique(reactions: Collection<Reaction>) {
    }
}

fun main() {
    val reaction = Reaction(
            Compounds(listOf(
                    Compound(5,
                            ElementsAndAmounts(mapOf("H" to 1, "S" to 2))
                    ),
                    Compound(2,
                            ElementsAndAmounts(mapOf("H" to 2, "S" to 1))
                    )
            )
            ), "->", Compounds(
            listOf(
                    Compound(5,
                            ElementsAndAmounts(mapOf("H" to 1, "S" to 2))
                    ),
                    Compound(2,
                            ElementsAndAmounts(mapOf("H" to 2, "S" to 1))
                    )
            )
    ))
    StehiometricMatrixUtils.unique(listOf(reaction))
}