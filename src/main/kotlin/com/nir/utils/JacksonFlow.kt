package com.nir.utils

import com.fasterxml.jackson.core.JsonFactory
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonToken
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.nir.beans.Beans
import com.nir.ui.dto.MethodInfo
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import java.io.File

fun main() = runBlocking {
    val filename = "json/butchers-tables.json"
    val flow = JacksonFlow(filename, MethodInfo::class.java).flow()

    flow.collect {
        println(it)
    }
}

class JacksonFlow<T : Any>(filename: String, private val type: Class<T>) {
    private val codec: ObjectMapper
    private val jsonParser: JsonParser

    init {
        val resource = JacksonFlow::class.java.classLoader.getResource(filename)!!
        val file = File(resource.toURI())
        jsonParser = JsonFactory().createParser(file)
        codec = Beans.kotlinObjectMapper()
        jsonParser.codec = codec
    }

    fun flow() = generateSequence { this.next() }.asFlow()


    operator fun next(): T? {
        val stringBuilder = StringBuilder()
        return parseNext(stringBuilder)
    }

    private fun parseNext(stringBuilder: StringBuilder): T? {
        val token = nextToken(jsonParser)
        if (token.isArrayStart()) {
            if (nextToken(jsonParser).isObjectStart()) {
                return parse(stringBuilder)
            }
        }

        if (token.isObjectStart()) {
            return parse(stringBuilder)
        }

        if (token.isScalarValue) {
            token.areValues(stringBuilder, jsonParser)
        }

        if (token.isArrayEnd()) {
            return null
        }
        throw RuntimeException("Token is not Array, not object, not scalar. What type is it?")
    }

    private fun parse(stringBuilder: StringBuilder): T {
        parseObject(stringBuilder, jsonParser)
        val toString = stringBuilder.toString()
        return codec.readValue(toString, type)
    }

    private fun parseObject(stringBuilder: StringBuilder, jsonParser: JsonParser) {
        stringBuilder.append("{")
        var counterOfStart = 1
        var counterOfEnd = 0
        do {
            val token = nextToken(jsonParser)
            token.isObjectStart().ifTrue {
                counterOfStart++
                stringBuilder.append("{")
            }
            token.isObjectEnd().ifTrue {
                counterOfEnd++
                stringBuilder.addEndObject()
            }
            token.isFieldName(stringBuilder, jsonParser)
            token.areValues(stringBuilder, jsonParser)
            token.isArrayStart(stringBuilder, jsonParser)
        } while (counterOfStart != counterOfEnd)
    }

    private fun JsonToken.isArrayStart(
            stringBuilder: StringBuilder,
            jsonParser: JsonParser
    ) {
        this.isArrayStart().ifTrue {
            parseArray(stringBuilder, jsonParser)
        }
    }

    fun parseArray(stringBuilder: StringBuilder, jsonParser: JsonParser) {
        stringBuilder.append("[")
        var counterOfStart = 1
        var counterOfEnd = 0
        do {
            val token = nextToken(jsonParser)
            token.isArrayStart().ifTrue {
                counterOfStart++
                parseArray(stringBuilder, jsonParser)
                counterOfEnd++
            }
            token.isArrayEnd().ifTrue {
                counterOfEnd++
                stringBuilder.addEndArray()
            }
            token.isFieldName(stringBuilder, jsonParser)
            token.areValues(stringBuilder, jsonParser)
            token.isObjectStart().ifTrue {
                parseObject(stringBuilder, this.jsonParser)
            }
        } while (counterOfStart != counterOfEnd)
    }
}

