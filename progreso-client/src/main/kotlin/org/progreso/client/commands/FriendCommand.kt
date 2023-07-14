package org.progreso.client.commands

import org.progreso.api.command.AbstractCommand
import org.progreso.api.command.argument.arguments.FriendArgumentType
import org.progreso.api.command.argument.arguments.StringArgumentType.Companion.string
import org.progreso.api.managers.FriendManager

@AbstractCommand.Register("friend")
object FriendCommand : AbstractCommand() {
    init {
        literal("add").argument("player", string()).executes { context ->
            val player: String by context

            if (FriendManager.isFriend(player)) {
                errorLocalized(
                    "command.friend.add_error",
                    player
                )
            } else {
                FriendManager.addFriendByName(player)
                infoLocalized(
                    "command.friend.add",
                    player
                )
            }
        }

        literal("remove").argument("friend", FriendArgumentType.create()).executes { context ->
            val friend = context.nullable<FriendManager.Friend>("friend") ?: return@executes

            FriendManager.removeFriendByName(friend.name)
            infoLocalized(
                "command.friend.remove",
                friend.name
            )
        }

        literal("list").executes { _ ->
            infoLocalized(
                "command.friend.list",
                FriendManager.friends.joinToString()
            )
        }
    }
}