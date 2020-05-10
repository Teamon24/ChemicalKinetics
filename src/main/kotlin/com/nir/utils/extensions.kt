package com.nir.utils


inline fun <reified T> Array<T>.get(fromInclusive: Int, amount: Int): Array<T> {
    val selected = ArrayList<T>(amount)
    for (i in fromInclusive until fromInclusive + amount) {
        selected.add(this[i])
    }
    return selected.toTypedArray()
}

fun <T> Array<T>.put(start: Int, end: Int, elements: Array<T>) {
    for ((j, i) in (start..end).withIndex()) {
        this[i] = elements[j]
    }
}

fun String.delete(string: String): String {
    return this.replace(string, "")
}

fun String.delete(vararg string: String): String {
    var result = this
    for (toDelete in string) {
        result = result.delete(toDelete)
    }
    return result
}

infix fun Boolean.ifTrue(predicate: () -> Any) = if (this) predicate() else {}
