package com.nir.utils.math.method.generalized

import com.nir.utils.math.ComputationConfigs
import com.nir.utils.math.InitialPoint
import com.nir.utils.math.method.F
import com.nir.utils.math.method.Method
import com.nir.utils.math.method.N
import com.nir.utils.math.method.X
import com.nir.utils.math.method.Xs
import com.nir.utils.math.method.Y
import com.nir.utils.math.method.Ys
import com.nir.utils.math.method.dX

class GeneralizedAdamsMoultonMethod(
        name: String,
        val order: Int,
        val betta: Array<Any>,
        val c: Any
)
    : GeneralizedMethod(name)
{

    override fun setUp(initialPoint: InitialPoint, computationConfig: ComputationConfigs): Method {
        TODO("AdamsMoultonMethod has not been implemented yet")
    }

    override fun invoke(f: F, x0: X, y0: Y, dx: dX, n: N): Pair<Xs, Ys> {
        TODO("AdamsMoultonMethod has not been implemented yet")
    }

    override fun invoke(f: F, x: X, y: Y, dx: dX): Y {
        TODO("AdamsMoultonMethod has not been implemented yet")
    }
}