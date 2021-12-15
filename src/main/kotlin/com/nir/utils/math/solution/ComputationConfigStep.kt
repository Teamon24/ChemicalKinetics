package com.nir.utils.math.solution

import com.nir.utils.math.ComputationConfigs
import com.nir.utils.math.N
import com.nir.utils.math.dX

class ComputationConfigStep(private val info: Solution.Info) {
    fun computation(computationConfigs: ComputationConfigs): SystemStep {
        this.info.computationConfigs = computationConfigs
        return SystemStep(this.info);
    }

    fun computation(dx: dX, n: N): SystemStep {
        this.info.computationConfigs = ComputationConfigs(dx, n)
        return SystemStep(this.info);
    }
}