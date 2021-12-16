package com.nir.utils.math.solution

import com.nir.utils.math.ComputationConfigs
import com.nir.utils.math.F
import com.nir.utils.math.InitialPoint

class ComputationConfigStep(private val method: F,
                            private val initialPoint: InitialPoint) {
    fun computation(computationConfigs: ComputationConfigs): MethodStep {
        return MethodStep(method, initialPoint, computationConfigs);
    }
}