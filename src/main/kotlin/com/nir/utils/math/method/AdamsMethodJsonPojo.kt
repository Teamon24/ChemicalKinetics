package com.nir.utils.math.method

data class AdamsBashforthMethodJsonPojo(val k: Int, val betta: Array<*>, val C: Any) : MethodInfoJsonPojo("Adams-Bashforth $k-order method")
data class AdamsMoultonMethodJsonPojo(val info: String?, val k: Int, val betta: Array<*>, val C: Any) : MethodInfoJsonPojo("Adams-Multon ${k+1}-order method")