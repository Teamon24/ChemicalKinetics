package com.nir.utils.math.method.jsonPojos

import com.nir.utils.math.method.MethodInfoJsonPojo

fun adamsBashforthName(k: Int) = "Adams-Bashforth $k-order method"
fun adamsMoultonName(k: Int) = "Adams-Multon ${k+1}-order method"

data class AdamsBashforthMethodJsonPojo(
        val k: Int,
        val betta: Array<*>,
        val C: Any
) : MethodInfoJsonPojo(adamsBashforthName(k))

data class AdamsMoultonMethodJsonPojo(
        val info: String?,
        val k: Int,
        val betta: Array<*>,
        val C: Any
) : MethodInfoJsonPojo(adamsMoultonName(k))