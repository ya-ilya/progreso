package org.progreso.client.command.commands

import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import org.progreso.api.command.arguments.ConfigHelperArgumentType
import org.progreso.api.managers.ConfigManager
import org.progreso.client.command.Command

class ConfigCommand : Command("config") {
    override fun build(builder: LiteralArgumentBuilder<Any>) {
        builder.then(argument("helper", ConfigHelperArgumentType.create())
            .then(
                literal("load").then(
                    argument("config", StringArgumentType.string()).executesSuccess { context ->
                        val config = StringArgumentType.getString(context, "config")
                        val helper = ConfigHelperArgumentType.get(context) ?: return@executesSuccess

                        try {
                            helper.load(config)
                        } catch (ex: Exception) {
                            send("Config $config not found")
                            return@executesSuccess
                        }

                        send("Loaded ${ConfigManager.getHelperConfig(helper)} config")

                        return@executesSuccess
                    }
                )
            )
            .then(
                literal("save").then(
                    argument("config", StringArgumentType.string()).executesSuccess { context ->
                        val config = StringArgumentType.getString(context, "config")
                        val helper = ConfigHelperArgumentType.get(context) ?: return@executesSuccess

                        try {
                            helper.save(config)
                        } catch (ex: Exception) {
                            send("Config $config not found")
                            return@executesSuccess
                        }

                        send("Saved ${ConfigManager.getHelperConfig(helper)} config")

                        return@executesSuccess
                    }
                ).executesSuccess { context ->
                    val helper = ConfigHelperArgumentType.get(context) ?: return@executesSuccess
                    helper.save()

                    send("Saved ${helper.name} configs")

                    return@executesSuccess
                }
            )
            .then(
                literal("refresh").then(
                    argument("config", StringArgumentType.string()).executesSuccess { context ->
                        val config = StringArgumentType.getString(context, "config")
                        val helper = ConfigHelperArgumentType.get(context) ?: return@executesSuccess
                        helper.refresh(config)

                        send("Refreshed $config ${helper.name} config")

                        return@executesSuccess
                    }
                ).executesSuccess { context ->
                    val helper = ConfigHelperArgumentType.get(context) ?: return@executesSuccess
                    helper.refresh()

                    send("Refreshed ${helper.name} configs")

                    return@executesSuccess
                }
            )
            .then(
                literal("list").executesSuccess { context ->
                    val helper = ConfigHelperArgumentType.get(context) ?: return@executesSuccess

                    send("Configs in ${helper.name}: ${helper.configs.joinToString { it.name }}")

                    return@executesSuccess
                }
            )
            .executesSuccess { context ->
                val helper = ConfigHelperArgumentType.get(context) ?: return@executesSuccess

                send("Current ${helper.name} config: ${ConfigManager.getHelperConfig(helper)}")

                return@executesSuccess
            }
        )
    }
}