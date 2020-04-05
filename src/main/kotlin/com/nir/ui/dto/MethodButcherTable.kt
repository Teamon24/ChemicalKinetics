package com.nir.ui.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class MethodInfo(
        val name: String,
        val info: String?,
        val type: String,
        val order: String,
        val stages: String,
        val expression: Expression?,
        @JsonProperty("butchers_table")
        val butchersTable: ButchersTable?
)

data class ButchersTable(
        val A: List<*>,
        val b: List<*>,
        val c: List<*>
)

data class Expression(
        val type: String,
        val vars: List<Vars>?
)

data class Vars(
        val name: String,
        @JsonProperty("excluded_values")
        val excludedValues: List<*>?
)