package org.progreso.client.command.commands

import org.progreso.api.managers.FriendManager
import org.progreso.client.command.Command

class FriendCommand : Command("friend") {
    override fun execute(args: List<String>) {
        if (args.isEmpty()) {
            return send("Not enough arguments. Correct usage - friend <add/del/list> [name]")
        }

        when (args[0].lowercase()) {
            "add" -> {
                if (args.size < 2) {
                    return send("Not enough arguments. Correct usage - friend add [name]")
                }

                val name = args[1]

                if (FriendManager.isFriend(name)) {
                    send("$name already your friend")
                } else {
                    FriendManager.friends.add(name)
                    send("$name now is your friend")
                }
            }

            "del" -> {
                if (args.size < 2) {
                    return send("Not enough arguments. Correct usage - friend del [name]")
                }

                val name = args[1]

                if (FriendManager.isFriend(name)) {
                    FriendManager.friends.remove(name)
                    send("$name now isn't your friend")
                } else {
                    send("$name isn't your friend")
                }
            }

            "list" -> {
                send("Friends: ${FriendManager.friends.joinToString()}")
            }

            else -> {
                send("Unknown argument type - ${args[0]}. Expected add/del/list")
            }
        }
    }
}