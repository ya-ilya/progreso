package org.progreso.client.gui.clickgui.component.components

import net.minecraft.client.gui.DrawContext
import org.progreso.api.setting.settings.EnumSetting
import org.progreso.client.gui.clickgui.component.AbstractComponent
import org.progreso.client.gui.clickgui.component.SettingComponent
import org.progreso.client.gui.invoke
import java.awt.Color

class EnumComponent(
    setting: EnumSetting<*>,
    height: Int,
    parent: AbstractComponent
) : SettingComponent<EnumSetting<*>>(setting, height, parent) {
    override fun render(context: DrawContext, mouseX: Int, mouseY: Int) {
        super.render(context, mouseX, mouseY)

        context {
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
    }

    override fun mouseClicked(mouseX: Int, mouseY: Int, button: Int) {
        super.mouseClicked(mouseX, mouseY, button)

        when (button) {
            0 -> setting.next()
            1 -> setting.prev()
        }
    }
}