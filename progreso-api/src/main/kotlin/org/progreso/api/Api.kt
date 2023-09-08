package org.progreso.api

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.progreso.api.accessor.ChatAccessor
import org.progreso.api.accessor.EventAccessor
import org.progreso.api.accessor.LoggerAccessor
import org.progreso.api.accessor.TextAccessor
import org.progreso.api.event.EventBus
import org.progreso.api.managers.*

object Api {
    private var initialized = false

    lateinit var EVENT: EventAccessor
    lateinit var CHAT: ChatAccessor
    lateinit var TEXT: TextAccessor
    lateinit var LOGGER: LoggerAccessor

    val API_EVENT_BUS = EventBus()
    val GSON: Gson = GsonBuilder()
        .setPrettyPrinting()
        .create()

    fun accessors(
        event: EventAccessor,
        chat: ChatAccessor,
        text: TextAccessor,
        logger: LoggerAccessor
    ) {
        logger.info("Setting accessors...")
        EVENT = event
        CHAT = chat
        TEXT = text
        LOGGER = logger
    }

    fun initialize(globalConfigAccessor: ConfigManager.GlobalConfigAccessor = ConfigManager.GlobalConfigAccessor.Default) {
        if (initialized) {
            throw RuntimeException("Api already initialized")
        }

        LOGGER.info("Setting managers...")
        ModuleManager
        CommandManager
        FriendManager
        ConfigManager.load(globalConfigAccessor)

        LOGGER.info("Loading plugins...")
        for (plugin in PluginManager.plugins) {
            plugin.loadPlugin()
        }

        LOGGER.info("Adding shutdown hook...")
        Runtime.getRuntime().addShutdownHook(Thread {
            LOGGER.info("Unloading plugins...")
            for (plugin in PluginManager.plugins.toList()) {
                plugin.unloadPlugin()
                PluginManager.removePlugin(plugin)
            }

            LOGGER.info("Unloading managers...")
            ConfigManager.save()

            initialized = false
        })

        initialized = true
    }
}