package com.nir.ui.beans

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
