package org.progreso.api.command.reader

class StringReader(string: String) {
    private var arguments = string.split(" ").filter { it.isNotEmpty() }

    fun hasNext(): Boolean {
        return arguments.isNotEmpty()
    }

    fun peek() = peek(1)[0]

    fun peek(count: Int = 1): List<String> {
        return arguments.slice(0 until count)
    }

    fun readString(): String {
        return arguments[0].also {
            arguments = arguments.drop(1)
        }
    }
}