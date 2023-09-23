package org.progreso.api.alt

sealed class AltAccount(val username: String) {
    val type: String = javaClass.simpleName

    override fun toString(): String {
        return username
    }

    class Offline(username: String) : AltAccount(username)
}