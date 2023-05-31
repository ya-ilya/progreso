package org.progreso.api.command.argument.arguments

import org.progreso.api.Api
import org.progreso.api.command.argument.ArgumentType
import org.progreso.api.command.reader.StringReader
import org.progreso.api.managers.FriendManager

class FriendArgumentType : ArgumentType<FriendManager.Friend?> {
    companion object {
        fun create() = FriendArgumentType()
    }

    override val name = "friend"

    override fun parse(reader: StringReader): FriendManager.Friend? {
        val friendName = reader.readString()
        val friend = FriendManager.getFriendByNameOrNull(friendName)
        if (friend == null) Api.CHAT.error("Friend $friendName not found")
        return friend
    }

    override fun check(reader: StringReader): Boolean {
        return true
    }
}