package com.nir.utils

import com.nir.utils.math.System
import com.nir.utils.math.InitialData


class LorentzStrangeAttractor(
        private val b: Double = 8/3.0,
        private val sigma: Double = 10.0,
        private val R: Double = 28.0)
    :
        System(
                { t, r -> sigma*(r[1] - r[0]) },
                { t, r -> r[0]*(R - r[2]) - r[1] },
                { t, r -> r[0]*r[1] - b*r[2] }
        ) {

    fun initialData(): InitialData {
        return InitialData(
                t0 = 0.0,
                r0 = arrayOf(1.0, 1.0, 1.0),
                dt = 0.001,
                N = 250000
        )
    }

    fun titles(): List<String> {
        return listOf("X", "Y", "Z")
    }
}
