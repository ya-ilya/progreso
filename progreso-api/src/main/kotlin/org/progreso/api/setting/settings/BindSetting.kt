package org.progreso.api.setting.settings

import org.progreso.api.setting.AbstractSetting

open class BindSetting(
    name: String,
    initialValue: Int,
    visibility: () -> Boolean = { true }
) : AbstractSetting<Int>(name, initialValue, visibility)