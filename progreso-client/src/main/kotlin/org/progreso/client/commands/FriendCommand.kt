package org.progreso.client.commands

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import org.progreso.api.command.AbstractCommand
import org.progreso.api.command.arguments.FriendArgumentType
import org.progreso.api.managers.FriendManager
import org.progreso.client.commands.arguments.PlayerArgumentType

@AbstractCommand.Register("friend")
object FriendCommand : AbstractCommand() {
    override fun build(builder: LiteralArgumentBuilder<Any>) {
        builder.then(
            literal("add").then(
                argument("player", PlayerArgumentType()).executesSuccess { context ->
                    val player = PlayerArgumentType[context]

                    if (FriendManager.isFriend(player.profile.name)) {
                        errorLocalized(
                            "command.friend.add_error",
                            player
                        )
                    } else {
                        FriendManager.addFriendByName(player.profile.name)
                        infoLocalized(
                            "command.friend.add",
                            player
                        )
                    }
                }
            )
        )

        builder.then(
            literal("remove").then(
                argument("friend", FriendArgumentType()).executesSuccess { context ->
                    val friend = FriendArgumentType[context]

                    FriendManager.removeFriendByName(friend.name)
                    infoLocalized(
                        "command.friend.remove",
                        friend.name
                    )
                }
            )
        )

        builder.then(literal("list").executesSuccess {
            infoLocalized(
                "command.friend.list",
                FriendManager.friends.joinToString()
            )
        })
    }
}