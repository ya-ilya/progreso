package org.progreso.client.commands

import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.arguments.StringArgumentType.string
import org.progreso.api.alt.AltAccount
import org.progreso.api.command.AbstractCommand
import org.progreso.api.command.arguments.AltArgumentType
import org.progreso.api.managers.AltManager

@AbstractCommand.Register("alt")
object AltCommand : AbstractCommand() {
    init {
        builder.then(
            literal("add")
                .then(
                    literal("offline").then(
                        argument("name", string()).execute { context ->
                            val name = StringArgumentType.getString(context, "name")

                            AltManager.addAlt(AltAccount.Offline(name))
                            infoLocalized("command.alt.add", name)
                        }
                    )
                )
        )

        builder.then(
            literal("remove").then(
                argument("alt", AltArgumentType()).execute { context ->
                    val alt = AltArgumentType[context]

                    AltManager.removeAlt(alt)
                    infoLocalized("command.alt.remove", alt.username)
                }
            )
        )

        builder.then(literal("list").execute {
            infoLocalized(
                AltManager.alts.ifEmpty("command.alt.list", "command.alt.list_empty"),
                AltManager.alts.joinToString()
            )
        })
    }
}