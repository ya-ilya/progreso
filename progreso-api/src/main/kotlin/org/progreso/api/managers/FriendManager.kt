package org.progreso.api.managers

object FriendManager {
    val friends = mutableListOf<String>()

    fun isFriend(name: String): Boolean {
        return friends.contains(name)
    }
}