package org.progreso.api.managers

object FriendManager {
    val friends = mutableListOf<Friend>()

    fun add(name: String) {
        friends.add(Friend(name))
    }

    fun remove(name: String) {
        friends.removeIf { it.name == name }
    }

    fun isFriend(name: String): Boolean {
        return friends.any { it.name == name }
    }

    operator fun get(name: String): Friend {
        return getOrNull(name)!!
    }

    fun getOrNull(name: String): Friend? {
        return friends.firstOrNull { it.name == name }
    }

    data class Friend(val name: String) {
        override fun toString(): String {
            return name
        }
    }
}