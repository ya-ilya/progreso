package org.progreso.api.config.helpers

import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import org.progreso.api.alt.AltAccount
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
        val alts = mutableListOf<AltAccount>()
        reader.beginArray()
        while (reader.hasNext()) {
            reader.beginObject()

            reader.nextName()
            val type = reader.nextString()

            reader.nextName()
            val username = reader.nextString()

            alts.add(
                when (type) {
                    "Cracked" -> AltAccount.Cracked(username)
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
            writer.name("Type").value(alt.type)
            writer.name("Name").value(alt.username)
            writer.endObject()
        }
        writer.endArray()
    }
}