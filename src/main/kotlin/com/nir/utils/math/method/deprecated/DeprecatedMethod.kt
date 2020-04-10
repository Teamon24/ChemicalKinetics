package com.nir.utils.math.method.deprecated

import com.nir.utils.math.method.D
import com.nir.utils.math.method.Method
import com.nir.utils.math.method.dX

abstract class DeprecatedMethod: Method() {
    abstract fun set(d: D, dx: dX)
}


