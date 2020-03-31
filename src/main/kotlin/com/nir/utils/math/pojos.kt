package com.nir.utils.math

/** Time */
typealias T = Double

/** R = [x1,x2,x3...xn] */
typealias R = Array<Double>

/**
 * dr/dt = F
 */
open class F (vararg val f: (R, T) -> Double) {
    val size: Int = f.size
    operator fun invoke(r: R, t: T): Array<Double> {
        return (f.indices).map { i -> f[i](r, t) }.toTypedArray()
    }
}

val zeroReturn = { _: R, _:T -> 0.0 }

class StageRates {

    val size: Int
    var elements: Array<(R,T)-> Double>

    constructor(size: Int) {
        this.size = size
        this.sizeRange = 0 until size
        this.elements = Array(size) { zeroReturn }
    }

    val sizeRange: IntRange

    constructor(elements: Array<(R,T)-> Double>) : this(elements.size) {
        this.elements = elements
    }

    operator fun get(row: Int): (R,T) -> Double {
        return elements[row]
    }
}

class Stehiomatrix {
    val rows: Int
    val columns: Int

    val rowsRange: IntRange
    val columnsRange: IntRange

    var elements: Array<Array<Int>>

    constructor(rows: Int, columns: Int) {
        this.rows = rows
        this.columns = columns
        this.rowsRange = 0 until rows
        this.columnsRange = 0 until columns
        this.elements = Array(rows) { Array(columns) { 0 } }
    }

    constructor(elements: Array<Array<Int>>)  {
        this.rows = elements.size
        this.columns = elements[0].size
        this.rowsRange = 0 until rows
        this.columnsRange = 0 until columns
        this.elements = elements
    }

    operator fun get(row: Int): Array<Int> {
        return elements[row]
    }

    fun transpose(): Stehiomatrix {
        val transposed = Array(columns) { Array(rows) { 0 } }
        for(row in 0 until rows) {
            for(column in 0 until columns) {
                transposed[column][row] = elements[row][column]
            }
        }
        return Stehiomatrix(transposed)
    }
}
