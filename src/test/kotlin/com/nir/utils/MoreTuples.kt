package com.nir.utils

data class Quadruple<out A, out B, out C, out D>
constructor(val a: A, val b: B, val c: C, val d: D) {
    override fun toString(): String = s(arrayOf(a, b, c, d))
}

data class Quintuple<out A, out B, out C, out D, out E>
constructor(val a: A, val b: B, val c: C, val d: D, val e: E) {
    override fun toString(): String = s(arrayOf(a, b, c, d, e))

}

data class Sextuple<A, B, C, D, E, F>
constructor(val a: A, val b: B, val c: C, val d: D, val e: E, val f: F) {
    override fun toString(): String = s(arrayOf(a, b, c, d, e, f))
}

data class Septuple<A, B, C, D, E, F, G>
constructor(val a: A, val b: B, val c: C, val d: D, val e: E, val f: F, val g: G) {
    override fun toString(): String = s(arrayOf(a, b, c, d, e, f, g))
}

data class Octuple<A, B, C, D, E, F, G, H>
constructor(val a: A, val b: B, val c: C, val d: D, val e: E, val f: F, val g: G, val h: H) {
    override fun toString(): String = s(arrayOf(a, b, c, d, e, f, g, h))
}

private fun s(anys: Array<*>): String = "(${anys.joinToString(separator = ", ", truncated = "") { it.toString() }})"


/**
 * Converts this quintuple into history list.
 */
fun <T> Pair      <T, T>                   .list(): List<T> = listOf(first, second)
fun <T> Triple    <T, T, T>                .list(): List<T> = listOf(first, second, third)
fun <T> Quadruple <T, T, T, T>             .list(): List<T> = listOf(a, b, c, d)
fun <T> Quintuple <T, T, T, T, T>          .list(): List<T> = listOf(a, b, c, d, e)
fun <T> Sextuple  <T, T, T, T, T, T>       .list(): List<T> = listOf(a, b, c, d, e, f)
fun <T> Septuple  <T, T, T, T, T, T, T>    .list(): List<T> = listOf(a, b, c, d, e, f, g)
fun <T> Octuple   <T, T, T, T, T, T, T, T> .list(): List<T> = listOf(a, b, c, d, e, f, g, h)

inline fun <reified T> Pair      <T, T>                   .array(): Array<T> = arrayOf(first, second)
inline fun <reified T> Triple    <T, T, T>                .array(): Array<T> = arrayOf(first, second, third)
inline fun <reified T> Quadruple <T, T, T, T>             .array(): Array<T> = arrayOf(a, b, c, d)
inline fun <reified T> Quintuple <T, T, T, T, T>          .array(): Array<T> = arrayOf(a, b, c, d, e)
inline fun <reified T> Sextuple  <T, T, T, T, T, T>       .array(): Array<T> = arrayOf(a, b, c, d, e, f)
inline fun <reified T> Septuple  <T, T, T, T, T, T, T>    .array(): Array<T> = arrayOf(a, b, c, d, e, f, g)
inline fun <reified T> Octuple   <T, T, T, T, T, T, T, T> .array(): Array<T> = arrayOf(a, b, c, d, e, f, g, h)

/* *************************************************************************************************************************************** */
/* **************************************************************** Triple-3 ************************************************************* */
/* *************************************************************************************************************************************** */
/**
 * [Any] `to` [Pair] = [Triple]
 */
infix fun
        <A, B, C>
        A.to(pair: Pair<B, C>): Triple<A, B, C> = Triple(this, pair.first, pair.second)

/**
 * [Pair] `to` [Any] = [Triple]
 */
infix fun
        <A, B, C>
        Pair<A, B>.to(third: C): Triple<A, B, C> = Triple(this.first, this.second, third)
/* *************************************************************************************************************************************** */
/* *************************************************************** Quadruple-4 *********************************************************** */
/* *************************************************************************************************************************************** */
/**
 * [Any] `to` [Triple] = [Quadruple]
 */
infix fun
        <A, B, C, D>
        A.to(triple: Triple<B, C, D>): Quadruple<A, B, C, D> = Quadruple(this, triple.first, triple.second, triple.third)

/**
 * [Pair] `to` [Pair] = [Quadruple]
 */
infix fun
        <A, B, C, D>
        Pair<A, B>.to(pair: Pair<C, D>): Quadruple<A, B, C, D> = Quadruple(this.first, this.second, pair.first, pair.second)

/**
 * [Triple] `to` [Any] = [Quadruple]
 */
infix fun
        <A, B, C, D>
        Triple<A, B, C>.to(fourth: D): Quadruple<A, B, C, D> = Quadruple(this.first, this.second, this.third, fourth)

/* *************************************************************************************************************************************** */
/* *************************************************************** Quintuple-5 *********************************************************** */
/* *************************************************************************************************************************************** */
/**
 * [Any] `to` [Quadruple] = [Quintuple]
 */
infix fun
        <A, B, C, D, E>
        A.to(quadruple: Quadruple<B, C, D, E>): Quintuple<A, B, C, D, E> = Quintuple(this, quadruple.a, quadruple.b, quadruple.c, quadruple.d)

/**
 * [Pair] `to` [Triple]  = [Quintuple]
 */
infix fun
        <A, B, C, D, E>
        Pair<A, B>.to(triple: Triple<C, D, E>): Quintuple<A, B, C, D, E> = Quintuple(this.first, this.second, triple.first, triple.second, triple.third)

/**
 * [Triple] `to` [Pair] = [Quintuple]
 */
infix fun
        <A, B, C, D, E>
        Triple<A, B, C>.to(pair: Pair<D, E>): Quintuple<A, B, C, D, E> = Quintuple(this.first, this.second, third, pair.first, pair.second)

/**
 * [Quadruple] `to` [Any] = [Quintuple]
 */
infix fun
        <A, B, C, D, E>
        Quadruple<A, B, C, D>.to(fifth: E): Quintuple<A, B, C, D, E> = Quintuple(this.a, this.b, this.c, this.d, fifth)

/* *************************************************************************************************************************************** */
/* *************************************************************** Sextuple-6 ************************************************************ */
/* *************************************************************************************************************************************** */
/**
 * [Any] `to` [Quintuple] = [Sextuple]
 */
infix fun
        <A, B, C, D, E, F>
        A.to(quintuple: Quintuple<B, C, D, E, F>): Sextuple<A, B, C, D, E, F> =
        Sextuple(this, quintuple.a, quintuple.b, quintuple.c, quintuple.d, quintuple.e)

/**
 * [Pair] `to` [Quadruple] = [Sextuple]
 */
infix fun
        <A, B, C, D, E, F>
        Pair<A, B>.to(qdrpl: Quadruple<C, D, E, F>): Sextuple<A, B, C, D, E, F> =
        Sextuple(this.first, this.second, qdrpl.a, qdrpl.b, qdrpl.c, qdrpl.d)

/**
 * [Triple] `to` [Triple] = [Sextuple]
 */
infix fun
        <A, B, C, D, E, F>
        Triple<A, B, C>.to(trpl: Triple<D, E, F>): Sextuple<A, B, C, D, E, F> =
        Sextuple(this.first, this.second, this.third, trpl.first, trpl.second, trpl.third)

/**
 * [Quadruple] `to` [Pair] = [Sextuple]
 */
infix fun
        <A, B, C, D, E, F>
        Quadruple<A, B, C, D>.to(pair: Pair<E, F>): Sextuple<A, B, C, D, E, F> =
        Sextuple(this.a, this.b, this.c, this.d, pair.first, pair.second)

/**
 * [Quintuple] `to` [Any] = [Sextuple]
 */
infix fun
        <A, B, C, D, E, F>
        Quintuple<A, B, C, D, E>.to(sixth: F): Sextuple<A, B, C, D, E, F> =
        Sextuple(this.a, this.b, this.c, this.d, e, sixth)
/* *************************************************************************************************************************************** */
/* *************************************************************** Septuple-7 ************************************************************ */
/* *************************************************************************************************************************************** */

/**
 * [1] to [6] = [7]
 * [Any] to [Sextuple] = [Septuple]
 */
infix fun
        <A, B, C, D, E, F, G>
        A.to(sxtpl: Sextuple<B, C, D, E, F, G>): Septuple<A, B, C, D, E, F, G> = Septuple(this, sxtpl.a, sxtpl.b, sxtpl.c, sxtpl.d, sxtpl.e, sxtpl.f)
/**
 * [2] to [5] = [7]
 * [Pair] to [Quintuple] = [Septuple]
 */
infix fun
        <A, B, C, D, E, F, G>
        Pair<A, B>.to(qntpl: Quintuple<C, D, E, F, G>): Septuple<A, B, C, D, E, F, G> = Septuple(first, second, qntpl.a, qntpl.b, qntpl.c, qntpl.d, qntpl.e)
/**
 * [3] to [4] = [7]
 * [Triple] to [Quadruple] = [Septuple]
 */
infix fun
        <A, B, C, D, E, F, G>
        Triple<A, B, C>.to(qdrple: Quadruple<D, E, F, G>): Septuple<A, B, C, D, E, F, G> = Septuple(first, second, third, qdrple.a, qdrple.b, qdrple.c, qdrple.d)

/**
 * [4] to [3] = [7]
 * [Quadruple] to [Triple] = [Septuple]
 */
infix fun
        <A, B, C, D, E, F, G>
        Quadruple<A, B, C, D>.to(triple: Triple<E, F, G>): Septuple<A, B, C, D, E, F, G> = Septuple(a, b, c, d, triple.first, triple.second, triple.third)

/**
 * [5] to [2] = [7]
 * [Quintuple] to [Pair] = [Septuple]
 */
infix fun
        <A, B, C, D, E, F, G>
        Quintuple<A, B, C, D, E>.to(pair: Pair<F, G>): Septuple<A, B, C, D, E, F, G> = Septuple(a, b, c, d, e, pair.first, pair.second)

/**
 * [6] to [1] = [7]
 * [Sextuple] to [Any] = [Septuple]
 */
infix fun
        <A, B, C, D, E, F, G>
        Sextuple<A, B, C, D, E, F>.to(sixth: G): Septuple<A, B, C, D, E, F, G> = Septuple(a, b, c, d, e, f, sixth)
