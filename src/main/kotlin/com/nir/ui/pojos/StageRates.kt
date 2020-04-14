package com.nir.ui.pojos

import com.nir.beans.C
import com.nir.beans.T
import com.nir.utils.math.method.automatized.emptyFunc

class StageRates {

    val size: Int
    var elements: Array<(T, C)-> Double>

    constructor(size: Int) {
        this.size = size
        this.sizeRange = 0 until size
        this.elements = Array(size) { emptyFunc }
    }

    val sizeRange: IntRange

    constructor(elements: Array<(T, C)-> Double>) : this(elements.size) {
        this.elements = elements
    }

    operator fun get(row: Int): (T, C) -> Double {
        return elements[row]
    }
}