package org.progreso.api.command.arguments

import com.mojang.brigadier.StringReader
import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.context.CommandContext
import org.progreso.api.Api
import org.progreso.api.managers.PluginManager
import org.progreso.api.plugin.AbstractPlugin

class PluginArgumentType : ArgumentType<String> {
    companion object {
        fun create() = PluginArgumentType()

        fun get(context: CommandContext<*>): AbstractPlugin? {
            val pluginName = context.getArgument("plugin", String::class.java)
            val plugin = PluginManager.getPluginByNameOrNull(pluginName)
            if (plugin == null) Api.CHAT.send("Plugin $pluginName not found")
            return plugin
        }
    }

    override fun parse(reader: StringReader): String {
        return reader.readString()
    }
}