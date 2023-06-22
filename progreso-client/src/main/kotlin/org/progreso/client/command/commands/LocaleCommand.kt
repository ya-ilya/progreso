package org.progreso.client.command.commands

import org.progreso.api.command.argument.arguments.LocaleArgumentType
import org.progreso.api.command.dispatcher.CommandContext
import org.progreso.api.i18n.I18n
import org.progreso.client.command.Command

object LocaleCommand : Command("locale") {
    init {
        literal("set") {
            argument("locale", LocaleArgumentType.create()).executes { context ->
                val oldLocale = I18n.locale.name
                val locale = context.locale() ?: return@executes

                I18n.locale = locale
                infoLocalized(
                    "command.locale.set",
                    "oldLocale" to oldLocale,
                    "locale" to locale.name
                )
            }
        }

        literal("list").executes {
            infoLocalized(
                "command.locale.list",
                "locales" to I18n.locales.joinToString { it.name }
            )
        }

        executes {
            infoLocalized(
                "command.locale.current",
                "locale" to I18n.locale.name
            )
        }
    }

    private fun CommandContext.locale(): I18n.Locale? {
        return nullable("locale")
    }
}