package com.nir.utils.math.solution

import com.nir.utils.Eightfold
import com.nir.utils.Fivefold
import com.nir.utils.math.ComputationConfigs
import com.nir.utils.math.InitialPoint
import com.nir.utils.math.D
import com.nir.beans.method.Method
import com.nir.utils.math.F
import com.nir.utils.math.N
import com.nir.utils.math.X0
import com.nir.utils.math.Y
import com.nir.utils.math.dX
import de.gsi.dataset.spi.DoubleDataSet

object Solution {

    class Info internal constructor() {
        internal lateinit var method: Method
        internal lateinit var f: F
        internal lateinit var initialPoint: InitialPoint
        internal lateinit var computationConfigs: ComputationConfigs
        internal lateinit var dataSets: List<DoubleDataSet>

        fun components() : Fivefold<Method, F, InitialPoint, ComputationConfigs, List<DoubleDataSet>> {
            return Fivefold(method, f, initialPoint, computationConfigs, dataSets)
        }

        fun datas() : Eightfold<Method, F, List<DoubleDataSet>, Y, X0, dX, N, D> {
            val (method, system, initialData, computationConfigs, dataSets) = this.components()
            val (x0, y0) = initialData
            val (dx, n) = computationConfigs
            val d = y0.size
            return Eightfold(method, system, dataSets, y0, x0, dx, n, d)
        }
    }

    @JvmStatic
    fun method(method: Method): ComputationConfigStep {
        val info = Info()
        info.method = method
        return ComputationConfigStep(info)
    }
}
