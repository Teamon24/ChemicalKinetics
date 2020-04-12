package com.nir.utils.math.method

object AdamsBashforthMethods {

    private val methods = LinkedHashMap<Int, AdamsBashforthMethod>()

    @JvmStatic
    operator fun get(order: Int): AdamsBashforthMethod {
        val adamsBashforthMethod = this.methods[order]!!
        return adamsBashforthMethod
    }

    @JvmStatic
    operator fun set(
            order: Int,
            adamsBashforthMethod: AdamsBashforthMethod
    ) {
        this.methods[order] = adamsBashforthMethod
    }
}