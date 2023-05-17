package org.progreso.api.command.dispatcher

@Suppress("UNCHECKED_CAST")
class CommandContext {
    private val arguments = mutableMapOf<String, Any?>()

    fun putArgument(name: String, value: Any?) {
        arguments[name] = value
    }

    fun <T> get(name: String): T {
        return arguments[name] as T
    }

    fun <T> getNullable(name: String): T? {
        return arguments[name] as T?
    }
}