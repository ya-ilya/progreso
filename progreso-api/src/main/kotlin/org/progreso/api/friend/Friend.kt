package org.progreso.api.friend

data class Friend(val name: String) {
    override fun toString(): String {
        return name
    }
}