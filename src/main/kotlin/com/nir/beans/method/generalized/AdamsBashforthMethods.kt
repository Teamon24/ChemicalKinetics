package com.nir.beans.method.generalized

object AdamsBashforthMethods {

    private val methods = LinkedHashMap<Int, AdamsBashforthMethod>()

    @JvmStatic
    operator fun get(order: Int): AdamsBashforthMethod {
        val adamsBashforthMethod = methods[order]!!
        return adamsBashforthMethod
    }

    @JvmStatic
    operator fun set(
            order: Int,
            adamsBashforthMethod: AdamsBashforthMethod
    ) {
        methods[order] = adamsBashforthMethod
    }
}