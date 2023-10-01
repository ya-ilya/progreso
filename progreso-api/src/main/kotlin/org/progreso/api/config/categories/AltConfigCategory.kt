package org.progreso.api.config.categories

import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import org.progreso.api.alt.AltAccount
import org.progreso.api.alt.container.AltContainer
import org.progreso.api.config.AbstractConfigCategory
import org.progreso.api.config.configs.AltConfig
import org.progreso.api.extensions.*
import org.progreso.api.managers.AltManager
import org.progreso.api.managers.ConfigManager

class AltConfigCategory : AbstractConfigCategory<AltConfig, AltContainer>(
    name = "alt",
    path = "alts",
    container = AltManager,
    defaultConfigName = ConfigManager.DEFAULT_CONFIG_NAME
) {
    override fun read(name: String, reader: JsonReader): AltConfig {
        return AltConfig(
            name,
            reader.readArray {
                iterateArray {
                    readObject {
                        reader.nextName()
                        val type = reader.nextString()

                        reader.nextName()
                        val username = reader.nextString()

                        when (type) {
                            "Offline" -> AltAccount.Offline(username)
                            else -> throw IllegalArgumentException()
                        }
                    }
                }
            }
        )
    }

    override fun write(config: AltConfig, writer: JsonWriter) {
        writer.writeArray {
            for (alt in config.alts) {
                writeObject {
                    name("Type").value(alt.type)
                    name("Name").value(alt.username)
                }
            }
        }
    }

    override fun create(name: String): AltConfig {
        return AltConfig(name, container.alts.toList())
    }

    override fun apply(config: AltConfig) {
        container.alts.clear()
        container.alts.addAll(config.alts)
    }
}