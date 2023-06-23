package org.progreso.api.setting.settings

import org.progreso.api.setting.AbstractSetting

class BooleanSetting(
    name: String,
    initialValue: Boolean,
    visibility: () -> Boolean = { true }
) : AbstractSetting<Boolean>(name, initialValue, visibility)