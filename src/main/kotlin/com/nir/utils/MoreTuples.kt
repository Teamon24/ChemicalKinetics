package com.nir.utils

data class Fourfold<out A, out B, out C, out D>
constructor(val a: A, val b: B, val c: C, val d: D) {
    override fun toString(): String = s(arrayOf(a, b, c, d))
    companion object {
        @JvmStatic
        fun<A, B, C, D> of(a: A, b: B, c: C, d: D): Fourfold<A, B, C, D> = Fourfold(a, b, c, d)
    }
}

data class Fivefold<out A, out B, out C, out D, out E>
constructor(val a: A, val b: B, val c: C, val d: D, val e: E) {
    override fun toString(): String = s(arrayOf(a, b, c, d, e))

}

data class Sixfold<A, B, C, D, E, F>
constructor(val a: A, val b: B, val c: C, val d: D, val e: E, val f: F) {
    override fun toString(): String = s(arrayOf(a, b, c, d, e, f))
}

data class Sevenfold<A, B, C, D, E, F, G>
constructor(val a: A, val b: B, val c: C, val d: D, val e: E, val f: F, val g: G) {
    override fun toString(): String = s(arrayOf(a, b, c, d, e, f, g))
}

data class Eightfold<A, B, C, D, E, F, G, H>
constructor(val a: A, val b: B, val c: C, val d: D, val e: E, val f: F, val g: G, val h: H) {
    override fun toString(): String = s(arrayOf(a, b, c, d, e, f, g, h))
}

data class Ninefold<A, B, C, D, E, F, G, H, I>
constructor(val a: A, val b: B, val c: C, val d: D, val e: E, val f: F, val g: G, val h: H, val i: I) {
    override fun toString(): String = s(arrayOf(a, b, c, d, e, f, g, h, i))
}

private fun s(anys: Array<*>): String = "(${anys.joinToString(separator = ", ", truncated = "") { it.toString() }})"


/**
 * Converts this quintuple into history list.
 */
fun <T> Pair      <T, T>                        .list(): List<T> = listOf(first, second)
fun <T> Triple    <T, T, T>                     .list(): List<T> = listOf(first, second, third)
fun <T> Fourfold  <T, T, T, T>                  .list(): List<T> = listOf(a, b, c, d)
fun <T> Fivefold  <T, T, T, T, T>               .list(): List<T> = listOf(a, b, c, d, e)
fun <T> Sixfold   <T, T, T, T, T, T>            .list(): List<T> = listOf(a, b, c, d, e, f)
fun <T> Sevenfold <T, T, T, T, T, T, T>         .list(): List<T> = listOf(a, b, c, d, e, f, g)
fun <T> Eightfold <T, T, T, T, T, T, T, T>      .list(): List<T> = listOf(a, b, c, d, e, f, g, h)
fun <T> Ninefold  <T, T, T, T, T, T, T, T, T>   .list(): List<T> = listOf(a, b, c, d, e, f, g, h, i)

inline fun <reified T> Pair      <T, T>                      .array(): Array<T> = arrayOf(first, second)
inline fun <reified T> Triple    <T, T, T>                   .array(): Array<T> = arrayOf(first, second, third)
inline fun <reified T> Fourfold  <T, T, T, T>                .array(): Array<T> = arrayOf(a, b, c, d)
inline fun <reified T> Fivefold  <T, T, T, T, T>             .array(): Array<T> = arrayOf(a, b, c, d, e)
inline fun <reified T> Sixfold   <T, T, T, T, T, T>          .array(): Array<T> = arrayOf(a, b, c, d, e, f)
inline fun <reified T> Sevenfold <T, T, T, T, T, T, T>       .array(): Array<T> = arrayOf(a, b, c, d, e, f, g)
inline fun <reified T> Eightfold <T, T, T, T, T, T, T, T>    .array(): Array<T> = arrayOf(a, b, c, d, e, f, g, h)
inline fun <reified T> Ninefold  <T, T, T, T, T, T, T, T, T> .array(): Array<T> = arrayOf(a, b, c, d, e, f, g, h, i)

/* *************************************************************************************************************************************** */
/* **************************************************************** Triple ************************************************************* */
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
/* ***************************************************************** Fourfold ************************************************************ */
/* *************************************************************************************************************************************** */
/**
 * [Any] `to` [Triple] = [Fourfold]
 */
infix fun
        <A, B, C, D>
        A.to(triple: Triple<B, C, D>): Fourfold<A, B, C, D> = Fourfold(this, triple.first, triple.second, triple.third)

/**
 * [Pair] `to` [Pair] = [Fourfold]
 */
infix fun
        <A, B, C, D>
        Pair<A, B>.to(pair: Pair<C, D>): Fourfold<A, B, C, D> = Fourfold(this.first, this.second, pair.first, pair.second)

/**
 * [Triple] `to` [Any] = [Fourfold]
 */
infix fun
        <A, B, C, D>
        Triple<A, B, C>.to(fourth: D): Fourfold<A, B, C, D> = Fourfold(this.first, this.second, this.third, fourth)

/* *************************************************************************************************************************************** */
/* *************************************************************** Fivefold ************************************************************** */
/* *************************************************************************************************************************************** */
/**
 * [Any] `to` [Fourfold] = [Fivefold]
 */
infix fun
        <A, B, C, D, E>
        A.to(fourfold: Fourfold<B, C, D, E>): Fivefold<A, B, C, D, E> = Fivefold(this, fourfold.a, fourfold.b, fourfold.c, fourfold.d)

/**
 * [Pair] `to` [Triple]  = [Fivefold]
 */
infix fun
        <A, B, C, D, E>
        Pair<A, B>.to(triple: Triple<C, D, E>): Fivefold<A, B, C, D, E> = Fivefold(this.first, this.second, triple.first, triple.second, triple.third)

/**
 * [Triple] `to` [Pair] = [Fivefold]
 */
infix fun
        <A, B, C, D, E>
        Triple<A, B, C>.to(pair: Pair<D, E>): Fivefold<A, B, C, D, E> = Fivefold(this.first, this.second, third, pair.first, pair.second)

/**
 * [Fourfold] `to` [Any] = [Fivefold]
 */
infix fun
        <A, B, C, D, E>
        Fourfold<A, B, C, D>.to(fifth: E): Fivefold<A, B, C, D, E> = Fivefold(this.a, this.b, this.c, this.d, fifth)

/* *************************************************************************************************************************************** */
/* *************************************************************** Sixfold *************************************************************** */
/* *************************************************************************************************************************************** */
/**
 * [Any] `to` [Fivefold] = [Sixfold]
 */
infix fun
        <A, B, C, D, E, F>
        A.to(fivefold: Fivefold<B, C, D, E, F>): Sixfold<A, B, C, D, E, F> =
        Sixfold(this, fivefold.a, fivefold.b, fivefold.c, fivefold.d, fivefold.e)

/**
 * [Pair] `to` [Fourfold] = [Sixfold]
 */
infix fun
        <A, B, C, D, E, F>
        Pair<A, B>.to(qdrpl: Fourfold<C, D, E, F>): Sixfold<A, B, C, D, E, F> =
        Sixfold(this.first, this.second, qdrpl.a, qdrpl.b, qdrpl.c, qdrpl.d)

/**
 * [Triple] `to` [Triple] = [Sixfold]
 */
infix fun
        <A, B, C, D, E, F>
        Triple<A, B, C>.to(trpl: Triple<D, E, F>): Sixfold<A, B, C, D, E, F> =
        Sixfold(this.first, this.second, this.third, trpl.first, trpl.second, trpl.third)

/**
 * [Fourfold] `to` [Pair] = [Sixfold]
 */
infix fun
        <A, B, C, D, E, F>
        Fourfold<A, B, C, D>.to(pair: Pair<E, F>): Sixfold<A, B, C, D, E, F> =
        Sixfold(this.a, this.b, this.c, this.d, pair.first, pair.second)

/**
 * [Fivefold] `to` [Any] = [Sixfold]
 */
infix fun
        <A, B, C, D, E, F>
        Fivefold<A, B, C, D, E>.to(sixth: F): Sixfold<A, B, C, D, E, F> =
        Sixfold(this.a, this.b, this.c, this.d, e, sixth)

/* *************************************************************************************************************************************** */
/* *************************************************************** Sevenfold ************************************************************* */
/* *************************************************************************************************************************************** */

/**
 * [1] to [6] = [7]
 * [Any] to [Sixfold] = [Sevenfold]
 */
infix fun
        <A, B, C, D, E, F, G>
        A.to(sxtpl: Sixfold<B, C, D, E, F, G>): Sevenfold<A, B, C, D, E, F, G> = Sevenfold(this, sxtpl.a, sxtpl.b, sxtpl.c, sxtpl.d, sxtpl.e, sxtpl.f)
/**
 * [2] to [5] = [7]
 * [Pair] to [Fivefold] = [Sevenfold]
 */
infix fun
        <A, B, C, D, E, F, G>
        Pair<A, B>.to(qntpl: Fivefold<C, D, E, F, G>): Sevenfold<A, B, C, D, E, F, G> = Sevenfold(first, second, qntpl.a, qntpl.b, qntpl.c, qntpl.d, qntpl.e)
/**
 * [3] to [4] = [7]
 * [Triple] to [Fourfold] = [Sevenfold]
 */
infix fun
        <A, B, C, D, E, F, G>
        Triple<A, B, C>.to(qdrple: Fourfold<D, E, F, G>): Sevenfold<A, B, C, D, E, F, G> = Sevenfold(first, second, third, qdrple.a, qdrple.b, qdrple.c, qdrple.d)

/**
 * [4] to [3] = [7]
 * [Fourfold] to [Triple] = [Sevenfold]
 */
infix fun
        <A, B, C, D, E, F, G>
        Fourfold<A, B, C, D>.to(triple: Triple<E, F, G>): Sevenfold<A, B, C, D, E, F, G> = Sevenfold(a, b, c, d, triple.first, triple.second, triple.third)

/**
 * [5] to [2] = [7]
 * [Fivefold] to [Pair] = [Sevenfold]
 */
infix fun
        <A, B, C, D, E, F, G>
        Fivefold<A, B, C, D, E>.to(pair: Pair<F, G>): Sevenfold<A, B, C, D, E, F, G> = Sevenfold(a, b, c, d, e, pair.first, pair.second)

/**
 * [6] to [1] = [7]
 * [Sixfold] to [Any] = [Sevenfold]
 */
infix fun
        <A, B, C, D, E, F, G>
        Sixfold<A, B, C, D, E, F>.to(sixth: G): Sevenfold<A, B, C, D, E, F, G> = Sevenfold(a, b, c, d, e, f, sixth)

/**
 * [7] to [1] = [8]
 * [Sevenfold] to [Any] = [Eightfold]
 */
infix fun
        <A,B,C,D,E,F,G,H>
        Sevenfold<A,B,C,D,E,F,G>.to(seventh: H): Eightfold<A,B,C,D,E,F,G,H> = Eightfold(a, b, c, d, e, f, g, seventh)

/**
 * [8] to [1] = [9]
 * [Eightfold] to [Any] = [Ninefold]
 */
infix fun
        <A,B,C,D,E,F,G,H,I>
        Eightfold<A,B,C,D,E,F,G,H>.to(eighth: I): Ninefold<A,B,C,D,E,F,G,H,I> = Ninefold(a, b, c, d, e, f, g, h, eighth)
