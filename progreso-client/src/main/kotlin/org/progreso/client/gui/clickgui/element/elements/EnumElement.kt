package org.progreso.client.gui.clickgui.element.elements

import net.minecraft.client.gui.DrawContext
import org.progreso.api.setting.settings.EnumSetting
import org.progreso.client.gui.clickgui.element.ParentElement
import org.progreso.client.gui.invoke
import java.awt.Color

class EnumElement(
    setting: EnumSetting<*>,
    height: Int,
    parent: ParentElement
) : SettingElement<EnumSetting<*>>(setting, height, parent) {
    override fun render(context: DrawContext, mouseX: Int, mouseY: Int) = context {
        drawTextRelatively(
            setting.name,
            offsets.textOffset,
            Color.WHITE
        )
        drawTextRelatively(
            setting.value.name,
            offsets.textOffset + getTextWidth("${setting.name}  "),
            mainColor
        )
    }

    override fun mouseClicked(mouseX: Int, mouseY: Int, button: Int) {
        when (button) {
            0 -> setting.next()
            1 -> setting.prev()
        }
    }
}