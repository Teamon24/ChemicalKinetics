package com.nir.ui.pojos

import com.nir.beans.method.emptyFunc
import com.nir.beans.reaction.C
import com.nir.beans.reaction.T

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