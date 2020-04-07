package com.nir.utils.math

import com.nir.utils.ListUtils

class Matrix<T> {
    val type: Type<T>
    val rows: Int
    val columns: Int

    val rowsRange: IntRange
    val columnsRange: IntRange

    var elements: ArrayList<ArrayList<T>>

    constructor(type: Type<T>, rows: Int, columns: Int) {
        this.type = type
        this.rows = rows
        this.columns = columns
        this.rowsRange = 0 until rows
        this.columnsRange = 0 until columns
        this.elements = ArrayList(rows)
    }

    constructor(type: Type<T>, elements: ArrayList<ArrayList<T>>)  {
        this.type = type
        this.rows = elements.size
        this.columns = elements[0].size
        this.rowsRange = 0 until rows
        this.columnsRange = 0 until columns
        this.elements = elements
    }

    constructor(type: Type<T>, elements: Array<Array<T>>) {
        this.type = type
        this.rows = elements.size
        this.columns = elements[0].size
        this.rowsRange = 0 until rows
        this.columnsRange = 0 until columns
        val destination = ArrayList<ArrayList<T>>()
        for (element in elements) {
            val arrayList = arrayListOf<T>()
            for (t in element) {
                arrayList.add(t)
            }
            destination.add(arrayList)
        }

        this.elements = destination
    }

    operator fun get(row: Int): ArrayList<T> {
        return elements[row]
    }

    fun transpose(): Matrix<T> {
        val transposed = ListUtils.arrayLists(columns, rows) { type.init() }
        for(row in 0 until rows) {
            for(column in 0 until columns) {
                transposed[column][row] = elements[row][column]!!
            }
        }
        return Matrix(type, transposed)
    }
}