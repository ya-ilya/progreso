package org.progreso.client.commands

import org.progreso.api.command.AbstractCommand
import org.progreso.api.command.argument.arguments.StringArgumentType
import org.progreso.api.managers.ConfigManager

@AbstractCommand.Register("config")
object ConfigCommand : AbstractCommand() {
    init {
        for ((category) in ConfigManager.categories) {
            literal(category.name) {
                literal("load") {
                    argument("config", StringArgumentType.string()).executes { context ->
                        val config: String by context

                        try {
                            category.load(config)
                        } catch (ex: Exception) {
                            errorLocalized(
                                "command.config.load_error",
                                config
                            )
                            return@executes
                        }

                        infoLocalized(
                            "command.config.load",
                            config
                        )
                    }
                }

                literal("save") {
                    argument("config", StringArgumentType.string()).executes { context ->
                        val config: String by context

                        category.save(config)
                        infoLocalized(
                            "command.config.save",
                            config
                        )
                    }

                    executes { context ->
                        category.save()

                        infoLocalized(
                            "command.config.save_many",
                            category.name
                        )
                    }
                }

                literal("refresh") {
                    argument("config", StringArgumentType.string()).executes { context ->
                        val config: String by context

                        category.refresh(config)
                        infoLocalized(
                            "command.config.refresh",
                            config
                        )
                    }

                    executes { context ->
                        category.refresh()
                        infoLocalized(
                            "command.config.refresh_many",
                            category.name
                        )
                    }
                }

                literal("list").executes { context ->
                    infoLocalized(
                        "command.config.list",
                        category.name,
                        category.configs.joinToString { it.name }
                    )
                }


                executes { context ->
                    infoLocalized(
                        "command.config.current",
                        category.name,
                        ConfigManager.getCategoryConfig(category)!!
                    )
                }
            }
        }
    }
}