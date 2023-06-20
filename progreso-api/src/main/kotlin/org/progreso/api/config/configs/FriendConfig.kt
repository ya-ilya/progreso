package org.progreso.api.config.configs

import org.progreso.api.config.AbstractConfig
import org.progreso.api.managers.FriendManager

class FriendConfig(name: String, val friends: List<FriendManager.Friend>) : AbstractConfig(name)