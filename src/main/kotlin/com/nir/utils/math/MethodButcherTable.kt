package com.nir.utils.math

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.ArrayList

enum class ExpressionType { NUMERIC, SYMBOLIC }

data class MethodInfoJsonPojo(
        val name: String,
        val info: String?,
        val type: String,
        val order: String,
        val stages: String,
        @JsonProperty("butchers_table")
        val butchersTableJsonPojo: ButchersTableJsonPojo?
)

data class ButchersTableJsonPojo(
        val expression: ExpressionJsonPojo?,
        val c: List<*>,
        val A: List<*>,
        val b: List<*>,
        val bCorrect: List<*> = ArrayList<Any>()
) {
        fun isAdaptive(): Boolean {
                return bCorrect.isNotEmpty()
        }

        fun hasNoExpression(): Boolean {
                return expression == null
        }

        fun hasVariablesExpression(): Boolean {
                return expression != null && expression.type == ExpressionType.SYMBOLIC
        }

        fun hasNumericExpressions(): Boolean {
                return expression != null && expression.type == ExpressionType.NUMERIC
        }
}

class ExpressionJsonPojo {
        val type: ExpressionType
        val vars: List<Vars>?

        @JsonCreator
        constructor(type: String, vars: List<Vars>?) {
                this.type = ExpressionType.valueOf(type.toUpperCase())
                this.vars = vars
        }

}

data class Vars(
        val name: String,
        @JsonProperty("excluded_values")
        val excludedValues: List<*>?
)