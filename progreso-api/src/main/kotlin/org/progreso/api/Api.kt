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

        logger.info("Initializing plugins...")
        for (plugin in PluginManager.plugins) {
            plugin.initializePlugin()
        }

        logger.info("Adding shutdown hook...")
        Runtime.getRuntime().addShutdownHook(Thread {
            LOGGER.info("Unloading managers...")
            ConfigManager.save()

            LOGGER.info("Uninitializing plugins...")
            for (plugin in PluginManager.plugins.toList()) {
                plugin.uninitializePlugin()
                PluginManager.plugins.remove(plugin)
            }

            initialized = false
        })

        initialized = true
    }
}