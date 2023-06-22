package org.progreso.api.i18n

import org.progreso.api.Api.GSON
import org.progreso.api.i18n.exceptions.I18nException
import java.io.InputStream

object I18n {
    lateinit var locales: List<Locale>
    lateinit var locale: Locale

    fun initialize(locales: List<Locale>, locale: String) {
        this.locales = locales
        this.locale = locales.first { it.name == locale }
    }

    fun getLocaleByNameOrNull(name: String): Locale? {
        return locales.firstOrNull { it.name == name }
    }

    @JvmStatic
    fun i18n(key: String, vararg replacements: Pair<String, Any>): String {
        val value = locale[key]
            ?: throw I18nException("Internationalization of $key not found in ${locale.name} dictionary")

        return replace(value, *replacements)
    }

    private fun replace(value: String, vararg replacements: Pair<String, Any>): String {
        var result = value
        replacements.forEach { result = result.replace("{{${it.first}}}", it.second.toString()) }
        return result
    }

    data class Locale(
        val name: String,
        private val dictionary: Map<String, String>
    ) {
        companion object {
            fun fromStream(name: String, stream: InputStream): Locale {
                return Locale(name, GSON.fromJson<Map<String, String>>(stream.reader(), Map::class.java))
            }
        }

        operator fun get(key: String): String? {
            return dictionary[key]
        }
    }
}