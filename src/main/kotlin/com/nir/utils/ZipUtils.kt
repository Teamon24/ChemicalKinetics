package com.nir.utils

object ZipUtils {

    @JvmStatic
    fun<T1, T2> zipToMap(c1: Collection<T1>, c2: Collection<T2>): Map<T1, T2> {
        return c1.zip(c2).toMap()
    }

    @JvmStatic
    fun<T1, T2> zipToMap(a1: Array<T1>, a2: Array<T2>): Map<T1, T2> {
        return a1.zip(a2).toMap()
    }

    @JvmStatic
    fun<T1, T2> zip(c1: Collection<T1>, c2: Collection<T2>): List<Pair<T1, T2>> {
        return c1.zip(c2)
    }

    @JvmStatic
    fun<T1, T2> zip(a1: Array<T1>, a2: Array<T2>): List<Pair<T1, T2>> {
        return a1.zip(a2)
    }
}