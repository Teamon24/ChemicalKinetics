package com.nir.utils.math.method

import com.nir.utils.math.ComputationConfigs
import com.nir.utils.math.InitialPoint
import com.nir.utils.math.method.automatized.Method

class AdamsMoultonMethod(
        override val name: String,
        val order: Int,
        val betta: Array<Any>,
        val c: Any
) : Method() {

    override fun setUp(initialPoint: InitialPoint, computationConfig: ComputationConfigs): Method {
        TODO("AdamsMoultonMethod has not been implemented yet")
    }

    override fun invoke(f: F, x0: X, y0: Y, dx: dX, n: N): Array<Y> {
        TODO("AdamsMoultonMethod has not been implemented yet")
    }

    override fun invoke(f: F, x: X, y: Y, dx: dX): Y {
        TODO("AdamsMoultonMethod has not been implemented yet")
    }
}