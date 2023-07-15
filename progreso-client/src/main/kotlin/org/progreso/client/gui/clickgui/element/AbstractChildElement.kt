package org.progreso.client.gui.clickgui.element

import net.minecraft.client.gui.DrawContext
import org.progreso.client.gui.clickgui.element.data.ElementOffsets
import org.progreso.client.gui.invoke
import org.progreso.client.modules.client.ClickGUI

@Suppress("SuspiciousVarProperty")
abstract class AbstractChildElement(
    override var height: Int,
    val parent: ParentElement
) : Element {
    companion object {
        val mainColor get() = ClickGUI.mainColor
        val rectColor get() = ClickGUI.rectColor
        val descriptions get() = ClickGUI.descriptions
    }

    open var renderRect = true

    override var x = 0; get() = parent.x
    override var y = 0; get() = parent.getChildElementY(this)
    override var width = 0; get() = parent.width
    override val offsets = ElementOffsets(parent.offsets.childTextOffset)
    override val visible = true

    override fun render(context: DrawContext, mouseX: Int, mouseY: Int) {
        context {
            if (renderRect) {
                drawRect(x, y, width, height, rectColor)
            }
        }

        super.render(context, mouseX, mouseY)
    }
}