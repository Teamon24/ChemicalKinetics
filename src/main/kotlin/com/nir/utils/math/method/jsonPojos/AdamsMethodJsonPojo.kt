package com.nir.utils.math.method.jsonPojos

import com.nir.utils.math.method.generalized.GeneralizedMethodInfoJsonPojo

fun adamsBashforthName(k: Int) = "Adams-Bashforth $k-order method"
fun adamsMoultonName(k: Int) = "Adams-Multon ${k+1}-order method"

data class AdamsBashforthGeneralizedMethodJsonPojo(
        val k: Int,
        val betta: Array<*>,
        val C: Any
) : GeneralizedMethodInfoJsonPojo(adamsBashforthName(k))

data class AdamsMoultonGeneralizedMethodJsonPojo(
        val info: String?,
        val k: Int,
        val betta: Array<*>,
        val C: Any
) : GeneralizedMethodInfoJsonPojo(adamsMoultonName(k))