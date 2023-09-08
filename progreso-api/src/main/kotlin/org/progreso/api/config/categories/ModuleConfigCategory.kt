package org.progreso.api.config.categories

import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import org.progreso.api.config.AbstractConfigCategory
import org.progreso.api.config.AbstractConfigProvider
import org.progreso.api.config.configs.ModuleConfig
import org.progreso.api.config.providers.ModuleConfigProvider
import org.progreso.api.managers.ConfigManager
import org.progreso.api.managers.ModuleManager
import java.awt.Color

class ModuleConfigCategory(
    name: String = "module",
    path: String = "modules",
    provider: AbstractConfigProvider<ModuleConfig> = ModuleConfigProvider(ModuleManager),
    defaultConfigName: String? = ConfigManager.DEFAULT_CONFIG_NAME
) : AbstractConfigCategory<ModuleConfig>(name, path, provider, defaultConfigName) {
    override fun read(name: String, reader: JsonReader): ModuleConfig {
        val config = ModuleConfig(name, mutableListOf())
        reader.beginObject()

        while (reader.hasNext()) {
            val module = ModuleConfig.ModuleConfigData(
                reader.nextName(),
                false,
                mutableListOf()
            )

            reader.beginObject()
            module.settings.addAll(readSettings(reader))
            reader.endObject()

            config.modules.add(module)
        }

        reader.endObject()
        return config
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

    private companion object {
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
                        reader.beginObject()
                        setting.value = readSettings(reader)
                        reader.endObject()
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
                        writer.beginObject()
                        writeSettings(writer, value as List<ModuleConfig.SettingConfigData>)
                        writer.endObject()
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