package com.nir.utils.math.solution

import com.nir.utils.math.F

object Solution {
    @JvmStatic
    fun system(system: F): InitialPointStep {
        return InitialPointStep(system);
    }
}
