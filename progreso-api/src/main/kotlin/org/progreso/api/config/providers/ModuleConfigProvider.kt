package org.progreso.api.config.providers

import org.progreso.api.config.AbstractConfigProvider
import org.progreso.api.config.configs.ModuleConfig
import org.progreso.api.module.container.ModuleContainer
import org.progreso.api.setting.container.SettingContainer
import org.progreso.api.setting.settings.GroupSetting

class ModuleConfigProvider(private val container: ModuleContainer) : AbstractConfigProvider<ModuleConfig>() {
    override fun create(name: String): ModuleConfig {
        return ModuleConfig(
            name,
            container.modules
                .map { ModuleConfig.ModuleConfigData.fromAbstractModule(it) }
                .toMutableList()
        )
    }

    override fun apply(config: ModuleConfig) {
        config.modules.removeIf { module ->
            !container.modules.any { it.name == module.name }
        }

        for (moduleData in config.modules) {
            val module = container.getModuleByName(moduleData.name)
            module.enabled = moduleData.enabled
            moduleData.settings.removeIf { setting ->
                !module.settings.any { it.name == setting.name }
            }
            module.setSettings(moduleData.settings)
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun SettingContainer.setSettings(settings: List<ModuleConfig.SettingConfigData>) {
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
}