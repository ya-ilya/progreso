package org.progreso.client.command.commands

import com.mojang.brigadier.Command.SINGLE_SUCCESS
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import org.progreso.api.command.arguments.ConfigHelperArgument
import org.progreso.api.managers.ConfigManager
import org.progreso.client.command.Command

class ConfigCommand : Command("config") {
    override fun build(builder: LiteralArgumentBuilder<Any>) {
        builder.then(argument("helper", ConfigHelperArgument.create())
            .then(
                literal("load").then(
                    argument("config", StringArgumentType.string()).executes { context ->
                        val helper = ConfigHelperArgument.get(context)
                        val config = StringArgumentType.getString(context, "config")

                        try {
                            helper.load(config)
                        } catch (ex: Exception) {
                            send("Config $config not found")
                            return@executes SINGLE_SUCCESS
                        }

                        send("Loaded ${ConfigManager[helper]} config")

                        return@executes SINGLE_SUCCESS
                    }
                )
            )
            .then(
                literal("save").then(
                    argument("config", StringArgumentType.string()).executes { context ->
                        val config = StringArgumentType.getString(context, "config")
                        val helper = ConfigHelperArgument.get(context)

                        try {
                            helper.save(config)
                        } catch (ex: Exception) {
                            send("Config $config not found")
                            return@executes SINGLE_SUCCESS
                        }

                        send("Saved ${ConfigManager[helper]} config")

                        return@executes SINGLE_SUCCESS
                    }
                ).executes { context ->
                    val helper = ConfigHelperArgument.get(context)
                    helper.save()

                    send("Saved ${helper.name} configs")

                    return@executes SINGLE_SUCCESS
                }
            )
            .then(
                literal("refresh").then(
                    argument("config", StringArgumentType.string()).executes { context ->
                        val helper = ConfigHelperArgument.get(context)
                        helper.refresh()

                        send("Refreshed ${helper.name}")

                        return@executes SINGLE_SUCCESS
                    }
                )
            )
            .then(
                literal("list").executes { context ->
                    val helper = ConfigHelperArgument.get(context)

                    send("Configs in ${helper.name}: ${helper.configs.map { it.name }}")

                    return@executes SINGLE_SUCCESS
                }
            )
            .executes { context ->
                val helper = ConfigManager[StringArgumentType.getString(context, "helper")]

                send("Current ${helper.name} config: ${ConfigManager[helper]}")

                return@executes SINGLE_SUCCESS
            }
        )
    }
}