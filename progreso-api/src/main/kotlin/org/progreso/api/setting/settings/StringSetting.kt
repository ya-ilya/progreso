package org.progreso.api.setting.settings

import org.progreso.api.setting.AbstractSetting

class StringSetting(
    name: String,
    initialValue: String,
    visibility: () -> Boolean
) : AbstractSetting<String>(name, initialValue, visibility)