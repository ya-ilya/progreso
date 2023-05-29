package org.progreso.client

import net.fabricmc.api.ModInitializer
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.client.MinecraftClient
import org.progreso.api.Api
import org.progreso.api.command.AbstractCommand
import org.progreso.api.managers.CommandManager
import org.progreso.api.managers.ModuleManager
import org.progreso.api.managers.PluginManager
import org.progreso.api.module.AbstractModule
import org.progreso.api.plugin.AbstractPlugin
import org.progreso.client.accessors.ChatAccessor
import org.progreso.client.accessors.EventAccessor
import org.progreso.client.accessors.LoggerAccessor
import org.progreso.client.gui.clickgui.ClickGUI
import org.progreso.client.gui.clickgui.HudEditor
import org.progreso.client.manager.Managers
import org.reflections.Reflections
import org.slf4j.LoggerFactory

class Client : ModInitializer {
    companion object {
        @JvmStatic
        val mc: MinecraftClient by lazy { MinecraftClient.getInstance() }

        @JvmField
        val LOGGER = LoggerFactory.getLogger("progreso")!!

        @JvmField
        val EVENT_BUS = Api.API_EVENT_BUS
    }

    override fun onInitialize() {
        for (entrypoint in FabricLoader.getInstance().getEntrypointContainers("progreso", AbstractPlugin::class.java)) {
            val metadata = entrypoint.provider.metadata
            val plugin = entrypoint.entrypoint

            plugin.name = metadata.name
            plugin.version = metadata.version.friendlyString
            plugin.author = metadata.authors.first().name

            PluginManager.addPlugin(plugin)
        }

        LOGGER.info("Initializing client modules...")
        for (clazz in Reflections("org.progreso.client.module.modules").getSubTypesOf(AbstractModule::class.java)) {
            try {
                ModuleManager.addModule(clazz.getField("INSTANCE").get(null) as AbstractModule)
            } catch (ex: Exception) {
                // Ignored
            }
        }

        LOGGER.info("Initializing client commands...")
        for (clazz in Reflections("org.progreso.client.command.commands").getSubTypesOf(AbstractCommand::class.java)) {
            try {
                CommandManager.addCommand(clazz.getField("INSTANCE").get(null) as AbstractCommand)
            } catch (ex: Exception) {
                // Ignored
            }
        }

        Api.initialize(EventAccessor, ChatAccessor, LoggerAccessor)

        LOGGER.info("Initializing client guis...")
        ClickGUI.initialize()
        HudEditor.initialize()

        LOGGER.info("Initializing client managers...")
        Managers
    }
}