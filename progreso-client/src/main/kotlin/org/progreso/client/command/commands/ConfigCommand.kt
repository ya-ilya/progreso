package org.progreso.client.command.commands

import org.progreso.api.command.argument.arguments.ConfigHelperArgumentType
import org.progreso.api.command.argument.arguments.StringArgumentType.Companion.string
import org.progreso.api.command.argument.ArgumentBuilder
import org.progreso.api.command.dispatcher.CommandContext
import org.progreso.api.config.AbstractConfigHelper
import org.progreso.api.managers.ConfigManager
import org.progreso.client.command.Command

class ConfigCommand : Command("config") {
    override fun build(builder: ArgumentBuilder) {
        builder.argument("helper", ConfigHelperArgumentType.create()) {
            literal("load") {
                argument("config", string()).executes { context ->
                    val config = context.get<String>("config")
                    val helper = context.helper() ?: return@executes

                    try {
                        helper.load(config)
                    } catch (ex: Exception) {
                        send("Config $config not found")
                        return@executes
                    }

                    send("Loaded ${ConfigManager.getHelperConfig(helper)} config")
                }
            }

            literal("save") {
                argument("config", string()).executes { context ->
                    val config = context.get<String>("config")
                    val helper = context.helper() ?: return@executes

                    try {
                        helper.save(config)
                    } catch (ex: Exception) {
                        send("Config $config not found")
                        return@executes
                    }

                    send("Saved ${ConfigManager.getHelperConfig(helper)} config")
                }

                executes { context ->
                    val helper = context.helper() ?: return@executes
                    helper.save()

                    send("Saved ${helper.name} configs")
                }
            }

            literal("refresh") {
                argument("config", string()).executes { context ->
                    val config = context.get<String>("config")
                    val helper = context.helper() ?: return@executes

                    helper.refresh(config)
                    send("Refreshed $config ${helper.name} config")
                }

                executes { context ->
                    val helper = context.helper() ?: return@executes
                    helper.refresh()

                    send("Refreshed ${helper.name} configs")
                }
            }

            literal("list").executes { context ->
                val helper = context.helper() ?: return@executes

                send("Configs in ${helper.name}: ${helper.configs.joinToString { it.name }}")
            }
        }.executes { context ->
            val helper = context.helper() ?: return@executes

            send("Current ${helper.name} config: ${ConfigManager.getHelperConfig(helper)}")
        }
    }
    
    private fun CommandContext.helper(): AbstractConfigHelper<*>? {
        return getNullable("helper")
    }
}