package org.progreso.client.commands

import com.mojang.brigadier.arguments.StringArgumentType
import org.progreso.api.command.AbstractCommand
import org.progreso.api.command.arguments.ConfigArgumentType
import org.progreso.api.managers.ConfigManager

@AbstractCommand.Register("config")
object ConfigCommand : AbstractCommand() {
    init {
        for (category in ConfigManager.categories) {
            val literal = literal(category.name)
                .then(
                    literal("load").then(
                        argument("config", ConfigArgumentType(category)).execute { context ->
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
                        argument("config", StringArgumentType.string()).execute { context ->
                            val config = StringArgumentType.getString(context, "config")

                            category.save(config)
                            infoLocalized(
                                "command.config.save",
                                config
                            )
                        }
                    ).execute {
                        category.save()

                        infoLocalized(
                            "command.config.save_many",
                            category.name
                        )
                    }
                )
                .then(
                    literal("refresh").then(
                        argument("config", ConfigArgumentType(category)).execute { context ->
                            val config = ConfigArgumentType[context]

                            category.refresh(config.name)
                            infoLocalized(
                                "command.config.refresh",
                                config
                            )
                        }
                    ).execute {
                        category.refresh()
                        infoLocalized(
                            "command.config.refresh_many",
                            category.name
                        )
                    }
                )
                .then(
                    literal("list").execute {
                        infoLocalized(
                            category.configs.ifEmpty("command.config.list", "command.config.list_empty"),
                            category.name,
                            category.configs.joinToString { it.name }
                        )
                    }
                )
                .execute {
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