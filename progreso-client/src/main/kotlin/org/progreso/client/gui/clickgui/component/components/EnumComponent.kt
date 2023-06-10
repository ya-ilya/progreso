package org.progreso.client.gui.clickgui.component.components

import net.minecraft.client.gui.DrawContext
import org.progreso.api.setting.settings.EnumSetting
import org.progreso.client.gui.clickgui.component.AbstractComponent
import org.progreso.client.gui.clickgui.component.ChildComponent
import org.progreso.client.gui.use
import org.progreso.client.manager.managers.render.TextRenderManager.getStringWidth
import java.awt.Color

class EnumComponent(
    private val setting: EnumSetting<*>,
    height: Int,
    parent: AbstractComponent
) : ChildComponent(height, parent) {
    override val visible get() = setting.visibility()

    override fun render(context: DrawContext, mouseX: Int, mouseY: Int) {
        super.render(context, mouseX, mouseY)

        context.use {
            drawStringRelatively(
                setting.name,
                offsets.textOffset,
                Color.WHITE
            )
            drawStringRelatively(
                setting.value.name,
                offsets.textOffset + getStringWidth("${setting.name}  "),
                theme
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