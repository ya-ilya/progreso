package org.progreso.client.gui.clickgui.element.elements

import net.minecraft.client.gui.DrawContext
import org.progreso.api.setting.settings.GroupSetting
import org.progreso.client.gui.clickgui.element.AbstractChildElement
import org.progreso.client.gui.clickgui.element.AbstractChildListElement
import org.progreso.client.gui.clickgui.element.ParentElement
import org.progreso.client.gui.clickgui.element.elements.SettingElement.Companion.createSettingElement
import org.progreso.client.gui.drawTextRelatively
import org.progreso.client.gui.invoke
import java.awt.Color

class GroupElement(
    private val setting: GroupSetting,
    height: Int,
    parent: ParentElement
) : AbstractChildListElement(height, parent) {
    override val visible get() = setting.visibility()

    init {
        offsets.childTextOffset += 3

        listElements.addAll(
            setting.value.map { it.createSettingElement(height, this) }
        )

        header = object : AbstractChildElement(height, this@GroupElement) {
            override fun render(context: DrawContext, mouseX: Int, mouseY: Int) = context {
                drawTextRelatively(
                    header!!,
                    setting.name,
                    this@GroupElement.offsets.textOffset,
                    Color.WHITE
                )
            }
        }
    }
}