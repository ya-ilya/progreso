package org.progreso.api.command.argument.arguments

import org.progreso.api.Api
import org.progreso.api.command.argument.ArgumentType
import org.progreso.api.command.reader.StringReader
import org.progreso.api.i18n.I18n

class LocaleArgumentType : ArgumentType<I18n.Locale?> {
    companion object {
        fun create() = LocaleArgumentType()
    }

    override val name = "locale"

    override fun parse(reader: StringReader): I18n.Locale? {
        val localeName = reader.readString()
        val locale = I18n.getLocaleByNameOrNull(localeName)
        if (locale == null) {
            Api.CHAT.errorLocalized(
                "argument.locale.error",
                "locale" to localeName
            )
        }
        return locale
    }

    override fun check(reader: StringReader): Boolean {
        return true
    }
}