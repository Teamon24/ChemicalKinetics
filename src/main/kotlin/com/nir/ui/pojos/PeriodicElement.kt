package com.nir.ui.pojos

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.google.common.base.CaseFormat

@JsonDeserialize
@JsonIgnoreProperties(ignoreUnknown = true)
data class PeriodicElement
constructor(
        val appearance: String? = null,
        val atomicMass: String? = null,
        val boil: Double? = null,
        val category: String? = null,
        val color: Any? = null,
        val density: Double? = null,
        val discoveredBy: String?  = null,
        val electronAffinity: Double? = null,
        val electronConfiguration: String?  = null,
        val electronegativityPauling: Double? = null,
        val ionizationEnergies: List<Double> = ArrayList(),
        val melt: Double? = null,
        val molarHeat: Double? = null,
        val name: String? = null,
        val namedBy: String? = null,
        val number: String? = null,
        val period: Int? = null,
        val phase: String? = null,
        val shells: List<Int> = ArrayList(),
        val source: String? = null,
        val spectralImg: String? = null,
        val summary: String? = null,
        val symbol: String? = null,
        val xpos: Int? = null,
        val ypos: Int? = null
)


enum class PeriodicElementType
constructor(val values: List<String>)
{
    POST_TRANSITION_METAL(listOf("post-transition metal")),
    DIATOMIC_NONMETAL(listOf("diatomic nonmetal")),
    ALKALINE_EARTH_METAL(listOf("alkaline earth metal")),
    POLYATOMIC_NONMETAL(listOf("polyatomic nonmetal")),
    ALKALI_METAL(listOf("alkali metal")),
    METALLOID(listOf("metalloid")),
    TRANSITION_METAL(listOf("transition metal")),
    ACTINIDE(listOf("actinide")),
    LANTHANIDE(listOf("lanthanide")),
    NOBLE_GAS(listOf("noble gas")),

    UNKNOWN(
        listOf(
            "unknown, but predicted to be an alkali metal",
            "unknown, probably post-transition metal",
            "unknown, probably transition metal",
            "unknown, predicted to be noble gas",
            "unknown, probably metalloid"
        )
    );

    fun getClassBy(type: String): String {
        val name = values().first(has(type)).name
        return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, name);
    }

    companion object {

        @JvmStatic
        fun getBy(type: String): PeriodicElementType {
            return values().first(has(type))
        }

        private fun has(type: String) = { it: PeriodicElementType -> it.values.contains(type) }
    }
}

