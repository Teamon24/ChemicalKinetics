package com.nir.beans.reaction.parsing

import com.fasterxml.jackson.core.JsonFactory
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonToken
import com.nir.beans.Beans
import com.nir.beans.addEndArray
import com.nir.beans.addEndObject
import com.nir.beans.areValues
import com.nir.beans.isArrayEnd
import com.nir.beans.isArrayStart
import com.nir.beans.isFieldName
import com.nir.beans.isObjectEnd
import com.nir.beans.isObjectStart
import com.nir.beans.method.jsonPojos.ExplicitRKGeneralizedMethodJsonPojo
import com.nir.beans.nextToken
import com.nir.utils.ifTrue
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import java.io.File
import kotlinx.coroutines.flow.Flow

fun main() = runBlocking {
    val filename = "json/runge-kutta.json"
    val jacksonComponent = JacksonComponent(filename, ExplicitRKGeneralizedMethodJsonPojo::class.java)
    val flow = jacksonComponent.flow()

    flow.collect {
        println(it)
    }

    val all = jacksonComponent.readAll()
    println(all)
}

class JacksonComponent<T : Any>(filename: String, private val type: Class<T>) {
    private var file: File
    private val codec = Beans.kotlinObjectMapper()

    init {
        val resource = JacksonComponent::class.java.classLoader.getResource(filename)!!
        file = File(resource.toURI())
    }

    private fun createParser(file: File): JsonParser {
        val jsonParser = JsonFactory().createParser(file)
        jsonParser.codec = codec
        return jsonParser
    }

    fun flow(): Flow<T> {
        val jsonParser = createParser(file)
        return generateSequence { this.next(jsonParser) }.asFlow()
    }

    fun readAll(): List<T> {
        val javaType = codec.typeFactory.constructCollectionType(List::class.java, type);
        return codec.readValue(file, javaType);
    }


    fun next(jsonParser: JsonParser): T? {
        val stringBuilder = StringBuilder()
        return parseNext(stringBuilder, jsonParser)
    }

    private fun parseNext(stringBuilder: StringBuilder, jsonParser: JsonParser): T? {
        val token = nextToken(jsonParser)
        if (token.isArrayStart()) {
            if (nextToken(jsonParser).isObjectStart()) {
                return parse(stringBuilder, jsonParser)
            }
        }

        if (token.isObjectStart()) {
            return parse(stringBuilder, jsonParser)
        }

        if (token.isScalarValue) {
            token.areValues(stringBuilder, jsonParser)
        }

        if (token.isArrayEnd()) {
            return null
        }
        throw RuntimeException("Token is not Array, not object, not scalar. What type is it?")
    }

    private fun parse(stringBuilder: StringBuilder, jsonParser: JsonParser): T {
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
                parseObject(stringBuilder, jsonParser)
            }
        } while (counterOfStart != counterOfEnd)
    }
}

