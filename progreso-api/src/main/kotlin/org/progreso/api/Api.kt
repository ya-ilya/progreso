package org.progreso.api

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.progreso.api.accessor.ChatAccessor
import org.progreso.api.accessor.EventAccessor
import org.progreso.api.accessor.LoggerAccessor
import org.progreso.api.event.EventBus
import org.progreso.api.managers.*
import kotlin.properties.Delegates

object Api {
    private var initialized = false

    var EVENT by Delegates.notNull<EventAccessor>()
    var CHAT by Delegates.notNull<ChatAccessor>()
    var LOGGER by Delegates.notNull<LoggerAccessor>()

    var API_EVENT_BUS = EventBus()

    @JvmStatic
    val GSON: Gson = GsonBuilder()
        .setPrettyPrinting()
        .create()

    fun initialize(event: EventAccessor, chat: ChatAccessor, logger: LoggerAccessor) {
        if (initialized) {
            throw RuntimeException("Api already initialized")
        }

        logger.info("Setting accessors...")
        EVENT = event
        CHAT = chat
        LOGGER = logger

        logger.info("Setting managers...")
        ModuleManager
        CommandManager
        FriendManager
        ConfigManager

        logger.info("Loading plugins...")
        for (plugin in PluginManager.plugins) {
            plugin.loadPlugin()
        }

        logger.info("Adding shutdown hook...")
        Runtime.getRuntime().addShutdownHook(Thread {
            LOGGER.info("Unloading plugins...")
            for (plugin in PluginManager.plugins.toList()) {
                plugin.unloadPlugin()
                PluginManager.plugins.remove(plugin)
            }

            LOGGER.info("Unloading managers...")
            ConfigManager.save()

            initialized = false
        })

        initialized = true
    }
}