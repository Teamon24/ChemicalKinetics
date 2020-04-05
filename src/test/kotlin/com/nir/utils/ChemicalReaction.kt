package com.nir.utils

import com.nir.utils.math.InitialData
import java.util.stream.Stream

data class ChemicalReaction(val N: Int,
                            val t0: Double,
                            val dt: Double,
                            val k: Array<Double>,
                            val r0: Array<Double>,
                            val reaction: Stream<String>
) {

    fun initialData(): InitialData {
        return InitialData(t0, r0, dt, N)
    }

    companion object {
        @JvmStatic
        fun chemicalReaction1(): ChemicalReaction {
            val n = 550000
            val t0 = 0.0
            val dt = 0.000001
            val k = arrayOf(2.0, 1.0)
            val r0 = arrayOf(15.5, 15.5, 0.0, 0.0)
            val reaction = Stream.of(
                    "2A+1B->4Q",
                    "1A+4Q->5Z"
            )
            return ChemicalReaction(n, t0, dt, k, r0, reaction)
        }

        @JvmStatic
        fun chemicalReaction2(): ChemicalReaction {
            val n = 15000
            val t0 = 0.0
            val dt = 0.0002
            val k = arrayOf(1.0, 1.0, 15.0, 1.0, 1.0)
            val r0 = arrayOf(5.5, 0.0, 12.0, 0.0, 50.0)
            val reaction = Stream.of(
                    "Br2->2Br",
                    "Br+H2->HBr+H",
                    "H+Br2->HBr+Br",
                    "H+HBr->Br+H2",
                    "2Br->Br2"
            )
            return ChemicalReaction(n, t0, dt, k, r0, reaction)
        }
    }

}