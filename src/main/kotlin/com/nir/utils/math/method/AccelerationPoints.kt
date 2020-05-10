package com.nir.utils.math.method

class AccelerationPoints(vararg points: Y) : ArrayList<Y>(points.size) {
    init {
        points.forEach { super.add(it) }
    }
}
