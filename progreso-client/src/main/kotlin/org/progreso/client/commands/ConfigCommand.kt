package org.progreso.client.commands

import org.progreso.api.command.AbstractCommand
import org.progreso.api.command.argument.arguments.ConfigHelperArgumentType
import org.progreso.api.command.argument.arguments.StringArgumentType.Companion.string
import org.progreso.api.command.dispatcher.CommandContext
import org.progreso.api.config.AbstractConfigHelper
import org.progreso.api.managers.ConfigManager

@AbstractCommand.Register("config")
object ConfigCommand : AbstractCommand() {
    init {
        argument("helper", ConfigHelperArgumentType.create()) {
            literal("load") {
                argument("config", string()).executes { context ->
                    val config: String by context
                    val helper = context.helper() ?: return@executes

                    try {
                        helper.load(config)
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
                argument("config", string()).executes { context ->
                    val config: String by context
                    val helper = context.helper() ?: return@executes

                    helper.save(config)
                    infoLocalized(
                        "command.config.save",
                        config
                    )
                }

                executes { context ->
                    val helper = context.helper() ?: return@executes
                    helper.save()

                    infoLocalized(
                        "command.config.save_many",
                        helper.name
                    )
                }
            }

            literal("refresh") {
                argument("config", string()).executes { context ->
                    val config: String by context
                    val helper = context.helper() ?: return@executes

                    helper.refresh(config)
                    infoLocalized(
                        "command.config.refresh",
                        config
                    )
                }

                executes { context ->
                    val helper = context.helper() ?: return@executes

                    helper.refresh()
                    infoLocalized(
                        "command.config.refresh_many",
                        helper.name
                    )
                }
            }

            literal("list").executes { context ->
                val helper = context.helper() ?: return@executes

                infoLocalized(
                    "command.config.list",
                    helper.name,
                    helper.configs.joinToString { it.name }
                )
            }
        }.executes { context ->
            val helper = context.helper() ?: return@executes

            infoLocalized(
                "command.config.current",
                helper.name,
                ConfigManager.getHelperConfig(helper)!!
            )
        }
    }

    private fun CommandContext.helper(): AbstractConfigHelper<*>? {
        return nullable("helper")
    }
}