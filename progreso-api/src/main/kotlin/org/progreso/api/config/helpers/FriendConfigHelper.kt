package org.progreso.api.config.helpers

import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import org.progreso.api.config.AbstractConfigHelper
import org.progreso.api.config.configs.FriendConfig
import org.progreso.api.config.providers.FriendConfigProvider
import org.progreso.api.managers.ConfigManager

class FriendConfigHelper : AbstractConfigHelper<FriendConfig>(
    name = "friend",
    path = "friends",
    provider = FriendConfigProvider,
    container = ConfigManager,
    defaultConfigName = ConfigManager.DEFAULT_CONFIG_NAME
) {
    override fun read(name: String, reader: JsonReader): FriendConfig {
        val friends = mutableListOf<String>()
        reader.beginArray()
        while (reader.hasNext()) {
            friends.add(reader.nextString())
        }
        reader.endArray()
        return FriendConfig(name, friends)
    }

    override fun write(config: FriendConfig, writer: JsonWriter) {
        writer.beginArray()
        for (friend in config.friends) {
            writer.value(friend)
        }
        writer.endArray()
    }

}