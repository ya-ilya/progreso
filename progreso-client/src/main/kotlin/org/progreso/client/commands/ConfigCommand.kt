package org.progreso.client.commands

import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import org.progreso.api.command.AbstractCommand
import org.progreso.api.command.arguments.ConfigArgumentType
import org.progreso.api.managers.ConfigManager

@AbstractCommand.Register("config")
object ConfigCommand : AbstractCommand() {
    override fun build(builder: LiteralArgumentBuilder<Any>) {
        for (category in ConfigManager.categories) {
            val literal = literal(category.name)
                .then(
                    literal("load").then(
                        argument("config", ConfigArgumentType(category)).executesSuccess { context ->
                            val config = ConfigArgumentType[context]

                            category.load(config.name)
                            infoLocalized(
                                "command.config.load",
                                config
                            )
                        }
                    )
                )
                .then(
                    literal("save").then(
                        argument("config", StringArgumentType.string()).executesSuccess { context ->
                            val config = StringArgumentType.getString(context, "config")

                            category.save(config)
                            infoLocalized(
                                "command.config.save",
                                config
                            )
                        }
                    ).executesSuccess {
                        category.save()

                        infoLocalized(
                            "command.config.save_many",
                            category.name
                        )
                    }
                )
                .then(
                    literal("refresh").then(
                        argument("config", ConfigArgumentType(category)).executesSuccess { context ->
                            val config = ConfigArgumentType[context]

                            category.refresh(config.name)
                            infoLocalized(
                                "command.config.refresh",
                                config
                            )
                        }
                    ).executesSuccess {
                        category.refresh()
                        infoLocalized(
                            "command.config.refresh_many",
                            category.name
                        )
                    }
                )
                .then(
                    literal("list").executesSuccess {
                        infoLocalized(
                            category.configs.ifEmpty("command.config.list", "command.config.list_empty"),
                            category.name,
                            category.configs.joinToString { it.name }
                        )
                    }
                )
                .executesSuccess {
                    infoLocalized(
                        "command.config.current",
                        category.name,
                        category.config
                    )
                }

            builder.then(literal)
        }
    }
}