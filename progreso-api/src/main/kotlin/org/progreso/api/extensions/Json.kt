package org.progreso.api.extensions

import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter

fun <T> JsonReader.iterateObjectMap(block: JsonReader.(String) -> T): List<T> {
    val result = mutableListOf<T>()

    while (hasNext()) {
        val name = nextName()
        result.add(readObject { block(name) })
    }

    return result
}

fun <T> JsonReader.iterateArray(block: JsonReader.() -> T): List<T> {
    val result = mutableListOf<T>()

    while (hasNext()) {
        result.add(block())
    }

    return result
}

fun <T> JsonReader.readObject(block: JsonReader.() -> T): T {
    beginObject()
    val result = block()
    endObject()

    return result
}

fun <T> JsonReader.readArray(block: JsonReader.() -> T): T {
    beginArray()
    val result = block()
    endArray()

    return result
}

fun <T> JsonWriter.writeObject(block: JsonWriter.() -> T): T {
    beginObject()
    val result = block()
    endObject()

    return result
}

fun <T> JsonWriter.writeArray(block: JsonWriter.() -> T): T {
    beginArray()
    val result = block()
    endArray()

    return result
}