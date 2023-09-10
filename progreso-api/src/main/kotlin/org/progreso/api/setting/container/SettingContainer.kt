package org.progreso.api.setting.container

import org.progreso.api.setting.AbstractSetting
import org.progreso.api.setting.settings.*
import java.awt.Color
import kotlin.reflect.KClass

/**
 * Interface for setting container
 */
interface SettingContainer {
    val settings: MutableSet<AbstractSetting<*>>

    fun <T : AbstractSetting<*>> getSettingByName(name: String, clazz: KClass<T>): T {
        return getSettingByNameOrNull(name, clazz)!!
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : AbstractSetting<*>> getSettingByNameOrNull(name: String, clazz: KClass<T>): T? {
        return settings.first { it::class == clazz && it.name == name } as T?
    }

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
        setting.also { settings.add(it) }
}