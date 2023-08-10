package org.progreso.client.gui.clickgui.element.elements

import org.progreso.api.setting.AbstractSetting
import org.progreso.api.setting.settings.*
import org.progreso.client.gui.clickgui.element.AbstractChildElement
import org.progreso.client.gui.clickgui.element.ParentElement

abstract class SettingElement<S : AbstractSetting<*>>(
    protected val setting: S,
    height: Int,
    parent: ParentElement
) : AbstractChildElement(height, parent) {
    companion object {
        fun AbstractSetting<*>.createSettingElement(height: Int, parent: ParentElement): AbstractChildElement {
            return when (this) {
                is BindSetting -> BindElement(this, height, parent)
                is BooleanSetting -> BooleanElement(this, height, parent)
                is ColorSetting -> ColorElement(this, height, parent)
                is EnumSetting<*> -> EnumElement(this, height, parent)
                is GroupSetting -> GroupElement(this, height, parent)
                is NumberSetting<*> -> SliderElement(this, height, parent)
                is StringSetting -> StringElement(this, height, parent)
                else -> throw RuntimeException("Unknown setting type")
            }
        }
    }

    override val visible get() = setting.visibility()
}