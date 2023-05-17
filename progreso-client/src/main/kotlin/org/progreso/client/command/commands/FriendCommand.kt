package org.progreso.client.command.commands

import org.progreso.api.command.argument.arguments.FriendArgumentType
import org.progreso.api.command.argument.arguments.StringArgumentType.Companion.string
import org.progreso.api.command.argument.ArgumentBuilder
import org.progreso.api.managers.FriendManager
import org.progreso.client.command.Command

class FriendCommand : Command("friend") {
    override fun build(builder: ArgumentBuilder) {
        builder.literal("add") {
            argument("player", string()).executes { context ->
                val player = context.get<String>("player")

                if (FriendManager.isFriend(player)) {
                    send("$player already your friend")
                } else {
                    FriendManager.addFriendByName(player)
                    send("$player now is your friend")
                }
            }
        }

        builder.literal("remove") {
            argument("friend", FriendArgumentType.create()).executes { context ->
                val friend = context.getNullable<FriendManager.Friend>("friend") ?: return@executes

                FriendManager.removeFriendByName(friend.name)
                send("${friend.name} now isn't your friend")
            }
        }

        builder.literal("list").executes { _ ->
            send("Friends: ${FriendManager.friends.joinToString()}")
        }
    }
}