package org.progreso.api.command.arguments

import com.mojang.brigadier.StringReader
import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.context.CommandContext
import org.progreso.api.Api
import org.progreso.api.config.AbstractConfigHelper
import org.progreso.api.managers.ConfigManager

class ConfigHelperArgument : ArgumentType<String> {
    companion object {
        fun create() = ConfigHelperArgument()

        fun get(context: CommandContext<*>): AbstractConfigHelper<*> {
            val helperName = context.getArgument("helper", String::class.java)
            val helper = ConfigManager.getOrNull(helperName)
            if (helper == null) Api.CHAT.send("Helper $helperName not found")
            return helper!!
        }
    }

    override fun parse(reader: StringReader): String {
        return reader.readString()
    }
}