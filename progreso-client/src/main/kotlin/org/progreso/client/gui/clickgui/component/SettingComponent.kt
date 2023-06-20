package org.progreso.client.gui.clickgui.component

import org.progreso.api.setting.AbstractSetting

open class SettingComponent<S : AbstractSetting<*>>(
    protected val setting: S,
    height: Int,
    parent: AbstractComponent
) : ChildComponent(height, parent) {
    override val visible get() = setting.visibility()
}