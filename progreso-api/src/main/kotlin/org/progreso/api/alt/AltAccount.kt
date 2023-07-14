package org.progreso.api.alt

sealed class AltAccount(val username: String) {
    val type: String = javaClass.simpleName

    class Cracked(username: String) : AltAccount(username)
}