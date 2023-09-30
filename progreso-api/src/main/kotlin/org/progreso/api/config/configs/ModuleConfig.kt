package org.progreso.api.config.configs

import org.progreso.api.config.AbstractConfig
import org.progreso.api.module.AbstractModule
import org.progreso.api.setting.AbstractSetting
import org.progreso.api.setting.settings.GroupSetting

class ModuleConfig(name: String, val modules: MutableList<ModuleConfigData>) : AbstractConfig(name) {
    data class ModuleConfigData(
        val name: String,
        var enabled: Boolean,
        val settings: MutableList<SettingConfigData>
    ) {
        companion object {
            fun create(module: AbstractModule): ModuleConfigData {
                return ModuleConfigData(
                    module.name,
                    module.enabled,
                    module.settings
                        .map { SettingConfigData.create(it) }
                        .toMutableList()
                )
            }
        }
    }

    data class SettingConfigData(
        val name: String,
        var value: Any
    ) {
        companion object {
            fun create(setting: AbstractSetting<*>): SettingConfigData {
                return when (setting) {
                    is GroupSetting -> SettingConfigData(setting.name, setting.settings.map { create(it) })
                    else -> SettingConfigData(setting.name, setting.value)
                }
            }
        }
    }
}