package com.nir.ui.pojos

import com.nir.beans.method.emptyFunc
import com.nir.beans.reaction.C
import com.nir.beans.reaction.T

/**
 * Скорости протекания стадии:
 * например для реакции
 *
 * для [2Br-><SUB>k</SUB>Br<SUB>2</SUB>] скорость протекания - W[<SUB>Br2</SUB>] = [k*C<SUB>Br</SUB><SUP>2</SUP>]
 */
class ReactionStagesRates {

    val size: Int
    var elements: Array<(T, C)-> Double>

    constructor(size: Int) {
        this.size = size
        this.elements = Array(size) { emptyFunc }
    }

    constructor(elements: Array<(T, C)-> Double>) : this(elements.size) {
        this.elements = elements
    }

    operator fun get(row: Int): (T, C) -> Double {
        return elements[row]
    }
}