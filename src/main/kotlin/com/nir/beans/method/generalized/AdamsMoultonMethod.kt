package com.nir.beans.method.generalized

import com.nir.utils.math.F
import com.nir.beans.method.Method
import com.nir.utils.math.D
import com.nir.utils.math.N
import com.nir.utils.math.X
import com.nir.utils.math.Xs
import com.nir.utils.math.Y
import com.nir.utils.math.Ys
import com.nir.utils.math.dX

class AdamsMoultonMethod(
        name: String,
        val order: Int,
        val betta: Array<Any>,
        val c: Any
)
    : Method("$name (Generalized)")
{

    override fun setUp(dx: X, d: D): Method {
        TODO("AdamsMoultonMethod has not been implemented yet")
    }

    override fun invoke(f: F, x0: X, y0: Y, dx: dX, n: N): Pair<Xs, Ys> {
        TODO("AdamsMoultonMethod has not been implemented yet")
    }

    override fun invoke(f: F, x: X, y: Y, dx: dX): Y {
        TODO("AdamsMoultonMethod has not been implemented yet")
    }
}