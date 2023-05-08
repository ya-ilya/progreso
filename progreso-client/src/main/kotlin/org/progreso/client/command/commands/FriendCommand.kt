package org.progreso.client.command.commands

import com.mojang.brigadier.Command.SINGLE_SUCCESS
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import org.progreso.api.command.arguments.FriendArgumentType
import org.progreso.api.managers.FriendManager
import org.progreso.client.command.Command

class FriendCommand : Command("friend") {
    override fun build(builder: LiteralArgumentBuilder<Any>) {
        builder.then(literal("add").then(
            argument("player", StringArgumentType.string()).executes { context ->
                val player = StringArgumentType.getString(context, "player")

                if (FriendManager.isFriend(player)) {
                    send("$player already your friend")
                } else {
                    FriendManager.add(player)
                    send("$player now is your friend")
                }

                return@executes SINGLE_SUCCESS
            }
        ))

        builder.then(literal("remove").then(
            argument("friend", FriendArgumentType.create()).executes { context ->
                val friend = FriendArgumentType.get(context) ?: return@executes SINGLE_SUCCESS

                FriendManager.remove(friend.name)
                send("${friend.name} now isn't your friend")

                return@executes SINGLE_SUCCESS
            }
        ))

        builder.then(literal("list").executes {
            send("Friends: ${FriendManager.friends.joinToString()}")

            return@executes SINGLE_SUCCESS
        })
    }
}