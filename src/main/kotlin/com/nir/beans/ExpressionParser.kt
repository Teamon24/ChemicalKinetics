package com.nir.beans

import net.objecthunter.exp4j.ExpressionBuilder

object ExpressionParser {
    fun parse(expression: String): Double {
        val expressionBuilder = ExpressionBuilder(expression)
        return expressionBuilder.build().evaluate()
    }

    fun toDouble(any: Any): Double {
        return if (any is String) {
            parse(any)
        } else {
            return when (any) {
                is Int -> any.toDouble()
                is Double -> any
                is Float -> any.toDouble()
                else -> throw RuntimeException("element: $any should be primitive")
            }
        }
    }
}