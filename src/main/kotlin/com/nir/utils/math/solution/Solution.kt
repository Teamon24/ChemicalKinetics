package com.nir.utils.math.solution

import com.nir.utils.Eightfold
import com.nir.utils.Fourfold
import com.nir.utils.math.InitialData
import com.nir.utils.math.method.D
import com.nir.utils.math.method.Method
import com.nir.utils.math.method.F
import com.nir.utils.math.method.N
import com.nir.utils.math.method.X0
import com.nir.utils.math.method.Y
import com.nir.utils.math.method.dX
import de.gsi.dataset.spi.DoubleDataSet

object Solution {

    class Info internal constructor() {
        internal lateinit var method: Method
        internal lateinit var f: F
        internal lateinit var initialData: InitialData
        internal lateinit var dataSets: List<DoubleDataSet>

        fun components() : Fourfold<Method, F, InitialData, List<DoubleDataSet>> {
            return Fourfold(method, f, initialData, dataSets)
        }

        fun datas() : Eightfold<Method, F, List<DoubleDataSet>, Y, X0, dX, N, D> {
            val (method, system, initialData, dataSets) = this.components()
            val (x0, y0, dx, n) = initialData
            val d = y0.size
            return Eightfold(method, system, dataSets, y0, x0, dx, n, d)
        }
    }

    @JvmStatic
    fun method(method: Method): SystemStep {
        val info = Info()
        info.method = method
        return SystemStep(info)
    }
}
