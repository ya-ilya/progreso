package org.progreso.api.config.configs

import org.progreso.api.config.AbstractConfig
import org.progreso.api.friend.Friend

class FriendConfig(name: String, val friends: List<Friend>) : AbstractConfig(name)