package com.nir.beans

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonToken
import com.nir.utils.delete

/**
 *
 */
fun JsonToken.isArrayStart(): Boolean {
    return this == JsonToken.START_ARRAY
}

fun JsonToken.isArrayEnd(): Boolean {
    return this == JsonToken.END_ARRAY
}

fun JsonToken.isObjectStart(): Boolean {
    return this == JsonToken.START_OBJECT
}

fun JsonToken.isObjectEnd(): Boolean {
    return this == JsonToken.END_OBJECT
}


fun StringBuilder.addEndObject() {
    val last = this.last()
    if (last == ',') {
        this.deleteCharAt(this.length - 1)
    }
    this.append("}").append(",")
}

fun StringBuilder.addEndArray() {
    if (this.last() == ',') {
        this.deleteCharAt(this.length - 1)
    }
    this.append("]")
}




fun nextToken(jsonParser: JsonParser): JsonToken = jsonParser.nextToken() ?: jsonParser.nextToken()

fun JsonToken.isFieldName(stringBuilder: StringBuilder, jsonParser: JsonParser) {
    val length = stringBuilder.length
    val str = stringBuilder[length - 1]
    if (str == ']') {
        stringBuilder.append(",")
    }
    if (this == JsonToken.FIELD_NAME) {
        stringBuilder.append("\"${jsonParser.text}\":")
    }
}

fun JsonToken.isNull(stringBuilder: StringBuilder, jsonParser: JsonParser) {
    if (this == JsonToken.VALUE_NULL) {
        stringBuilder.append("${jsonParser.text},".delete("\""))
    }
}

fun JsonToken.isFloat(stringBuilder: StringBuilder, jsonParser: JsonParser) {
    if (this == JsonToken.VALUE_NUMBER_FLOAT) {
        stringBuilder.append("${jsonParser.text},")
    }
}

fun JsonToken.isInt(stringBuilder: StringBuilder, jsonParser: JsonParser) {
    if (this == JsonToken.VALUE_NUMBER_INT) {
        stringBuilder.append("${jsonParser.text.delete("\"")},")
    }
}

fun JsonToken.isTrue(stringBuilder: StringBuilder, jsonParser: JsonParser) {
    if (this == JsonToken.VALUE_TRUE) {
        stringBuilder.append("${jsonParser.text},")
    }
}

fun JsonToken.isFalse(stringBuilder: StringBuilder, jsonParser: JsonParser) {
    if (this == JsonToken.VALUE_FALSE) {
        stringBuilder.append("${jsonParser.text},")
    }
}

fun JsonToken.isString(stringBuilder: StringBuilder, jsonParser: JsonParser) {
    if (this == JsonToken.VALUE_STRING) {
        stringBuilder.append("\"${jsonParser.text.replace("\"", "\\\"")}\",")
    }
}


fun JsonToken.areValues(stringBuilder: StringBuilder, jsonParser: JsonParser) {
    this.isNull(stringBuilder, jsonParser)
    this.isString(stringBuilder, jsonParser)
    this.isFloat(stringBuilder, jsonParser)
    this.isInt(stringBuilder, jsonParser)
    this.isTrue(stringBuilder, jsonParser)
    this.isFalse(stringBuilder, jsonParser)
}