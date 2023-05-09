package org.progreso.client

import net.minecraftforge.fml.common.Mod
import org.apache.logging.log4j.LogManager
import org.progreso.api.Api
import org.progreso.api.command.AbstractCommand
import org.progreso.api.event.EventBus
import org.progreso.api.managers.CommandManager
import org.progreso.api.managers.ModuleManager
import org.progreso.api.module.AbstractModule
import org.progreso.client.accessors.ChatAccessor
import org.progreso.client.accessors.EventAccessor
import org.progreso.client.accessors.LoggerAccessor
import org.progreso.client.gui.ClickGUI
import org.progreso.client.manager.Managers
import org.reflections.Reflections

@Mod(modid = Client.MOD_ID, name = Client.MOD_NAME)
class Client {
    companion object {
        const val MOD_ID = "progreso"
        const val MOD_NAME = "Progreso Client"

        @JvmField
        val LOGGER = LogManager.getLogger("progreso")!!

        @JvmField
        val EVENT_BUS = EventBus()

        @JvmField
        val CLICK_GUI: ClickGUI = ClickGUI()

        @JvmStatic
        fun initialize() {
            LOGGER.info("Initializing client modules...")
            for (clazz in Reflections("org.progreso.client.module.modules").getSubTypesOf(AbstractModule::class.java)) {
                try {
                    ModuleManager.addModule(clazz.newInstance())
                } catch (ex: Exception) {
                    // Ignored
                }
            }

            LOGGER.info("Initializing client commands...")
            for (clazz in Reflections("org.progreso.client.command.commands").getSubTypesOf(AbstractCommand::class.java)) {
                try {
                    CommandManager.addCommand(clazz.newInstance())
                } catch (ex: Exception) {
                    // Ignored
                }
            }

            Api.initialize(EventAccessor, ChatAccessor, LoggerAccessor)

            LOGGER.info("Initializing client managers...")
            Managers.initialize()

            LOGGER.info("Initializing client ClickGUI...")
            CLICK_GUI.initialize()
        }
    }
}