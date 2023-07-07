package org.progreso.api.accessor

interface TextAccessor {
    fun i18n(key: String, vararg args: Any): String
}