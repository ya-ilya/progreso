package org.progreso.api.friend.container

import org.progreso.api.common.Container
import org.progreso.api.friend.Friend

/**
 * Interface for friend containers
 */
interface FriendContainer : Container {
    val friends: MutableSet<Friend>

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
}