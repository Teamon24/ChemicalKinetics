package com.nir.utils.math.method

import com.nir.utils.math.ComputationConfigs
import com.nir.utils.math.InitialData

class AdamsMoultonMethod(
        override val name: String,
        val order: Int,
        val betta: Array<Any>,
        val c: Any
) : Method() {

    override fun init(initialData: InitialData, computationConfig: ComputationConfigs): Method {
        TODO("AdamsMoultonMethod has not been implemented yet")
    }

    override fun invoke(f: F, y0: Y, x0: X, dx: dX, n: N): Array<Y> {
        TODO("AdamsMoultonMethod has not been implemented yet")
    }

    override fun invoke(f: F, y: Y, x: X, dx: dX): Y {
        TODO("AdamsMoultonMethod has not been implemented yet")
    }
}