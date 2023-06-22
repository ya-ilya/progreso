package org.progreso.api.command.argument.arguments

import org.progreso.api.Api
import org.progreso.api.command.argument.ArgumentType
import org.progreso.api.command.reader.StringReader
import org.progreso.api.managers.ModuleManager
import org.progreso.api.module.AbstractModule

class ModuleArgumentType : ArgumentType<AbstractModule?> {
    companion object {
        fun create() = ModuleArgumentType()
    }

    override val name = "module"

    override fun parse(reader: StringReader): AbstractModule? {
        val moduleName = reader.readString()
        val module = ModuleManager.getModuleByNameOrNull(moduleName)
        if (module == null) Api.CHAT.errorLocalized(
            "argument.module.error",
            "module" to moduleName
        )
        return module
    }

    override fun check(reader: StringReader): Boolean {
        return true
    }
}