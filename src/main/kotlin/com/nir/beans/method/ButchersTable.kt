package com.nir.beans.method

import com.nir.utils.math.Matrix
import com.nir.utils.math.Matrix2

class ButchersTable(
        val c: Array<Double>,
        val A: Matrix2<Double>,
        val b: Array<Double>,
        val b2: Array<Double>)