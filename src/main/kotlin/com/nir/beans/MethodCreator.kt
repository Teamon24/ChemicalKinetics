package com.nir.beans

import com.nir.utils.math.method.ButchersTableJsonPojo
import com.nir.utils.math.method.MethodInfoJsonPojo
import com.nir.utils.ExpressionUtils.vals
import com.nir.utils.ExpressionUtils.vars
import com.nir.utils.ExpressionUtils.varsAndVals
import com.nir.utils.math.method.ButchersTable
import com.nir.utils.math.method.ExplicitRungeKutta
import com.nir.utils.math.method.Method
import net.objecthunter.exp4j.ExpressionBuilder

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
        return Method.from(explicit)
    }

    private fun getButcherTable(butchersTableJsonPojo: ButchersTableJsonPojo): ButchersTable {
        return when {
            butchersTableJsonPojo.hasNoExpression() || butchersTableJsonPojo.hasNumericExpressions() -> {
                ButcherTableConverter.convert(butchersTableJsonPojo)
            }

            butchersTableJsonPojo.hasVariablesExpression() -> throw UnsupportedOperationException("No butcher's talbe creation logic for generalized (parameterized) numeric method")
            else -> throw IllegalStateException("Butcher's Table case that was forgotten to consider.")
        }
    }
}

