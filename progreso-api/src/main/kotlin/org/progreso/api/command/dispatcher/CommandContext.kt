package org.progreso.api.command.dispatcher

import kotlin.reflect.KProperty

@Suppress("UNCHECKED_CAST")
class CommandContext {
    private val arguments = mutableMapOf<String, Any?>()

    operator fun set(name: String, value: Any?) {
        arguments[name] = value
    }

    fun <T> get(name: String): T {
        return arguments[name] as T
    }

    fun <T> nullable(name: String): T? {
        return arguments[name] as T?
    }

    operator fun <T : Any> getValue(thisRef: Any?, property: KProperty<*>): T {
        return get(property.name)
    }
}