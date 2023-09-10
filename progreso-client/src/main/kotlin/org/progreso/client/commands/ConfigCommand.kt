package org.progreso.client.commands

import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import org.progreso.api.command.AbstractCommand
import org.progreso.api.managers.ConfigManager

@AbstractCommand.Register("config")
object ConfigCommand : AbstractCommand() {
    override fun build(builder: LiteralArgumentBuilder<Any>) {
        for (category in ConfigManager.categories) {
            val literal = literal(category.name)
                .then(
                    literal("load").then(
                        argument("config", StringArgumentType.string()).executesSuccess { context ->
                            val config = StringArgumentType.getString(context, "config")

                            try {
                                category.load(config)
                            } catch (ex: Exception) {
                                errorLocalized(
                                    "command.config.load_error",
                                    config
                                )
                                return@executesSuccess
                            }

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
                        argument("config", StringArgumentType.string()).executesSuccess { context ->
                            val config = StringArgumentType.getString(context, "config")

                            category.refresh(config)
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
                            "command.config.list",
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