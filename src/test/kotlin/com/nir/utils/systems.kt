package com.nir.utils

import com.nir.utils.math.F


class LorentzStrangeAttractor(
        private val b: Double = 8/3.0,
        private val sigma: Double = 10.0,
        private val R: Double = 28.0)
    :
        F(
                { r, t -> sigma*(r[1] - r[0]) },
                { r, t -> r[0]*(R - r[2]) - r[1] },
                { r, t -> r[0]*r[1] - b*r[2] }
        )