package org.progreso.client.gui.clickgui.window

import net.minecraft.client.gui.DrawContext
import org.progreso.client.gui.clickgui.element.AbstractChildElement
import org.progreso.client.gui.clickgui.element.ParentElement
import org.progreso.client.gui.clickgui.element.data.ElementOffsets

@Suppress("SuspiciousVarProperty")
abstract class AbstractWindow(
    override var x: Int,
    override var y: Int,
    override var width: Int
) : ParentElement {
    override val elements = mutableListOf<AbstractChildElement>()
    override val visible = true
    override val offsets = ElementOffsets.DEFAULT

    private var dragging = false
    private var dragX = 0
    private var dragY = 0

    protected val windowElements = mutableListOf<AbstractChildElement>()
    private var opened = false

    override var height = 0; get() = visibleElements.sumOf { it.height }

    protected var header: AbstractChildElement? = null
        set(value) {
            if (field != null) {
                elements.removeAt(0)
            }

            field = value

            if (value != null) {
                elements.add(0, value)
            }
        }

    override fun render(context: DrawContext, mouseX: Int, mouseY: Int) {
        if (dragging) {
            x = mouseX - dragX
            y = mouseY - dragY
        }

        super.render(context, mouseX, mouseY)
    }

    override fun mouseClicked(mouseX: Int, mouseY: Int, button: Int) {
        if (button == 0 && header?.isHover(mouseX, mouseY) == true) {
            dragging = true
            dragX = mouseX - x
            dragY = mouseY - y
        }

        if (header?.isHover(mouseX, mouseY) == true && button == 1) {
            if (opened) {
                elements.removeIf { windowElements.contains(it) }
            } else {
                elements.addAll(windowElements)
            }

            opened = !opened
        }

        super.mouseClicked(mouseX, mouseY, button)
    }

    override fun mouseReleased(mouseX: Int, mouseY: Int, state: Int) {
        dragging = false

        super.mouseReleased(mouseX, mouseY, state)
    }
}