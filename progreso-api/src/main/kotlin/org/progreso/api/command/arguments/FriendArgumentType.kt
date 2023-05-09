package org.progreso.api.command.arguments

import com.mojang.brigadier.StringReader
import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.context.CommandContext
import org.progreso.api.Api
import org.progreso.api.managers.FriendManager

class FriendArgumentType : ArgumentType<String> {
    companion object {
        fun create() = FriendArgumentType()

        fun get(context: CommandContext<*>): FriendManager.Friend? {
            val friendName = context.getArgument("friend", String::class.java)
            val friend = FriendManager.getFriendByNameOrNull(friendName)
            if (friend == null) Api.CHAT.send("Friend $friendName not found")
            return friend
        }
    }

    override fun parse(reader: StringReader): String {
        return reader.readString()
    }
}