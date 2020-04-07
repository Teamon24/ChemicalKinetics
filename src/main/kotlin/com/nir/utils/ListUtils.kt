package com.nir.utils

object ListUtils {

    @JvmStatic
    fun <T> arrayLists(n: Int, m: Int): ArrayList<ArrayList<T>> {
        val list = ArrayList<ArrayList<T>>()
        for(i in 0 until n) {
            val arrayList = ArrayList<T>(m)
            list.add(arrayList)
        }
        return list
    }

    @JvmStatic
    fun <T> arrayLists(n: Int, m: Int, init: () -> T): ArrayList<ArrayList<T>> {
        val list = ArrayList<ArrayList<T>>()
        for(i in 0 until n) {
            val arrayList = arrayList(m, init)
            list.add(arrayList)
        }
        return list
    }

    fun <T> arrayList(n: Int, init: () -> T): ArrayList<T> {
        val arrayList = ArrayList<T>()
        for (j in 0 until n) {
            arrayList.add(init())
        }
        return arrayList
    }
}