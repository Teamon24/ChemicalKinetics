package com.nir.beans.method

import com.nir.beans.method.hardcoded.ForwardEuler
import com.nir.beans.method.hardcoded.RungeKutta

class HardcodedMethodComponent {
    val getMethods: List<Method>
        get() = arrayListOf(RungeKutta(4), RungeKutta(5), ForwardEuler)
}