package org.progreso.api.command.argument.arguments

import org.progreso.api.Api
import org.progreso.api.command.argument.ArgumentType
import org.progreso.api.command.reader.StringReader
import org.progreso.api.managers.PluginManager
import org.progreso.api.plugin.AbstractPlugin

class PluginArgumentType : ArgumentType<AbstractPlugin?> {
    companion object {
        fun create() = PluginArgumentType()
    }

    override val name = "plugin"

    override fun parse(reader: StringReader): AbstractPlugin? {
        val pluginName = reader.readString()
        val plugin = PluginManager.getPluginByNameOrNull(pluginName)
        if (plugin == null) Api.CHAT.errorLocalized("argument.plugin.error", pluginName)
        return plugin
    }

    override fun check(reader: StringReader): Boolean {
        return true
    }
}