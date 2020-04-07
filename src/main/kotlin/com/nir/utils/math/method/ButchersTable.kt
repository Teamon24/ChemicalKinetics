package com.nir.utils.math.method

import com.nir.utils.math.Matrix

class ButchersTable(
        val c: Array<Double>,
        val A: Matrix<Double>,
        val b: Array<Double>,
        val bCorrect: Array<Double>)