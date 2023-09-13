package org.progreso.api.setting.settings

import org.progreso.api.setting.AbstractSetting
import org.progreso.api.setting.container.SettingContainer

class GroupSetting(
    name: String,
    visibility: () -> Boolean = { true }
) : AbstractSetting<Set<AbstractSetting<*>>>(name, emptySet(), visibility), SettingContainer {
    override val settings = mutableSetOf<AbstractSetting<*>>()

    override var value: Set<AbstractSetting<*>>
        get() = settings
        set(value) {
            val oldValue = settings.toSet()
            settings.clear()
            settings.addAll(value)
            valueListeners.forEach { it.invoke(oldValue, settings) }
        }

    override fun reset() {
        // Empty
    }

    override fun setting(name: String, visibility: () -> Boolean): GroupSetting {
        throw UnsupportedOperationException("GroupSetting doesn't support adding other GroupSettings to itself")
    }
}