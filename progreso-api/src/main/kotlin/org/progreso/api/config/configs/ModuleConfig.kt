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
            fun fromAbstractModule(module: AbstractModule): ModuleConfigData {
                return ModuleConfigData(
                    module.name,
                    module.enabled,
                    module.settings
                        .map { SettingConfigData.fromAbstractSetting(it) }
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
            fun fromAbstractSetting(setting: AbstractSetting<*>): SettingConfigData {
                return when (setting) {
                    is GroupSetting -> fromGroupSetting(setting)
                    else -> SettingConfigData(setting.name, setting.value)
                }
            }

            private fun fromGroupSetting(setting: GroupSetting): SettingConfigData {
                return SettingConfigData(setting.name, setting.settings.map { fromAbstractSetting(it) })
            }
        }
    }
}