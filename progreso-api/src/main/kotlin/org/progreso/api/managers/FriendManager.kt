package org.progreso.api.managers

object FriendManager {
    val friends = mutableSetOf<Friend>()

    fun addFriendByName(name: String) {
        friends.add(Friend(name))
    }

    fun removeFriendByName(name: String) {
        friends.removeIf { it.name == name }
    }

    fun isFriend(name: String): Boolean {
        return friends.any { it.name == name }
    }

    fun getFriendByName(name: String): Friend {
        return getFriendByNameOrNull(name)!!
    }

    fun getFriendByNameOrNull(name: String): Friend? {
        return friends.firstOrNull { it.name == name }
    }

    data class Friend(val name: String) {
        override fun toString(): String {
            return name
        }
    }
}