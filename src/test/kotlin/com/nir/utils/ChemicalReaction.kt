package com.nir.utils

import com.nir.utils.math.ComputationConfigs
import com.nir.utils.math.InitialData
import java.util.stream.Stream

data class ChemicalReaction(val N: Int,
                            val t0: Double,
                            val dt: Double,
                            val k: Array<Double>,
                            val C0: Array<Double>,
                            val reaction: Stream<String>
) {

    val initialData = InitialData(t0, C0)
    val computationConfigs = ComputationConfigs(dt, N)

    companion object {
        @JvmStatic
        fun chemicalReaction1(): ChemicalReaction {
            val n = 600000
            val t0 = 0.0
            val dt = 0.00001
            val k = arrayOf(2.0, 1.0)
            val C0 = arrayOf(15.5, 15.5, 0.0, 0.0)
            val reaction = Stream.of(
                    "2A+1B->4Q",
                    "1A+4Q->5Z"
            )
            return ChemicalReaction(n, t0, dt, k, C0, reaction)
        }

        @JvmStatic
        fun chemicalReaction2(): ChemicalReaction {
            val n = 50000
            val t0 = 0.0
            val dt = 0.004
            val k = arrayOf(1.0, 1.0, 15.0, 1.0, 1.0)
            val C0 = arrayOf(5.5, 0.0, 12.0, 0.0, 50.0)
            val reaction = Stream.of(
                    "Br2->2Br",
                    "Br+H2->HBr+H",
                    "H+Br2->HBr+Br",
                    "H+HBr->Br+H2",
                    "2Br->Br2"
            )
            return ChemicalReaction(n, t0, dt, k, C0, reaction)
        }
    }

}