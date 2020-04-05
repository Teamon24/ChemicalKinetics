package com.nir.utils

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
