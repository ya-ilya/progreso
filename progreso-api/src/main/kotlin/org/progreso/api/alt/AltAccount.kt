package org.progreso.api.alt

sealed class AltAccount(val username: String) {
    val type: String = javaClass.simpleName

    class Offline(username: String) : AltAccount(username)
}