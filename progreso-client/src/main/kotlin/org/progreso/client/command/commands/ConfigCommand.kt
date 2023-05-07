package org.progreso.client.command.commands

import org.progreso.api.managers.ConfigManager
import org.progreso.client.command.Command

class ConfigCommand : Command("config") {
    private companion object {
        val helperNames get() = ConfigManager.helpers.map { it.key.name }
    }

    override fun execute(args: List<String>) {
        if (args.isEmpty()) {
            return send("Not enough arguments. Correct usage - config <${helperNames.joinToString("/")}> <load/save/list> [name]")
        }

        val helperName = args[0].lowercase()

        if (helperName !in helperNames) {
            return send("Unknown argument type - ${helperName}. Excepted ${helperNames.joinToString("/")}")
        }

        val helper = ConfigManager[helperName]

        if (args.size == 1) {
            return send("Current config: ${ConfigManager[helper]}")
        }

        when (args[1].lowercase()) {
            "load" -> {
                if (args.size < 3) {
                    return send("Not enough arguments. Correct usage - config $helperName load [name]")
                }

                helper.load(args[2])
                send("Config loaded")
            }

            "save" -> {
                if (args.size < 3) {
                    helper.save()
                } else {
                    helper.save(args[2])
                }

                send("Config saved")
            }

            "list" -> {
                send("Configs: ${helper.configs.joinToString { it.name }}")
            }

            else -> {
                send("Unknown argument type - ${args[1]}. Expected load/save/list")
            }
        }
    }
}