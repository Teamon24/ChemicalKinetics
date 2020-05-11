package com.nir.utils.math.solution

import com.nir.utils.math.method.F

class SystemStep(private val info: Solution.Info) {
    fun system(f: F): InitialPointStep {
        this.info.f = f
        return InitialPointStep(this.info);
    }
}