package org.progreso.client.command.commands

import org.progreso.api.command.argument.arguments.FriendArgumentType
import org.progreso.api.command.argument.arguments.StringArgumentType.Companion.string
import org.progreso.api.managers.FriendManager
import org.progreso.client.command.Command

object FriendCommand : Command("friend") {
    init {
        literal("add") {
            argument("player", string()).executes { context ->
                val player: String by context

                if (FriendManager.isFriend(player)) {
                    error("$player already your friend")
                } else {
                    FriendManager.addFriendByName(player)
                    info("$player now is your friend")
                }
            }
        }

        literal("remove") {
            argument("friend", FriendArgumentType.create()).executes { context ->
                val friend = context.getNullable<FriendManager.Friend>("friend") ?: return@executes

                FriendManager.removeFriendByName(friend.name)
                info("${friend.name} now isn't your friend")
            }
        }

        literal("list").executes { _ ->
            info("Friends: ${FriendManager.friends.joinToString()}")
        }
    }
}