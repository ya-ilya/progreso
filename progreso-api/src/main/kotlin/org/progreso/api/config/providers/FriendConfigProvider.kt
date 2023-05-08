package org.progreso.api.config.providers

import org.progreso.api.config.AbstractConfigProvider
import org.progreso.api.config.configs.FriendConfig
import org.progreso.api.managers.FriendManager

object FriendConfigProvider : AbstractConfigProvider<FriendConfig>() {
    override fun create(name: String): FriendConfig {
        return FriendConfig(
            name,
            FriendManager.friends.map { it.name }
        )
    }

    override fun apply(config: FriendConfig) {
        FriendManager.friends.clear()
        FriendManager.friends.addAll(config.friends.map { FriendManager.Friend(it) })
    }
}