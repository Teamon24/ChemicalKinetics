package com.nir.utils.math

import kotlin.reflect.KClass

abstract class Type<T> (
        val init:  (    ) -> T,
        val plus:  (T, T) -> T,
        val minus: (T, T) -> T,
        val times: (T, T) -> T
) {
    abstract fun clazz(): KClass<out Any>
}

object Doubles: Type<Double>({ 0.0 }, Double::plus, Double::minus, Double::times) {
    override fun clazz() = Double::class
}

object Integers: Type<Int>({ 0 }, Int::plus, Int::minus, Int::times) {
    override fun clazz() = Int::class
}

object Longs: Type<Long>({ 0L }, Long::plus, Long::minus, Long::times) {
    override fun clazz() = Long::class
}