package com.nir

import com.google.common.collect.Lists
import com.nir.beans.StageParser
import com.nir.beans.reaction.T
import com.nir.ui.pojos.ReactionStage
import com.nir.utils.array
import com.nir.utils.math.ComputationConfigs
import com.nir.utils.math.InitialPoint
import com.nir.utils.math.X
import com.nir.utils.to
import kotlin.math.exp

data class ChemicalReaction(val N: Int,
                            val t0: Double,
                            val dt: Double,
                            val k: Array<Double>,
                            val C0: Array<Double>,
                            val reaction: ArrayList<String>,
                            val analyticalSolution: Map<String, (X) -> Double> = HashMap()
) {

    val initialPoint = InitialPoint(t0, C0)
    val computationConfigs = ComputationConfigs(dt, N)
    val reactionStages = ArrayList<ReactionStage>()

    init {
        reactionStages.addAll(
                reaction
                        .map(StageParser::parse)
                        .map(StageParser::convert)
        )
    }

    companion object {
        @JvmStatic
        fun chemicalReaction1(): ChemicalReaction {
            val n = 600000
            val t0 = 0.0
            val dt = 0.00001
            val k = arrayOf(2.0, 1.0)
            val C0 = arrayOf(15.5, 15.5, 0.0, 0.0)
            val reactionStages = Lists.newArrayList(
                    "2A+1B->4Q",
                    "1A+4Q->5Z"
            )
            return ChemicalReaction(n, t0, dt, k, C0, reactionStages)
        }

        @JvmStatic
        fun chemicalReaction2(): ChemicalReaction {
            val n = 50000
            val t0 = 0.0
            val dt = 0.004
            val k = arrayOf(1.0, 1.0, 15.0, 1.0, 1.0)
            val C0 = arrayOf(5.5, 0.0, 12.0, 0.0, 50.0)
            val reaction = Lists.newArrayList(
                    "Br2->2Br",
                    "Br+H2->HBr+H",
                    "H+Br2->HBr+Br",
                    "H+HBr->Br+H2",
                    "2Br->Br2"
            )
            return ChemicalReaction(n, t0, dt, k, C0, reaction)
        }

        @JvmStatic
        fun chemicalReaction3(): ChemicalReaction {
            val n = 100
            val t0 = 0.0
            val dt = 0.1
            val (k1, k2) = 0.5 to 0.2
            val (Ca0, Cr0, Cs0) = 0.2 to 0.0 to 0.0
            val reaction = Lists.newArrayList("A->R", "R->S")
            val etalonSolution =
                    mapOf(
                            "A" to { t:T -> Ca0 * exp(-k1*t)},
                            "R" to { t:T -> k1*Ca0 / (k2-k1) * (exp(-k1*t) - exp(-k2*t))},
                            "S" to { t:T -> Ca0 * (1 + k1/(k1-k2)* exp(-k1*t) + k1/(k2-k1) - exp(-k2*t))}
                    )
            val C0 = (Ca0 to Cr0 to Cs0).array()
            val k = arrayOf(k1, k2)
            return ChemicalReaction(n, t0, dt, k, C0, reaction, etalonSolution)
        }

        @JvmStatic
        fun chemicalReaction4(): ChemicalReaction {
            val n = 100
            val t0 = 0.0
            val dt = 0.1
            val k = arrayOf(1.0,2.0,3.0,4.0)
            
            val reaction = Lists.newArrayList(
                "CH3I + Mg -> CH3-MgI",
                "(CH3)2C=O + CH3MgI -> (CH3)3C-O-Mg",
                "(CH3)3C-O-Mg + H-OH -> (CH3)3C-OH + OH-MgI",
                "(CH3)-MgI + H-OH -> CH4 + OH-MgI"
            )
            
            val C0 = arrayOf(1.0,2.0,3.0,4.0)
            return ChemicalReaction(n, t0, dt, k, C0, reaction)
        }
    }
}