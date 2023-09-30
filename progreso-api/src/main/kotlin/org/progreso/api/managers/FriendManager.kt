package org.progreso.api.managers

import org.progreso.api.friend.Friend
import org.progreso.api.friend.container.FriendContainer

object FriendManager : FriendContainer {
    override val friends = mutableSetOf<Friend>()
}