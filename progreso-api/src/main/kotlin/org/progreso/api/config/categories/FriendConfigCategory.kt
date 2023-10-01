package org.progreso.api.config.categories

import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import org.progreso.api.config.AbstractConfigCategory
import org.progreso.api.config.configs.FriendConfig
import org.progreso.api.extensions.iterateArray
import org.progreso.api.extensions.readArray
import org.progreso.api.extensions.writeArray
import org.progreso.api.friend.Friend
import org.progreso.api.friend.container.FriendContainer
import org.progreso.api.managers.ConfigManager
import org.progreso.api.managers.FriendManager

class FriendConfigCategory : AbstractConfigCategory<FriendConfig, FriendContainer>(
    name = "friend",
    path = "friends",
    container = FriendManager,
    defaultConfigName = ConfigManager.DEFAULT_CONFIG_NAME
) {
    override fun read(name: String, reader: JsonReader): FriendConfig {
        return FriendConfig(
            name,
            reader.readArray {
                iterateArray { Friend(nextString()) }
            }
        )
    }

    override fun write(config: FriendConfig, writer: JsonWriter) {
        writer.writeArray {
            for (friend in config.friends) {
                value(friend.name)
            }
        }
    }

    override fun create(name: String): FriendConfig {
        return FriendConfig(name, container.friends.toList())
    }

    override fun apply(config: FriendConfig) {
        container.friends.clear()
        container.friends.addAll(config.friends)
    }
}