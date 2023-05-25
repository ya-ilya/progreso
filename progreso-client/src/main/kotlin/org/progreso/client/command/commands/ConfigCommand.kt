package org.progreso.client.command.commands

import org.progreso.api.command.argument.ArgumentBuilder
import org.progreso.api.command.argument.arguments.ConfigHelperArgumentType
import org.progreso.api.command.argument.arguments.StringArgumentType.Companion.string
import org.progreso.api.command.dispatcher.CommandContext
import org.progreso.api.config.AbstractConfigHelper
import org.progreso.api.managers.ConfigManager
import org.progreso.client.command.Command

object ConfigCommand : Command("config") {
    override fun build(builder: ArgumentBuilder) {
        builder.argument("helper", ConfigHelperArgumentType.create()) {
            literal("load") {
                argument("config", string()).executes { context ->
                    val config = context.get<String>("config")
                    val helper = context.helper() ?: return@executes

                    try {
                        helper.load(config)
                    } catch (ex: Exception) {
                        error("Config $config not found")
                        return@executes
                    }

                    info("Loaded ${ConfigManager.getHelperConfig(helper)} config")
                }
            }

            literal("save") {
                argument("config", string()).executes { context ->
                    val config = context.get<String>("config")
                    val helper = context.helper() ?: return@executes

                    helper.save(config)
                    info("Saved ${ConfigManager.getHelperConfig(helper)} config")
                }

                executes { context ->
                    val helper = context.helper() ?: return@executes
                    helper.save()

                    info("Saved ${helper.name} configs")
                }
            }

            literal("refresh") {
                argument("config", string()).executes { context ->
                    val config = context.get<String>("config")
                    val helper = context.helper() ?: return@executes

                    helper.refresh(config)
                    info("Refreshed $config ${helper.name} config")
                }

                executes { context ->
                    val helper = context.helper() ?: return@executes
                    helper.refresh()

                    info("Refreshed ${helper.name} configs")
                }
            }

            literal("list").executes { context ->
                val helper = context.helper() ?: return@executes

                info("Configs in ${helper.name}: ${helper.configs.joinToString { it.name }}")
            }
        }.executes { context ->
            val helper = context.helper() ?: return@executes

            info("Current ${helper.name} config: ${ConfigManager.getHelperConfig(helper)}")
        }
    }

    private fun CommandContext.helper(): AbstractConfigHelper<*>? {
        return getNullable("helper")
    }
}