package org.progreso.api.config.helpers

import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import org.progreso.api.alt.AbstractAltAccount
import org.progreso.api.alt.accounts.CrackedAccount
import org.progreso.api.config.AbstractConfigHelper
import org.progreso.api.config.configs.AltConfig
import org.progreso.api.config.providers.AltConfigProvider
import org.progreso.api.managers.ConfigManager

class AltConfigHelper : AbstractConfigHelper<AltConfig>(
    name = "alt",
    path = "alts",
    provider = AltConfigProvider,
    container = ConfigManager,
    defaultConfigName = ConfigManager.DEFAULT_CONFIG_NAME
) {
    override fun read(name: String, reader: JsonReader): AltConfig {
        val alts = mutableListOf<AbstractAltAccount>()
        reader.beginArray()
        while (reader.hasNext()) {
            reader.beginObject()
            reader.nextName()
            val altName = reader.nextString()
            reader.nextName()
            alts.add(
                when (reader.nextString()) {
                    "Cracked" -> CrackedAccount(altName)
                    else -> throw RuntimeException()
                }
            )
            reader.endObject()
        }
        reader.endArray()
        return AltConfig(name, alts)
    }

    override fun write(config: AltConfig, writer: JsonWriter) {
        writer.beginArray()
        for (alt in config.alts) {
            writer.beginObject()
            writer.name("Name").value(alt.name)
            writer.name("Type").value(alt.type)
            writer.endObject()
        }
        writer.endArray()
    }
}