package org.progreso.client.commands

import org.progreso.api.command.AbstractCommand
import org.progreso.api.command.arguments.FriendArgumentType
import org.progreso.api.managers.FriendManager
import org.progreso.client.commands.arguments.PlayerArgumentType

@AbstractCommand.Register("friend")
object FriendCommand : AbstractCommand() {
    init {
        builder.then(
            literal("add").then(
                argument("player", PlayerArgumentType()).execute { context ->
                    val player = PlayerArgumentType[context]

                    if (FriendManager.isFriend(player.profile.name)) {
                        errorLocalized(
                            "command.friend.add_error",
                            player.profile.name
                        )
                    } else {
                        FriendManager.addFriendByName(player.profile.name)
                        infoLocalized(
                            "command.friend.add",
                            player.profile.name
                        )
                    }
                }
            )
        )

        builder.then(
            literal("remove").then(
                argument("friend", FriendArgumentType()).execute { context ->
                    val friend = FriendArgumentType[context]

                    FriendManager.removeFriendByName(friend.name)
                    infoLocalized(
                        "command.friend.remove",
                        friend.name
                    )
                }
            )
        )

        builder.then(literal("list").execute {
            infoLocalized(
                FriendManager.friends.ifEmpty("command.friend.list", "command.friend.list_empty"),
                FriendManager.friends.joinToString()
            )
        })
    }
}