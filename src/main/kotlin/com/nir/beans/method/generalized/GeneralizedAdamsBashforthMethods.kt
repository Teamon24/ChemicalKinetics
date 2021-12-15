package com.nir.beans.method.generalized

object GeneralizedAdamsBashforthMethods {

    private val methods = LinkedHashMap<Int, GeneralizedAdamsBashforthMethod>()

    @JvmStatic
    operator fun get(order: Int): GeneralizedAdamsBashforthMethod {
        val adamsBashforthMethod = methods[order]!!
        return adamsBashforthMethod
    }

    @JvmStatic
    operator fun set(
            order: Int,
            adamsBashforthMethod: GeneralizedAdamsBashforthMethod
    ) {
        methods[order] = adamsBashforthMethod
    }
}