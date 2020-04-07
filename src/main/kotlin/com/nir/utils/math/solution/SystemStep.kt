package com.nir.utils.math.solution

import com.nir.utils.math.method.F

class SystemStep(private val info: Solution.Info) {
    fun system(f: F): InitialDataStep {
        this.info.f = f
        return InitialDataStep(this.info);
    }
}