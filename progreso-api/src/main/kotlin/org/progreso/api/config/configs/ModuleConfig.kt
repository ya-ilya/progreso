package org.progreso.api.config.configs

import org.progreso.api.config.AbstractConfig
import org.progreso.api.module.AbstractModule
import org.progreso.api.setting.AbstractSetting
import org.progreso.api.setting.settings.GroupSetting

class ModuleConfig(name: String, var modules: List<ModuleConfigData>) : AbstractConfig(name) {
    data class ModuleConfigData(
        val name: String,
        var settings: List<SettingConfigData>
    ) {
        constructor(module: AbstractModule) : this(module.name, module.settings.map { SettingConfigData(it) })
    }

    data class SettingConfigData(
        val name: String,
        var value: Any
    ) {
        constructor(setting: AbstractSetting<*>) : this(
            setting.name,
            if (setting is GroupSetting) setting.settings.map { SettingConfigData(it) }
            else setting.value
        )
    }
}