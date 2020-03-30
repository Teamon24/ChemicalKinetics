package com.nir.utils


object ArrayUtils {
    fun twoDimArray(sizes: Pair<Int,Int>): Array<Array<Double>> {
        val dimension = sizes.first
        val pointsAmount = sizes.second
        val arrayList = ArrayList<Array<Double>>()
        (0 until dimension).map {
            val points = ArrayList<Double>()
            (0 until pointsAmount).map { points.add(0.0) }
            arrayList.add(points.toTypedArray())
        }
        return arrayList.toTypedArray()
    }
}

