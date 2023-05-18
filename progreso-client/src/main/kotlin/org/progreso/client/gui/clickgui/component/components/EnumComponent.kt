package org.progreso.client.gui.clickgui.component.components

import org.progreso.api.setting.settings.EnumSetting
import org.progreso.client.gui.clickgui.component.AbstractComponent
import org.progreso.client.gui.clickgui.component.ChildComponent
import org.progreso.client.manager.managers.render.TextRenderManager.getStringWidth
import org.progreso.client.util.Render2DUtil.drawStringRelatively
import java.awt.Color

class EnumComponent(
    private val setting: EnumSetting<*>,
    height: Int,
    parent: AbstractComponent
) : ChildComponent(height, parent) {
    override val visible get() = setting.visibility()

    override fun drawComponent(mouseX: Int, mouseY: Int, partialTicks: Float) {
        super.drawComponent(mouseX, mouseY, partialTicks)

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

    override fun mouseClicked(mouseX: Int, mouseY: Int, mouseButton: Int) {
        super.mouseClicked(mouseX, mouseY, mouseButton)

        when (mouseButton) {
            0 -> setting.next()
            1 -> setting.prev()
        }
    }
}