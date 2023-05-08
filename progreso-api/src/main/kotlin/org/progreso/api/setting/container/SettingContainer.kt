package org.progreso.api.setting.container

import org.progreso.api.setting.AbstractSetting
import org.progreso.api.setting.settings.*
import java.awt.Color

/**
 * Interface for setting container
 *
 * Current implementations: AbstractModule
 */
interface SettingContainer {
    val settings: MutableList<AbstractSetting<*>>

    fun setting(
        name: String,
        initialValue: Int,
        visibility: () -> Boolean = { true }
    ) = setting(BindSetting(name, initialValue, visibility))

    fun setting(
        name: String,
        initialValue: Boolean,
        visibility: () -> Boolean = { true }
    ) = setting(BooleanSetting(name, initialValue, visibility))

    fun setting(
        name: String,
        initialValue: Color,
        visibility: () -> Boolean = { true }
    ) = setting(ColorSetting(name, initialValue, visibility))

    fun <T : Enum<T>> setting(
        name: String,
        initialValue: T,
        visibility: () -> Boolean = { true }
    ) = setting(EnumSetting(name, initialValue, visibility))

    fun setting(
        name: String,
        visibility: () -> Boolean = { true }
    ) = setting(GroupSetting(name, visibility))

    fun <T> setting(
        name: String,
        initialValue: T,
        range: ClosedRange<T>,
        visibility: () -> Boolean = { true }
    ) where T : Number, T : Comparable<T> = setting(NumberSetting(name, initialValue, range, visibility))

    fun setting(
        name: String,
        initialValue: String,
        visibility: () -> Boolean = { true }
    ) = setting(StringSetting(name, initialValue, visibility))

    fun <T : AbstractSetting<V>, V> setting(setting: T): T =
        setting.also {
            settings.add(it)
            it.valueChanged(setting.initialValue, setting.initialValue)
        }

    operator fun iterator(): Iterator<AbstractSetting<*>> {
        return settings.iterator()
    }
}