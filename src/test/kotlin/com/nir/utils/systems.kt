package com.nir.utils

import com.nir.utils.math.method.F
import com.nir.utils.math.InitialPoint


class LorentzStrangeAttractor(
        private val b: Double = 8/3.0,
        private val sigma: Double = 10.0,
        private val R: Double = 28.0)
    :
        F(
                { t, r -> sigma*(r[1] - r[0]) },
                { t, r -> r[0]*(R - r[2]) - r[1] },
                { t, r -> r[0]*r[1] - b*r[2] }
        ) {

    fun initialPoint(): InitialPoint {
        return InitialPoint(
                x0 = 0.0,
                y0 = arrayOf(1.0, 1.0, 1.0))
    }

    fun titles(): List<String> {
        return listOf("X", "Y", "Z")
    }
}

class LorentzStrangeAttractor2(
        private val b: Double = 8/3.0,
        private val sigma: Double = 10.0,
        private val R: Double = 28.0)
    :
        F(
                { t, r -> sigma*(r[1] - r[0]) + r[3] },
                { t, r -> r[0]*(R - r[2]) - r[1] },
                { t, r -> r[0]*r[1] - b*r[2] },
                { t, r -> r[3]*r[1]-r[2] }
        ) {

    fun initialPoint(): InitialPoint {
        return InitialPoint(
                x0 = 0.0,
                y0 = Array(4) { -2.0 }
        )
    }

    fun titles(): List<String> {
        return listOf("Y1", "Y2", "Y3", "Y4")
    }
}
