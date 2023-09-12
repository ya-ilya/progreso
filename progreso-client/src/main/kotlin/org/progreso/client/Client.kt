package org.progreso.client

import net.fabricmc.api.ClientModInitializer
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.option.GameOptions
import org.progreso.api.Api
import org.progreso.api.command.AbstractCommand
import org.progreso.api.managers.CommandManager
import org.progreso.api.managers.ConfigManager
import org.progreso.api.managers.ModuleManager
import org.progreso.api.managers.PluginManager
import org.progreso.api.module.AbstractModule
import org.progreso.api.plugin.AbstractPlugin
import org.progreso.client.accessors.*
import org.progreso.client.gui.clickgui.ClickGUI
import org.progreso.client.gui.clickgui.HudEditor
import org.progreso.client.managers.CombatManager
import org.progreso.client.util.client.KeyboardUtil
import org.reflections.Reflections
import org.slf4j.LoggerFactory

class Client : ClientModInitializer {
    companion object {
        @JvmStatic
        val mc by lazy { MinecraftClientWrapper(MinecraftClient.getInstance()) }

        @JvmField
        val LOGGER = LoggerFactory.getLogger("progreso")!!

        @JvmField
        val EVENT_BUS = Api.API_EVENT_BUS

        val config get() = ProgresoGlobalConfigAccessor.config
    }

    override fun onInitializeClient() {
        Api.accessors(
            EventAccessor,
            ChatAccessor,
            CommandAccessor,
            TextAccessor,
            LoggerAccessor
        )

        for (entrypoint in FabricLoader.getInstance().getEntrypointContainers("progreso", AbstractPlugin::class.java)) {
            val metadata = entrypoint.provider.metadata
            val plugin = entrypoint.entrypoint

            plugin.name = metadata.name
            plugin.version = metadata.version.friendlyString
            plugin.author = metadata.authors.first().name

            PluginManager.addPlugin(plugin)
        }

        LOGGER.info("Initializing client modules...")
        for (clazz in Reflections("org.progreso.client.modules")
            .getSubTypesOf(AbstractModule::class.java)
            .sortedBy { it.name }
        ) {
            try {
                ModuleManager.addModule(clazz.getField("INSTANCE").get(null) as AbstractModule)
            } catch (ex: Exception) {
                // Ignored
            }
        }

        println(KeyboardUtil.keyMap)

        LOGGER.info("Initializing client commands...")
        for (clazz in Reflections("org.progreso.client.commands")
            .getSubTypesOf(AbstractCommand::class.java)
            .sortedBy { it.name }
        ) {
            try {
                CommandManager.addCommand(clazz.getField("INSTANCE").get(null) as AbstractCommand)
            } catch (ex: Exception) {
                // Ignored
            }
        }

        Api.initialize(ProgresoGlobalConfigAccessor)

        LOGGER.info("Initializing client guis...")
        ClickGUI.initialize()
        HudEditor.initialize()

        LOGGER.info("Initializing client managers...")
        CombatManager
    }

    object ProgresoGlobalConfigAccessor : ConfigManager.GlobalConfigAccessor {
        data class GlobalConfig(
            var categories: Map<String, String> = emptyMap(),
            var customFont: CustomFont? = null
        ) {
            data class CustomFont(
                var name: String? = null,
                var size: Float? = null
            )
        }

        var config = GlobalConfig()

        override var categories
            get() = config.categories
            set(value) {
                config.categories = value
            }

        override fun fromJson(text: String) {
            config = Api.GSON.fromJson(text, GlobalConfig::class.java)
        }

        override fun toJson(): String {
            return Api.GSON.toJson(config)
        }
    }

    class MinecraftClientWrapper(val client: MinecraftClient) {
        val tickDelta get() = client.tickDelta

        val world get() = client.world!!
        val player get() = client.player!!
        val textRenderer get() = client.textRenderer!!
        val resourceManager get() = client.resourceManager!!
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
            client.send {
                client.setScreen(screen)
            }
        }

        fun isNotSafe(): Boolean {
            return client.player == null || client.world == null
        }
    }
}