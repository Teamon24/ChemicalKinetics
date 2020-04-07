package com.nir.ui.pojos

import com.nir.utils.math.R
import com.nir.utils.math.T
import com.nir.utils.math.emptyFunc

class StageRates {

    val size: Int
    var elements: Array<(T, R)-> Double>

    constructor(size: Int) {
        this.size = size
        this.sizeRange = 0 until size
        this.elements = Array(size) { emptyFunc }
    }

    val sizeRange: IntRange

    constructor(elements: Array<(T, R)-> Double>) : this(elements.size) {
        this.elements = elements
    }

    operator fun get(row: Int): (T, R) -> Double {
        return elements[row]
    }
}