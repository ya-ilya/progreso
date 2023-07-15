package org.progreso.client.gui.clickgui.element.elements

import org.progreso.api.setting.AbstractSetting
import org.progreso.client.gui.clickgui.element.AbstractChildElement
import org.progreso.client.gui.clickgui.element.ParentElement

open class SettingElement<S : AbstractSetting<*>>(
    protected val setting: S,
    height: Int,
    parent: ParentElement
) : AbstractChildElement(height, parent) {
    override val visible get() = setting.visibility()
}