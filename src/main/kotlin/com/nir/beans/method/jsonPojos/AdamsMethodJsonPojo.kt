package com.nir.beans.method.jsonPojos

import com.nir.beans.method.generalized.MethodInfoJsonPojo

fun adamsBashforthName(k: Int) = "Adams-Bashforth $k-order method"
fun adamsMoultonName(k: Int) = "Adams-Multon ${k+1}-order method"

data class AdamsBashforthMethodJsonPojo(
        val k: Int,
        val betta: Array<Any>,
        val C: Any
) : MethodInfoJsonPojo(adamsBashforthName(k))

data class AdamsMoultonMethodJsonPojo(
        val info: String?,
        val k: Int,
        val betta: Array<Any>,
        val C: Any
) : MethodInfoJsonPojo(adamsMoultonName(k))