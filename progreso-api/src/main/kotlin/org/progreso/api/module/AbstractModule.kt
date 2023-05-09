package org.progreso.api.module

import org.progreso.api.Api
import org.progreso.api.event.events.ModuleEvent
import org.progreso.api.setting.AbstractSetting
import org.progreso.api.setting.container.SettingContainer
import org.progreso.api.setting.settings.BooleanSetting

/**
 * Module abstract class
 *
 * @param name Module name
 * @param description Module description
 * @param category Module category
 */
abstract class AbstractModule(
    val name: String,
    val description: String,
    val category: Category
) : SettingContainer {
    override val settings = mutableListOf<AbstractSetting<*>>()

    @Suppress("LeakingThis")
    val bind = setting("Bind", 0)

    var enabled by object : BooleanSetting("Enabled", false) {
        override fun valueChanged(oldValue: Boolean, newValue: Boolean) {
            if (oldValue == newValue) return
            if (newValue) {
                Api.EVENT.register(this@AbstractModule)
                Api.API_EVENT_BUS.post(ModuleEvent(this@AbstractModule))
                onEnable()
            } else {
                Api.EVENT.unregister(this@AbstractModule)
                Api.API_EVENT_BUS.post(ModuleEvent(this@AbstractModule))
                onDisable()
            }
        }
    }

    open fun onEnable() {}
    open fun onDisable() {}

    fun toggle() {
        enabled = !enabled
    }
}