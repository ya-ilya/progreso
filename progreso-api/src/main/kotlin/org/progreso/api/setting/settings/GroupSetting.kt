package org.progreso.api.setting.settings

import org.progreso.api.setting.AbstractSetting
import org.progreso.api.setting.container.SettingContainer

open class GroupSetting(
    name: String,
    visibility: () -> Boolean
) : AbstractSetting<List<AbstractSetting<*>>>(name, emptyList(), visibility), SettingContainer {
    override val settings = mutableListOf<AbstractSetting<*>>()

    override var value: List<AbstractSetting<*>>
        get() = settings
        set(value) {
            val oldValue = settings.toList()
            settings.clear()
            settings.addAll(value)
            valueChanged(oldValue, settings)
        }

    override fun setting(name: String, visibility: () -> Boolean): GroupSetting {
        throw UnsupportedOperationException("GroupSetting doesn't support adding other GroupSettings to itself")
    }
}