package org.progreso.api.config.categories

import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import org.progreso.api.config.AbstractConfigCategory
import org.progreso.api.config.configs.ModuleConfig
import org.progreso.api.extensions.iterateObjectMap
import org.progreso.api.extensions.readObject
import org.progreso.api.extensions.writeObject
import org.progreso.api.managers.ConfigManager
import org.progreso.api.managers.ModuleManager
import org.progreso.api.module.container.ModuleContainer
import org.progreso.api.setting.container.SettingContainer
import org.progreso.api.setting.settings.GroupSetting
import java.awt.Color

class ModuleConfigCategory(
    name: String = "module",
    path: String = "modules",
    container: ModuleContainer = ModuleManager,
    defaultConfigName: String? = ConfigManager.DEFAULT_CONFIG_NAME
) : AbstractConfigCategory<ModuleConfig, ModuleContainer>(name, path, container, defaultConfigName) {
    override fun read(name: String, reader: JsonReader): ModuleConfig {
        return ModuleConfig(
            name,
            reader.readObject {
                iterateObjectMap { entryName ->
                    ModuleConfig.ModuleConfigData(
                        entryName,
                        readSettings(this)
                    )
                }
            }
        )
    }

    override fun write(config: ModuleConfig, writer: JsonWriter) {
        writer.beginObject()

        for (module in config.modules) {
            writer.name(module.name)
            writer.beginObject()
            writeSettings(writer, module.settings)
            writer.endObject()
        }

        writer.endObject()
    }

    override fun create(name: String): ModuleConfig {
        return ModuleConfig(
            name,
            container.modules
                .map { ModuleConfig.ModuleConfigData.create(it) }
                .toMutableList()
        )
    }

    override fun apply(config: ModuleConfig) {
        config.modules = config.modules.filter { module ->
            container.modules.any { it.name == module.name }
        }

        for (moduleData in config.modules) {
            val module = container.getModuleByName(moduleData.name)
            moduleData.settings = moduleData.settings.filter { setting ->
                module.settings.any { it.name == setting.name }
            }
            module.setSettings(moduleData.settings)
        }
    }

    private companion object {
        @Suppress("UNCHECKED_CAST")
        fun SettingContainer.setSettings(settings: List<ModuleConfig.SettingConfigData>) {
            for (settingData in settings) {
                when (val setting = this.settings.firstOrNull { it.name == settingData.name }) {
                    null -> continue

                    is GroupSetting -> {
                        setting.setSettings(settingData.value as List<ModuleConfig.SettingConfigData>)
                    }

                    else -> setting.setAnyValue(settingData.value)
                }
            }
        }

        fun readSettings(reader: JsonReader): List<ModuleConfig.SettingConfigData> {
            val settings = mutableListOf<ModuleConfig.SettingConfigData>()

            while (reader.hasNext()) {
                val setting = ModuleConfig.SettingConfigData(reader.nextName(), Any())

                when (reader.peek()) {
                    // BooleanSetting
                    JsonToken.BOOLEAN -> setting.value = reader.nextBoolean()

                    // EnumSetting, ColorSetting
                    JsonToken.STRING -> setting.value = reader.nextString()

                    // NumberSetting, BindSetting
                    JsonToken.NUMBER -> {
                        val string = reader.nextString()

                        if (string.contains(".")) {
                            try {
                                setting.value = string.toFloat()
                            } catch (ex: Exception) {
                                setting.value = string.toDouble()
                            }
                        } else {
                            setting.value = string.toInt()
                        }
                    }

                    // GroupSetting
                    JsonToken.BEGIN_OBJECT -> {
                        setting.value = reader.readObject { readSettings(reader) }
                    }

                    else -> throw RuntimeException("Unknown setting type: ${setting.name}")
                }

                settings.add(setting)
            }

            return settings
        }

        @Suppress("UNCHECKED_CAST")
        fun writeSettings(writer: JsonWriter, settings: List<ModuleConfig.SettingConfigData>) {
            for (setting in settings) {
                writer.name(setting.name)

                when (val value = setting.value) {
                    is Color -> writer.value(colorToString(value))
                    is Enum<*> -> writer.value(value.name)
                    is String -> writer.value(value)
                    is Boolean -> writer.value(value)
                    is Number -> writer.value(value)
                    is List<*> -> {
                        writer.writeObject {
                            writeSettings(this, value as List<ModuleConfig.SettingConfigData>)
                        }
                    }

                    else -> throw RuntimeException("Unknown setting type: ${setting.name}")
                }
            }
        }

        fun colorToString(color: Color): String {
            return "${color.red}-${color.green}-${color.blue}-${color.alpha}"
        }
    }
}