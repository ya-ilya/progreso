package org.progreso.api.command.argument.arguments

import org.progreso.api.Api
import org.progreso.api.command.argument.ArgumentType
import org.progreso.api.command.reader.StringReader
import org.progreso.api.config.AbstractConfigHelper
import org.progreso.api.managers.ConfigManager

class ConfigHelperArgumentType : ArgumentType<AbstractConfigHelper<*>?> {
    companion object {
        fun create() = ConfigHelperArgumentType()
    }

    override val name = "helper"

    override fun parse(reader: StringReader): AbstractConfigHelper<*>? {
        val helperName = reader.readString()
        val helper = ConfigManager.getHelperByNameOrNull(helperName)
        if (helper == null) Api.CHAT.errorLocalized("argument.config_helper.error", helperName)
        return helper
    }

    override fun check(reader: StringReader): Boolean {
        return true
    }
}