package com.nir.beans

import com.nir.utils.math.ButchersTableJsonPojo
import com.nir.utils.math.MethodInfoJsonPojo
import com.nir.utils.ExpressionUtils.vals
import com.nir.utils.ExpressionUtils.vars
import com.nir.utils.ExpressionUtils.varsAndVals
import com.nir.utils.math.ButchersTable
import com.nir.utils.math.ExplicitRungeKutta
import com.nir.utils.math.Method
import com.nir.utils.math.R
import com.nir.utils.math.System
import com.nir.utils.math.T
import net.objecthunter.exp4j.ExpressionBuilder
import java.awt.Dimension

object MethodCreator {

    object ExpressionParser {
        fun parse(expression: String): Double {
            val expressionBuilder = ExpressionBuilder(expression)
            return expressionBuilder.build().evaluate()
        }
    }

    init {
        val stringExpression = "1/a - 2 + a"
        val expressionBuilder = ExpressionBuilder(stringExpression)

        val variablesAndValues: Map<String, Double> = varsAndVals(vars("a"), vals(2.0))
        val variables = variablesAndValues.keys
        val expression = expressionBuilder.variables(variables).build()
        expression.setVariables(variablesAndValues)
        val actual = expression.evaluate()
    }

    @JvmStatic
    fun create(methodInfoJsonPojo: MethodInfoJsonPojo): Method {
        val butchersTableJsonPojo = methodInfoJsonPojo.butchersTableJsonPojo!!
        val butchersTable = getButcherTable(butchersTableJsonPojo)
        val order = methodInfoJsonPojo.order.toInt()
        val stages = methodInfoJsonPojo.stages.toInt()
        val methodName = methodInfoJsonPojo.name
        val explicit = ExplicitRungeKutta(stages, order, butchersTable, methodName)
        return object: Method() {
            override val name: String get() = explicit.name
            override fun set(dimension: Int, dt: T) { explicit.set(dimension, dt) }
            override fun invoke(system: System, r0: R, t0: T, dt: T, N: Int): Array<R> { return explicit(system, r0, t0, dt, N) }
            override fun invoke(system: System, r: R, t: T, dt: T): R { return explicit(system, r, t, dt) }
        }
    }

    private fun getButcherTable(butchersTableJsonPojo: ButchersTableJsonPojo): ButchersTable {
        return when {
            butchersTableJsonPojo.hasNoExpression() || butchersTableJsonPojo.hasNumericExpressions() -> {
                ButcherTableConverter.convert(butchersTableJsonPojo)
            }

            butchersTableJsonPojo.hasVariablesExpression() -> throw UnsupportedOperationException("No logic for general method with parameterized butcher's tables")
            else -> throw IllegalStateException("Case that was forgotten to consider.")
        }
    }
}

