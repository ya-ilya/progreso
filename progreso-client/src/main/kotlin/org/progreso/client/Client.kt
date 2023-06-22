package org.progreso.client

import net.fabricmc.api.ModInitializer
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.option.GameOptions
import org.progreso.api.Api
import org.progreso.api.command.AbstractCommand
import org.progreso.api.i18n.I18n
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
import org.reflections.scanners.Scanners
import org.slf4j.LoggerFactory

class Client : ModInitializer {
    companion object {
        @JvmStatic
        val mc by lazy { MinecraftClientWrapper(MinecraftClient.getInstance()) }

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

        val locales = Reflections("locales", Scanners.Resources)
            .getResources(".*\\.json")
            .map {
                I18n.Locale.fromStream(
                    it.removeSuffix(".json").split("/").last(),
                    javaClass.classLoader.getResourceAsStream(it)!!
                )
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

        Api.initialize(EventAccessor, ChatAccessor, LoggerAccessor, Api.Configuration(locales, "en"))

        LOGGER.info("Initializing client guis...")
        ClickGUI.initialize()
        HudEditor.initialize()

        LOGGER.info("Initializing client managers...")
        Managers
    }

    class MinecraftClientWrapper(val client: MinecraftClient) {
        val tickDelta get() = client.tickDelta

        val world get() = client.world!!
        val player get() = client.player!!
        val textRenderer get() = client.textRenderer!!
        val inGameHud get() = client.inGameHud!!
        val interactionManager get() = client.interactionManager!!
        val networkHandler get() = client.networkHandler!!
        val options: GameOptions? get() = client.options
        val currentScreen: Screen? get() = client.currentScreen

        var session
            get() = client.session!!
            set(value) {
                client.session = value
            }

        fun setScreen(screen: Screen?) {
            client.setScreen(screen)
        }

        fun isNotSafe(): Boolean {
            return client.player == null || client.world == null
        }
    }
}