package org.progreso.client.gui.clickgui.element.elements

import net.minecraft.client.gui.DrawContext
import org.progreso.api.setting.settings.GroupSetting
import org.progreso.client.gui.clickgui.element.AbstractChildElement
import org.progreso.client.gui.clickgui.element.AbstractChildListElement
import org.progreso.client.gui.clickgui.element.ParentElement
import org.progreso.client.gui.clickgui.element.elements.ModuleElement.Companion.createElement
import org.progreso.client.gui.invokeSuper
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
            setting.value.map { it.createElement(height, this) }
        )

        header = object : AbstractChildElement(height, this@GroupElement) {
            override fun render(context: DrawContext, mouseX: Int, mouseY: Int) {
                super.render(context, mouseX, mouseY)

                context.invokeSuper(this) {
                    drawTextRelatively(
                        setting.name,
                        it.parent.offsets.textOffset,
                        Color.WHITE
                    )
                }
            }
        }
    }
}